package com.kt.worktimetrackermanager.data.remote.dto.enums

enum class ProjectStatus(val title: String) {
    NotStarted("Not Started"),
    InProgress("In Progress"),
    Completed("Completed"),
    OnHold("On Hold"),
    Cancelled("Cancelled")
}