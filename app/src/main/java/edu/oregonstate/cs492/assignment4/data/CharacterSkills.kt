package edu.oregonstate.cs492.assignment4.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass (generateAdapter = true)
data class CharacterSkills (
    @Json(name= "pve") val pveSkills: PVESkills,
    @Json(name = "pvp") val pvpSkills: PVPSkills,
    @Json(name = "wvw") val wvwSkills: WVWSkills
){
    @JsonClass (generateAdapter = true)
    data class PVESkills(
        @Json(name="heal") val healSkill:Int,
        @Json(name="utilities") val utilSkills:List<Int>
    )
    @JsonClass (generateAdapter = true)
    data class PVPSkills(
        @Json(name="heal") val healSkill:Int,
        @Json(name="utilities") val utilSkills:List<Int>
    )
    @JsonClass (generateAdapter = true)
    data class WVWSkills(
        @Json(name="heal") val healSkill:Int,
        @Json(name="utilities") val utilSkills:List<Int>
    )
}


