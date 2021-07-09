package br.eti.rafaelcouto.cryptocap.viewmodel

import androidx.lifecycle.*
import br.eti.rafaelcouto.cryptocap.application.network.model.Result
import br.eti.rafaelcouto.cryptocap.domain.model.CryptoItemUI
import br.eti.rafaelcouto.cryptocap.domain.usecase.abs.HomeUseCaseAbs
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeViewModel(
    private val useCase: HomeUseCaseAbs,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val mutableData = MutableLiveData<Result<List<CryptoItemUI>>>()

    val status = Transformations.map(mutableData) { it.status }
    val errorMessage = Transformations.map(mutableData) { it.error }
    val data = Transformations.map(mutableData) { it.data.orEmpty() }

    fun loadData() = viewModelScope.launch(dispatcher) {
        mutableData.postValue(Result.loading())
        useCase.fetchAll().collect { mutableData.postValue(it) }
    }
}
