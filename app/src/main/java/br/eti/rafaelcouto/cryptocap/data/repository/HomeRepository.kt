package br.eti.rafaelcouto.cryptocap.data.repository

import br.eti.rafaelcouto.cryptocap.data.api.HomeApi
import br.eti.rafaelcouto.cryptocap.data.repository.abs.HomeRepositoryAbs
import kotlinx.coroutines.flow.flowOf

class HomeRepository(
    private val api: HomeApi
) : HomeRepositoryAbs {

    override suspend fun fetchAll() = flowOf(api.fetchAll())
}
