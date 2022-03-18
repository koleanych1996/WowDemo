package com.example.wowdemo.network

import com.example.wowdemo.Constants.DATE_FORMAT_UTC
import com.google.gson.*
import java.lang.reflect.Type
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DateDeserializer : JsonSerializer<Date>, JsonDeserializer<Date> {

    private val dateFormatUtc = SimpleDateFormat(DATE_FORMAT_UTC, Locale.getDefault())

    override fun serialize(
        src: Date,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(dateFormatUtc.format(src))
    }

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Date? {
        if (json is JsonNull) {
            return null
        }

        if (json !is JsonPrimitive) {
            throw JsonParseException("Expected string, but was ${json.toString()}")
        }

        val text = json.asString
        if (text.isNullOrEmpty()) {
            return null
        }

        try {
            return dateFormatUtc.parse(text.removeRange(20, 23))
        } catch (e: ParseException) {
            throw JsonParseException("Cannot parse date: $text")
        }
    }
}