package edu.oregonstate.cs492.assignment4.data

import androidx.constraintlayout.widget.Guideline
import com.squareup.moshi.Moshi
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
interface GuildWarsAPIService {
    @GET("account/")
    suspend fun loadAccountInformation(
        @Query("access_token") key: String
    ) : Response<AccountInformation>
    @GET("characters/{character_name}")
    suspend fun loadCharacterInformation(
        @Path("character_name") character_name: String,
        @Query("access_token") key: String
    ) : Response<CharacterInformation>

    @GET("characters/")
    suspend fun  loadCharacterList(
        @Query("access_token") key: String
    ) : Response<List<String>>

    @GET("races/{id}")
    suspend fun loadRacialInformation(
        @Path("id") race_name:String
    ) : Response<RacialInformation>

    @GET("skills")
    suspend fun loadSkillInformation(
        @Query("ids") skill_id:String
    ) : Response<List<SkillInformation>>

    @GET("worlds/{worldID}")
    suspend fun loadWorldInformation_API(
        @Path("worldID") worldID:Int
    ) : Response<WorldInformation>

    @GET("guild/{id}")
    suspend fun loadGuildInformation(
        @Path("id") id:String
    ) : Response<GuildInformation>

    companion object{
        private const val BASE_URL = "https://api.guildwars2.com/v2/"

        fun create() : GuildWarsAPIService{
            val moshi = Moshi.Builder()
                .build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(GuildWarsAPIService::class.java)
        }
    }



}