package com.kt.worktimetrackermanager.data.remote.adapters

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.kt.worktimetrackermanager.data.remote.dto.enums.LogStatus
import java.lang.reflect.Type

class LogStatusAdapter : JsonSerializer<LogStatus>, JsonDeserializer<LogStatus> {
    override fun serialize(
        src: LogStatus,
        typeOfSrc: Type?,
        context: JsonSerializationContext?,
    ): JsonElement? {
        return JsonPrimitive(src.name)
    }

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?,
    ): LogStatus? {
        return LogStatus.valueOf(json.asString)
    }
}