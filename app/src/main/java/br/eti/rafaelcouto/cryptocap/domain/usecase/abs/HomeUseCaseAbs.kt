package br.eti.rafaelcouto.cryptocap.domain.usecase.abs

import br.eti.rafaelcouto.cryptocap.application.network.model.Result
import br.eti.rafaelcouto.cryptocap.domain.model.CryptoItemUI
import kotlinx.coroutines.flow.Flow

interface HomeUseCaseAbs {

    suspend fun fetchAll(): Flow<Result<List<CryptoItemUI>>>
}
