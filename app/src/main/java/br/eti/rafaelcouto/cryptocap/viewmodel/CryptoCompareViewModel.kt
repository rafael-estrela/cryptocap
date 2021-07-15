package br.eti.rafaelcouto.cryptocap.viewmodel

import androidx.lifecycle.*
import br.eti.rafaelcouto.cryptocap.application.network.model.Result
import br.eti.rafaelcouto.cryptocap.domain.model.CryptoCompareUI
import br.eti.rafaelcouto.cryptocap.domain.usecase.abs.CryptoCompareUseCaseAbs
import br.eti.rafaelcouto.cryptocap.ext.humanReadable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class CryptoCompareViewModel(
    private val useCase: CryptoCompareUseCaseAbs,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val data = MutableLiveData<Result<CryptoCompareUI>>()

    val fromAmount = MutableLiveData("1")

    val status = Transformations.map(data) { it.status }
    val errorMessage = Transformations.map(data) { it.error }

    val from = Transformations.map(data) { it.data?.from }
    val to = Transformations.map(data) { it.data?.to }

    val converted = MediatorLiveData<String>().apply {
        fun convert(optAmount: String?, optRatio: Double?) {
            val amount = try {
                optAmount?.toDouble()
            } catch (e: NumberFormatException) {
                null
            }

            amount?.let { numericAmount ->
                optRatio?.let { ratio ->
                    value = (numericAmount * ratio).humanReadable()
                }
            }
        }

        addSource(fromAmount) { convert(it, data.value?.data?.fromToRatio) }
        addSource(data) { convert(fromAmount.value, it.data?.fromToRatio) }
    }

    fun loadData(fromId: Long, toId: Long) = viewModelScope.launch {
        data.value = Result.loading()

        useCase.fetchInfo(fromId, toId).flowOn(dispatcher).collect {
            data.value = it
        }
    }

    fun swap() {
        data.value?.data?.let {
            data.value = Result.success(useCase.swap(it))
        }
    }
}
