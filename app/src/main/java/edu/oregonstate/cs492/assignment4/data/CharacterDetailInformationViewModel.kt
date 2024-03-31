package edu.oregonstate.cs492.assignment4.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class CharacterDetailInformationViewModel:ViewModel() {
    private val repository = CharacterInformationRepo(GuildWarsAPIService.create())
    private val _characterInfo = MutableLiveData<CharacterInformation?>(null)

    val characterInfo: LiveData<CharacterInformation?> = _characterInfo

    private val _error = MutableLiveData<Throwable?>(null)
    val error:LiveData<Throwable?> = _error

    private val _loading = MutableLiveData<Boolean?>(null)
    val loading:LiveData<Boolean?> = _loading

    fun loadCharDetailInformation_VM(characterName:String,apikey:String){
        viewModelScope.launch {
            _loading.value = true
            val result = repository.loadCharInformation_repo(characterName, apikey)
            _loading.value=false
            _error.value = result.exceptionOrNull()
            _characterInfo.value = result.getOrNull()
        }
    }
}