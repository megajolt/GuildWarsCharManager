package edu.oregonstate.cs492.assignment4.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass (generateAdapter = true)
data class SkillInformation(
    @Json(name="name") val name: String,
    @Json(name="facts") val facts:List<SkillFacts>?,
    @Json(name="description") val description:String,
    @Json(name="icon") val icon:String,
    @Json(name="id") val id:Int
) {
    @JsonClass (generateAdapter = true)
    data class SkillFacts(
        @Json(name="text") val text:String,
        @Json(name="type") val type: String,
        @Json(name="icon") val icon: String,
        @Json(name="value") val value:Any?
    )

}
