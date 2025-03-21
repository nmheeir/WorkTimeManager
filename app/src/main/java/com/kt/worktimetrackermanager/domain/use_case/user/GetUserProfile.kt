package com.kt.worktimetrackermanager.domain.use_case.user

import com.kt.worktimetrackermanager.data.remote.repositories.IUserRepo

class GetUserProfile (private val userRepo: IUserRepo) {
    suspend operator fun invoke(token: String) = userRepo.profile(token)
}