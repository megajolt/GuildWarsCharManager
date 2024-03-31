package edu.oregonstate.cs492.assignment4.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AccountInformation(
    val id:String,
    val age:Int,
    val name:String,
    val world:Int,
    val created:String,
    val guilds:List<String>,
)