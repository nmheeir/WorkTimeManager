package com.kt.worktimetrackermanager.domain.use_case.project

import com.kt.worktimetrackermanager.data.remote.dto.request.CreateProjectRequest
import com.kt.worktimetrackermanager.data.remote.repositories.IProjectRepo

data class ProjectUseCase(
    val getProjects: GetProjects,
    val getTasksFromProject: GetTasksFromProject,
    val createProject: CreateProject,
)

class GetProjects(
    private val iProjectRepo: IProjectRepo,
) {
    suspend operator fun invoke(token: String) = iProjectRepo.getProjects(token)
}

class GetTasksFromProject(
    private val iProjectRepo: IProjectRepo,
) {
    suspend operator fun invoke(token: String, id: Int) =
        iProjectRepo.getTasksFromProject(token, id)
}

class CreateProject(
    private val iProjectRepo: IProjectRepo,
) {
    suspend operator fun invoke(token: String, project: CreateProjectRequest) =
        iProjectRepo.createProject(token, project)
}
