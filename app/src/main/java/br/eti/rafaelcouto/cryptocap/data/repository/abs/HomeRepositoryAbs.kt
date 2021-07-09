package br.eti.rafaelcouto.cryptocap.data.repository.abs

import br.eti.rafaelcouto.cryptocap.application.network.model.Result
import br.eti.rafaelcouto.cryptocap.data.model.CryptoItem
import kotlinx.coroutines.flow.Flow

interface HomeRepositoryAbs {

    suspend fun fetchAll(): Flow<Result<List<CryptoItem>>>
}
