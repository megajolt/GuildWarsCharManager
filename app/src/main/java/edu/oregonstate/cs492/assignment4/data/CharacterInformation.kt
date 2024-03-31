package edu.oregonstate.cs492.assignment4.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class CharacterInformation(
   @Json(name="name") val name: String,
    @Json(name="race") val race: String,
   @Json(name="gender") val gender: String,
    @Json(name="profession") val profession: String,
    @Json(name = "level") val level: Int,
    @Json(name = "skills") val skills: CharacterSkills
)
