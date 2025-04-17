package com.kt.worktimetrackermanager.presentation.viewmodels.memberManager

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kt.worktimetrackermanager.core.ext.isValidEmail
import com.kt.worktimetrackermanager.core.ext.isValidPassword
import com.kt.worktimetrackermanager.core.presentation.utils.Helper
import com.kt.worktimetrackermanager.data.local.LocalUserManager
import com.kt.worktimetrackermanager.data.remote.dto.enum.EmployeeType
import com.kt.worktimetrackermanager.data.remote.dto.enum.Role
import com.kt.worktimetrackermanager.data.remote.dto.request.AddUserRequest
import com.kt.worktimetrackermanager.data.remote.dto.request.UpdateUserRequest
import com.kt.worktimetrackermanager.data.remote.dto.response.Team
import com.kt.worktimetrackermanager.domain.use_case.team.TeamUseCase
import com.kt.worktimetrackermanager.domain.use_case.user.UserUseCase
import com.skydoves.sandwich.message
import com.skydoves.sandwich.retrofit.errorBody
import com.skydoves.sandwich.retrofit.statusCode
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnException
import com.skydoves.sandwich.suspendOnSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject


@HiltViewModel
class AddMemberViewModel @Inject constructor(
    private val localUserManager: LocalUserManager,
    private val userUseCase : UserUseCase,
    private val teamUseCase: TeamUseCase
) : ViewModel() {
    private val _channel = Channel<AddMemberUiEvent>()
    val channel = _channel.receiveAsFlow()

    private var _state = MutableStateFlow(AddMemberUiState())
    val uiState = _state
        .onStart {
            if (_state.value.userId != null) {
                getUserById()
            }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, AddMemberUiState())

    init {
        getCompanyTeams()

    }

    fun setUserId(userId: Int?) {
        _state.value = _state.value.copy(
            userId = userId
        )
    }

    fun onAction(action: AddMemberUiAction) {
        when(action) {
            AddMemberUiAction.AddMember -> {
                var isVaid = true;
                if (_state.value.email.isEmpty() || _state.value.emailError != null) {
                    isVaid = false
                    _state.value = _state.value.copy(
                        emailError = "Email is required or invalid"
                    )
                }

                if (_state.value.userName.isEmpty() || _state.value.usernameError != null) {
                    isVaid = false
                    _state.value = _state.value.copy(
                        usernameError = "Username is required or invalid"
                    )
                }

                if (_state.value.password.isEmpty() || _state.value.passwordError != null) {
                    isVaid = false
                    _state.value = _state.value.copy(
                        passwordError = "Password is required or invalid"
                    )
                }

                if(isVaid) {
                    if(_state.value.userId == null) {
                        addUser()
                    }
                    else{
                        updateUser()
                    }
                }
            }

            is AddMemberUiAction.OnFieldChange -> {
                onFieldChange(action.fieldName, action.value)
            }
        }
    }


    private fun onFieldChange(fieldName: String, value: Any?) {
        _state.value = when (fieldName) {
            "isLoading" -> _state.value.copy(isLoading = value as Boolean)
            "address" -> _state.value.copy(address = value as String)
            "avatarURL" -> _state.value.copy(avatarURL = value as String)
            "companyId" -> _state.value.copy(companyId = value as Int?)
            "createdAt" -> _state.value.copy(createdAt = value as String)
            "dayOfBirth" -> _state.value.copy(dayOfBirth = value as LocalDate)
            "department" -> _state.value.copy(department = value as String)
            "designation" -> _state.value.copy(designation = value as String)
            "email" -> {
                val isEmailEmpty = (value as String).isEmpty()
                val emailError =
                    if (isEmailEmpty) "Email can't be empty"
                    else if (!value.isValidEmail()) "Email is not valid"
                    else null
                _state.value.copy(email = value, emailError = emailError)
            }
            "employeeType" -> _state.value.copy(employeeType = value as EmployeeType)
            "password" -> {
                val isPasswordEmpty = (value as String).isEmpty()
                val passwordError =
                    if (isPasswordEmpty) "Password can't be empty"
                    else if (!value.isValidPassword()) "Password need both special character and number."
                    else null
                _state.value.copy(password = value, passwordError = passwordError)
            }
            "phoneNumber" -> _state.value.copy(phoneNumber = value as String)
            "role" -> _state.value.copy(role = value as Role)
            "teamId" -> _state.value.copy(teamId = (value as Team?)?.id)
            "userFullName" -> _state.value.copy(userFullName = value as String)
            "userName" -> {
                val isUsernameEmpty = (value as String).isEmpty()
                val isEnoughCharacter = value.length > 8;
                val usernameError =
                    if (isUsernameEmpty) "Username can't be empty"
                    else if (!isEnoughCharacter) "Username need atleast 8 characters."
                    else null
                _state.value.copy(userName = value, usernameError = usernameError)
            }
            else -> _state.value
        }
    }

    private fun getCompanyTeams() {
        viewModelScope.launch {
            val token = localUserManager.readAccessToken()

            teamUseCase
                .getCompanyTeams(token, pageNumber = 1, pageSize = Int.MAX_VALUE, searchValue = "")
                .suspendOnSuccess {
                    _state.value = _state.value.copy(
                        companyTeams = this.data.data!!
                    )
                    _channel.send(AddMemberUiEvent.Success)
                }
                .suspendOnError {
                    _channel.send(
                        AddMemberUiEvent.Failure(
                            this.errorBody.toString()
                        )
                    )
                    Timber.d( "getCompanyTeams: " + this.message() + this.statusCode.toString())
                }
                .suspendOnException {
                    _channel.send(AddMemberUiEvent.Failure(this.message ?: ""))
                    Timber.d(
                        "getCompanyTeams: " + this.message
                    )
                }
        }
    }

    private fun addUser() {
        viewModelScope.launch {
            val token = localUserManager.readAccessToken()

            _state.value = _state.value.copy(
                isLoading = true
            )
            val newUser = AddUserRequest(
                address = _state.value.address,
                avatarURL = _state.value.avatarURL,
                dayOfBirth = Helper.convertLocalDateToISOString(_state.value.dayOfBirth.toString()),
                companyId = 1,
                department = _state.value.department,
                designation = _state.value.designation,
                email = _state.value.email,
                employeeType = _state.value.employeeType.value,
                password = _state.value.password,
                phoneNumber = _state.value.phoneNumber,
                role = _state.value.role.ordinal,
                teamId = _state.value.teamId,
                userFullName = _state.value.userFullName,
                userName = _state.value.userName
            )
            userUseCase
                .addUser(token, newUser)
                .suspendOnSuccess {
                    _state.value = _state.value.copy(
                        isLoading = false
                    )
                    _channel.send(AddMemberUiEvent.Success)
                }
                .suspendOnError {
                    _channel.send(
                        AddMemberUiEvent.Failure(
                            this.errorBody.toString()
                        )
                    )
                    Timber.d("addMember: " + this.message() + this.statusCode.toString())
                }
                .suspendOnException {
                    _channel.send(AddMemberUiEvent.Failure(this.message ?: ""))
                    Timber.d(
                        "addMember: " + this.message
                    )
                }
        }
    }


    private fun updateUser() {
        viewModelScope.launch {
            val token = localUserManager.readAccessToken()

            _state.value = _state.value.copy(
                isLoading = true
            )
            val newUser = UpdateUserRequest(
                id = _state.value.userId!!,
                address = _state.value.address,
                avatarURL = _state.value.avatarURL,
                dayOfBirth = Helper.convertLocalDateToISOString(_state.value.dayOfBirth.toString()),
                companyId = 1,
                department = _state.value.department,
                designation = _state.value.designation,
                email = _state.value.email,
                employeeType = _state.value.employeeType.value,
                password = _state.value.password,
                phoneNumber = _state.value.phoneNumber,
                role = _state.value.role.ordinal,
                teamId = _state.value.teamId,
                userFullName = _state.value.userFullName,
                userName = _state.value.userName
            )

            Log.d("UpdateUser", newUser.toString())
            userUseCase
                .updateUser(token, newUser)
                .suspendOnSuccess {
                    _state.value = _state.value.copy(
                        isLoading = false
                    )
                    _channel.send(AddMemberUiEvent.Success)
                }
                .suspendOnError {
                    _channel.send(
                        AddMemberUiEvent.Failure(
                            this.errorBody.toString()
                        )
                    )
                    Timber.d("updateMember: " + this.message() + this.statusCode.toString())
                }
                .suspendOnException {
                    _channel.send(AddMemberUiEvent.Failure(this.message ?: ""))
                    Timber.d(
                        "updateMember: " + this.message
                    )
                }
        }
    }

    private fun getUserById() {
        viewModelScope.launch {

            _state.value = _state.value.copy(
                isLoading = true
            )
            _state.value.userId?.let {
                userUseCase.getUserById(it)
                    .suspendOnSuccess {
                        Timber.d("getUserById: ${this.data.data}")
                        _state.value = _state.value.copy(
                            address = this.data.data!!.address,
                            avatarURL = this.data.data!!.avatarUrl ?: "",
                            createdAt = this.data.data!!.createdAt,
                            dayOfBirth = LocalDateTime.parse(this.data.data!!.dayOfBirth).toLocalDate(),
                            department = this.data.data!!.department,
                            designation = this.data.data!!.designation,
                            email = this.data.data!!.email,
                            employeeType = EmployeeType.fromInt(this.data.data!!.employeeType.ordinal)!!,
                            phoneNumber = this.data.data!!.phoneNumber,
                            role = Role.fromInt(this.data.data!!.role.ordinal),
                            teamId = this.data.data!!.teamId,
                            userFullName = this.data.data!!.userFullName,
                            userName = this.data.data!!.userName,
                        )
                    }
                    .suspendOnError {
                        _channel.send(
                            AddMemberUiEvent.Failure(
                                this.errorBody.toString()
                            )
                        )
                        Timber.d("getUserById: " + this.message() + this.statusCode.toString())
                    }
                    .suspendOnException {
                        _channel.send(AddMemberUiEvent.Failure(this.message ?: ""))
                        Timber.d(
                            "getUserById: " + this.message
                        )
                    }
            }
        }
    }
}

sealed class AddMemberUiEvent {
    data object Success : AddMemberUiEvent()
    data class Failure(val message: String) : AddMemberUiEvent()
}


data class AddMemberUiState(
    var isLoading: Boolean = false,
    var address: String = "",
    var avatarURL: String = "",
    var companyId: Int? = null,
    var createdAt: String = "",
    var dayOfBirth: LocalDate = LocalDate.now(),
    var department: String = "",
    var designation: String = "",
    var email: String = "",
    var employeeType: EmployeeType = EmployeeType.FULL_TIME,
    var password: String = "",
    var phoneNumber: String = "",
    var role: Role = Role.Staff,
    var teamId: Int? = null,
    var userFullName: String = "",
    var userName: String = "",
    var companyTeams : List<Team> = emptyList(),

    // loi
    val usernameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,

    // id user for update
    val userId: Int? = null
)

sealed interface AddMemberUiAction {
    data class OnFieldChange(var fieldName: String, var value: Any?) : AddMemberUiAction
    data object AddMember : AddMemberUiEvent(), AddMemberUiAction
}
