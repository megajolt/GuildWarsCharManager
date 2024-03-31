package edu.oregonstate.cs492.assignment4.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class RaceInformationViewModel : ViewModel() {
    private val repository = RaceInformationRepo(GuildWarsAPIService.create())
    private val _raceInfo = MutableLiveData<RacialInformation?>(null)
    val raceInfo :LiveData<RacialInformation?> = _raceInfo

    private val _error = MutableLiveData<Throwable?>(null)
    val error: LiveData<Throwable?> = _error

    private val _loading = MutableLiveData<Boolean?>(null)
    val loading: LiveData<Boolean?> = _loading

    fun loadRaceInformation_VM(id:String){
        viewModelScope.launch {
            _loading.value = true
            val result = repository.loadRaceInformation_repo(id)
            _loading.value=false
            _error.value = result.exceptionOrNull()
            _raceInfo.value = result.getOrNull()
        }
    }
}