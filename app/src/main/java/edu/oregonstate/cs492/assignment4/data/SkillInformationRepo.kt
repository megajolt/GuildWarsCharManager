package edu.oregonstate.cs492.assignment4.data

import android.window.OnBackInvokedDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class SkillInformationRepo (
    private val service:GuildWarsAPIService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO)
{
    suspend fun loadSkillInformation_repo(id:String): Result<List<SkillInformation>?>{
        val response = service.loadSkillInformation(id)
        return Result.success(response.body())
    }
}