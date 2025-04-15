package com.kt.worktimetrackermanager.data.remote.adapters

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.kt.worktimetrackermanager.data.remote.dto.enum.Role
import java.lang.reflect.Type

class RoleAdapter : JsonDeserializer<Role>, JsonSerializer<Role> {
//    override fun deserialize(
//        json: JsonElement,
//        typeOfT: Type,
//        context: JsonDeserializationContext,
//    ): Role {
//        return Role.valueOf(json.asString)
//    }
override fun deserialize(
    json: JsonElement,
    typeOfT: Type,
    context: JsonDeserializationContext,
): Role {
    val value = json.asInt
    return Role.fromInt(value)
}
    override fun serialize(
        src: Role,
        typeOfSrc: Type,
        context: JsonSerializationContext,
    ): JsonElement {
        return JsonPrimitive(src.name)
    }
}