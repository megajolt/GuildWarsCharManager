package edu.oregonstate.cs492.assignment4.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class CharacterInformationRepo(
    private val service:GuildWarsAPIService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO) {
    suspend fun loadCharInformation_repo(characterName:String,apikey:String): Result<CharacterInformation?>{
        val response = service.loadCharacterInformation(characterName,apikey)
        return Result.success(response.body())
    }
}