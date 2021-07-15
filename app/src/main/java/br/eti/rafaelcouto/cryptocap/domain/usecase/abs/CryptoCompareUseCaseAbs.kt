package br.eti.rafaelcouto.cryptocap.domain.usecase.abs

import br.eti.rafaelcouto.cryptocap.application.network.model.Result
import br.eti.rafaelcouto.cryptocap.domain.model.CryptoCompareUI
import kotlinx.coroutines.flow.Flow

interface CryptoCompareUseCaseAbs {

    fun fetchInfo(fromId: Long, toId: Long): Flow<Result<CryptoCompareUI>>
    fun swap(old: CryptoCompareUI): CryptoCompareUI
}
