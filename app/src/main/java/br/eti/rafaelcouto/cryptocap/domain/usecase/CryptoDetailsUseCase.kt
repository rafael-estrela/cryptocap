package br.eti.rafaelcouto.cryptocap.domain.usecase

import br.eti.rafaelcouto.cryptocap.data.repository.abs.CryptoDetailsRepositoryAbs
import br.eti.rafaelcouto.cryptocap.domain.mapper.abs.CryptoDetailsMapperAbs
import br.eti.rafaelcouto.cryptocap.domain.usecase.abs.CryptoDetailsUseCaseAbs
import kotlinx.coroutines.flow.zip

class CryptoDetailsUseCase(
    private val repository: CryptoDetailsRepositoryAbs,
    private val mapper: CryptoDetailsMapperAbs
) : CryptoDetailsUseCaseAbs {

    override fun fetchDetails(id: Long) = repository.fetchDetails(id)
        .zip(repository.fetchQuotes(id)) { details, quotes -> mapper.map(details, quotes) }
}
