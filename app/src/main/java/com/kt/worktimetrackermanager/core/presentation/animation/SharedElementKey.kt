package com.kt.worktimetrackermanager.core.presentation.animation

data class ProfileSharedElementKey(
    val id: Int,
    val type: ProfileSharedElementType,
)

enum class ProfileSharedElementType {
    Bounds,
    Image,
    Title,
    Tagline,
    Background
}