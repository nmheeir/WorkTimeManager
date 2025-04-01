package com.kt.worktimetrackermanager.data.remote.dto.enum

enum class ProjectStatus(val title: String) {
    NotStarted("Not Started"),
    InProgress("In Progress"),
    Completed("Completed"),
    OnHold("On Hold"),
    Cancelled("Cancelled")
}