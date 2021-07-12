package br.eti.rafaelcouto.cryptocap.data.repository.abs

import androidx.paging.PagingData
import br.eti.rafaelcouto.cryptocap.data.model.CryptoItem
import kotlinx.coroutines.flow.Flow

interface HomeRepositoryAbs {

    suspend fun fetchAll(): Flow<PagingData<CryptoItem>>
}
