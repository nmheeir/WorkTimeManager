package com.kt.worktimetrackermanager.domain.use_case.project

import com.kt.worktimetrackermanager.data.remote.dto.enums.ProjectStatus
import com.kt.worktimetrackermanager.data.remote.dto.request.CreateProjectRequest
import com.kt.worktimetrackermanager.data.remote.repositories.IProjectRepo

data class ProjectUseCase(
    val getProjects: GetProjects,
    val getProject: GetProject,
    val getTasksFromProject: GetTasksFromProject,
    val createProject: CreateProject,
)

class GetProjects(
    private val iProjectRepo: IProjectRepo,
) {
    suspend operator fun invoke(
        token: String,
        pageNumber: Int,
        pageSize: Int,
        filter: ProjectStatus? = null,
    ) = iProjectRepo.getProjects(token, pageNumber, pageSize, filter)
}

class GetProject(
    private val iProjectRepo: IProjectRepo,
) {
    suspend operator fun invoke(token: String, id: Int) = iProjectRepo.getProject(token, id)
}

class GetTasksFromProject(
    private val iProjectRepo: IProjectRepo,
) {
    suspend operator fun invoke(
        token: String,
        id: Int,
        pageNumber: Int,
        pageSize: Int,
        filter: ProjectStatus? = null,
    ) =
        iProjectRepo.getTasksFromProject(token, id, pageNumber, pageSize, filter)
}

class CreateProject(
    private val iProjectRepo: IProjectRepo,
) {
    suspend operator fun invoke(token: String, project: CreateProjectRequest) =
        iProjectRepo.createProject(token, project)
}
