package com.kt.worktimetrackermanager.data.remote.dto.enums

enum class EmployeeType(val value: Int) {
    PERNAMENT(0),
    FULL_TIME(1),
    PART_TIME(2);

    companion object {
        fun fromInt(value: Int): EmployeeType? {
            return entries.find { it.value == value }
        }
        fun fromIntToName(value: Int): String? {
            return entries.find { it.value == value }?.name
                ?.replace("_", " ")
                ?.split(" ")
                ?.joinToString(" ") { it.lowercase().replaceFirstChar { char -> char.uppercase() } }
        }
    }
}