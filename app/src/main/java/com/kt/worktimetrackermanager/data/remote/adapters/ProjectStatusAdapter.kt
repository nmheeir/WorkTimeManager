package com.kt.worktimetrackermanager.data.remote.adapters

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.kt.worktimetrackermanager.data.remote.dto.enums.ProjectStatus
import java.lang.reflect.Type

class ProjectStatusAdapter : JsonDeserializer<ProjectStatus>, JsonSerializer<ProjectStatus> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext?,
    ): ProjectStatus {
        return ProjectStatus.valueOf(json.asString)
    }

    override fun serialize(
        src: ProjectStatus,
        typeOfSrc: Type,
        context: JsonSerializationContext?,
    ): JsonElement {
        return JsonPrimitive(src.name)
    }
}