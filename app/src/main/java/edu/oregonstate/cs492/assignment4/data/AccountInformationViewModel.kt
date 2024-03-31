package edu.oregonstate.cs492.assignment4.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AccountInformationViewModel : ViewModel(){
    private val repository = AccountInformationRepo(GuildWarsAPIService.create())
    private val _accountinfo = MutableLiveData<AccountInformation?>(null)
    val accountinfo:LiveData<AccountInformation?> = _accountinfo

    private val _error = MutableLiveData<Throwable?>(null)
    val error:LiveData<Throwable?> = _error

    private val _loading = MutableLiveData<Boolean?>(null)
    val loading:LiveData<Boolean?> = _loading

    private val _world = MutableLiveData<WorldInformation?>(null)
    val world:LiveData<WorldInformation?> = _world

    private val _characterNames = MutableLiveData<List<String?>>(null)
    private val _characters = MutableLiveData<List<CharacterInformation?>>(null)
    val characters:LiveData<List<CharacterInformation?>> = _characters

    private val _guilds = MutableLiveData<List<GuildInformation?>>(null)
    val guilds:LiveData<List<GuildInformation?>> = _guilds


    private var _loadingAccount:Boolean = false
    private var _loadingWorld:Boolean = false
    private var _loadingCharacters:Boolean = false
    private var _loadingGuild:Boolean = false



    fun loadAccountInformation_VM(
        api_key:String,
        sort:String?
    ){
        viewModelScope.launch {
            _loadingAccount = true
            updateLoading()
            val result = repository.loadAccountInformation_repo(api_key)

            _error.value = result.exceptionOrNull()
            _accountinfo.value = result.getOrNull()
            getCharacters_VM(api_key,sort)
            _loadingAccount = false
        }
    }
    fun checkWorldName_VM(worldID:Int){
        viewModelScope.launch {
            _loadingWorld = true
            updateLoading()

            val result = repository.checkWorldName(worldID)

            _error.value = result.exceptionOrNull()
            _world.value = result.getOrNull()
            _loadingWorld = false
        }
    }
    fun getCharacters_VM(api_key: String, sort:String?){
        viewModelScope.launch {
            _loadingCharacters = true
            updateLoading()

            val namesResult = repository.getCharacterNames(api_key)
            _error.value = namesResult.exceptionOrNull()
            _characterNames.value = namesResult.getOrNull()
            val charResult = repository.getCharacters(api_key, _characterNames.value, sort)
            _error.value = charResult.exceptionOrNull()
            _characters.value = charResult.getOrNull()
            _loadingCharacters = false

        }
    }

    fun getGuildName_VM(guildIDs:List<String>){
        viewModelScope.launch {
            _loadingGuild = true
            updateLoading()
            val result = repository.checkGuildName(guildIDs)
            _guilds.value = result.getOrNull()
            _error.value = result.exceptionOrNull()
            _loadingGuild = false

        }
    }



    fun updateLoading(){
        val disable:Boolean = true

        if ((_loadingAccount || _loadingWorld || _loadingCharacters || _loadingGuild) && disable == false){
            _loading.value = true
        }
        else{
            _loading.value = false
        }
    }


}