package edu.oregonstate.cs492.assignment4.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WorldInformation(
    val id:Int,
    val name:String
)