package edu.oregonstate.cs492.assignment4.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GuildInformation(
    val id:String,
    val name:String
)
