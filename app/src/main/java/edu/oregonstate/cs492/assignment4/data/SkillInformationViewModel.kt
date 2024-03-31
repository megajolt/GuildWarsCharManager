package edu.oregonstate.cs492.assignment4.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class SkillInformationViewModel:ViewModel() {
    private val repository = SkillInformationRepo(GuildWarsAPIService.create())

    private val _skillInfo = MutableLiveData<List<SkillInformation>?>(null)
    val skillInfo:LiveData<List<SkillInformation>?> = _skillInfo

    private val _error = MutableLiveData<Throwable?>(null)
    val error: LiveData<Throwable?> = _error

    private val _loading = MutableLiveData<Boolean?>(null)
    val loading: LiveData<Boolean?> = _loading

    fun loadSkillInformation_VM(id:String){
        viewModelScope.launch {
            _loading.value = true
            val result = repository.loadSkillInformation_repo(id)
            _loading.value=false
            _error.value = result.exceptionOrNull()
            _skillInfo.value = result.getOrNull()
        }
    }
}