package edu.oregonstate.cs492.assignment4.data

import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import java.sql.ResultSet

class AccountInformationRepo(
    private val service:GuildWarsAPIService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
){
    private val tag = "AccountInformationRepo"
    suspend fun loadAccountInformation_repo(
        apikey:String
    ) : Result<AccountInformation?> {
        val response = service.loadAccountInformation(apikey)
        return Result.success(response.body())
    }
    suspend fun checkWorldName(
        worldID:Int
    ) : Result<WorldInformation?>{
        val response = service.loadWorldInformation_API(worldID)
        return Result.success(response.body())
    }

    suspend fun checkGuildName(
        guildIDs:List<String>
    ):Result<List<GuildInformation?>> {
        val tobereturned:MutableList<GuildInformation?> = mutableListOf()
        for(element in guildIDs){
            val toinsert = service.loadGuildInformation(element).body()
            Log.d(tag, "Received ${toinsert?.name} from API service, with ID: ${element}")
            tobereturned.add(toinsert)
            Log.d(tag, tobereturned.toString())
        }
        return Result.success(tobereturned)
    }

    suspend fun getCharacterNames(
        apikey: String
    ) : Result<List<String>?>{
        val response = service.loadCharacterList(apikey)
        return Result.success(response.body())
    }

    suspend fun getCharacters(
        apikey: String,
        names: List<String?>?,
        sort:String?
    ) : Result<List<CharacterInformation?>>{

        val tobereturned:MutableList<CharacterInformation?> = mutableListOf()
        if (names != null) {
            for(element in names){
                val toinsert = element?.let { service.loadCharacterInformation(it,apikey).body() }
                Log.d(tag, "Received ${toinsert?.name}, ${toinsert?.race} from Character API service, with Name: ${element}")
                tobereturned.add(toinsert)
                Log.d(tag, tobereturned.toString())
            }
        }
        if(sort == "alphabetical") {
            tobereturned.sortBy {
                it?.name
            }
        }
        else if (sort == "level") {
            tobereturned.sortBy {
                it?.level
            }
        }
        else{
            tobereturned.sortBy {
                it?.race
            }
            }

        return Result.success(tobereturned)
    }


}