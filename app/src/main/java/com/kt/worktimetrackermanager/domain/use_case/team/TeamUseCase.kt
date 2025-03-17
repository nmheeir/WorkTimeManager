package com.kt.worktimetrackermanager.domain.use_case.team

data class TeamUseCase(
    val getCompanyTeams: GetCompanyTeam,
    val getCompanyTeamById: GetCompanyTeamById,
    val createTeam: CreateTeam,
)