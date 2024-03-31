package edu.oregonstate.cs492.assignment4.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class RaceInformationRepo(private val service:GuildWarsAPIService,
                          private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO) {
    suspend fun loadRaceInformation_repo(
        id: String
    ) : Result<RacialInformation?> {
        val response = service.loadRacialInformation(id)
        return Result.success(response.body())
    }
}