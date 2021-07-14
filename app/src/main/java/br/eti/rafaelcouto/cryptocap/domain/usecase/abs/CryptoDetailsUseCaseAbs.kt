package br.eti.rafaelcouto.cryptocap.domain.usecase.abs

import br.eti.rafaelcouto.cryptocap.application.network.model.Result
import br.eti.rafaelcouto.cryptocap.domain.model.CryptoDetailsUI
import kotlinx.coroutines.flow.Flow

interface CryptoDetailsUseCaseAbs {

    fun fetchDetails(id: Long): Flow<Result<CryptoDetailsUI>>
}
