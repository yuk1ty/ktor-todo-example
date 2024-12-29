package com.github.yuk1ty.todoAppKt.shared.modules

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import java.time.LocalDateTime
import java.util.UUID

private object UUIDAsStringSerializer : KSerializer<UUID> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("java.util.UUID", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): UUID {
        val uuidAsString = decoder.decodeString()
        return UUID.fromString(uuidAsString)
    }

    override fun serialize(encoder: Encoder, value: UUID) {
        val uuidAsString = value.toString()
        encoder.encodeString(uuidAsString)
    }

}

private object LocalDateTimeSerializer : KSerializer<LocalDateTime> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("java.time.LocalDateTime", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): LocalDateTime {
        val localDateTimeAsString = decoder.decodeString()
        return LocalDateTime.parse(localDateTimeAsString)
    }

    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        val localDateTimeAsString = value.toString()
        encoder.encodeString(localDateTimeAsString)
    }
}

internal fun Application.registerSerializers() {
    val module = SerializersModule {
        contextual(UUIDAsStringSerializer)
        contextual(LocalDateTimeSerializer)
    }
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
            serializersModule = module
        })
    }
}