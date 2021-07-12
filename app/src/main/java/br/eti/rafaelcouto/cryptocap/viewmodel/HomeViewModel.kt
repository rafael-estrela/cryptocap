package br.eti.rafaelcouto.cryptocap.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import br.eti.rafaelcouto.cryptocap.domain.model.CryptoItemUI
import br.eti.rafaelcouto.cryptocap.domain.usecase.abs.HomeUseCaseAbs
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeViewModel(
    private val useCase: HomeUseCaseAbs
) : ViewModel() {

    private val mutableData = MutableLiveData<PagingData<CryptoItemUI>>()

    val data get() = mutableData as LiveData<PagingData<CryptoItemUI>>

    fun loadData() = viewModelScope.launch {
        useCase.fetchAll().collect { mutableData.value = it }
    }
}
