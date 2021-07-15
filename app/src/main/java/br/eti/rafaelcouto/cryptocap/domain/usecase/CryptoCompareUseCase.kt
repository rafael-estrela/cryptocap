package br.eti.rafaelcouto.cryptocap.domain.usecase

import br.eti.rafaelcouto.cryptocap.application.network.model.Result
import br.eti.rafaelcouto.cryptocap.data.repository.abs.CryptoDetailsRepositoryAbs
import br.eti.rafaelcouto.cryptocap.domain.mapper.abs.CryptoCompareMapperAbs
import br.eti.rafaelcouto.cryptocap.domain.model.CryptoCompareUI
import br.eti.rafaelcouto.cryptocap.domain.usecase.abs.CryptoCompareUseCaseAbs
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.zip

class CryptoCompareUseCase(
    private val repository: CryptoDetailsRepositoryAbs,
    private val mapper: CryptoCompareMapperAbs
) : CryptoCompareUseCaseAbs {

    override fun fetchInfo(fromId: Long, toId: Long): Flow<Result<CryptoCompareUI>> {
        val fromResult = repository.fetchDetails(fromId)
            .zip(repository.fetchQuotes(fromId)) { details, quotes -> mapper.mapElement(details, quotes) }

        val toResult = repository.fetchDetails(toId)
            .zip(repository.fetchQuotes(toId)) { details, quotes -> mapper.mapElement(details, quotes) }

        return fromResult.zip(toResult) { from, to -> mapper.map(from, to) }
    }

    override fun swap(old: CryptoCompareUI) = mapper.map(old.to, old.from)
}
