package br.eti.rafaelcouto.cryptocap.domain.usecase

import br.eti.rafaelcouto.cryptocap.data.repository.abs.HomeRepositoryAbs
import br.eti.rafaelcouto.cryptocap.domain.mapper.abs.CryptoItemMapperAbs
import br.eti.rafaelcouto.cryptocap.domain.usecase.abs.HomeUseCaseAbs
import kotlinx.coroutines.flow.map

class HomeUseCase(
    private val repository: HomeRepositoryAbs,
    private val cryptoItemMapper: CryptoItemMapperAbs
) : HomeUseCaseAbs {

    override fun fetchAll() = repository.fetchAll().map { cryptoItemMapper.map(it) }
}
