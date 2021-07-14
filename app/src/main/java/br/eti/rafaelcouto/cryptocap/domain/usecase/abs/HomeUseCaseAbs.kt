package br.eti.rafaelcouto.cryptocap.domain.usecase.abs

import androidx.paging.PagingData
import br.eti.rafaelcouto.cryptocap.domain.model.CryptoItemUI
import kotlinx.coroutines.flow.Flow

interface HomeUseCaseAbs {

    fun fetchAll(): Flow<PagingData<CryptoItemUI>>
}
