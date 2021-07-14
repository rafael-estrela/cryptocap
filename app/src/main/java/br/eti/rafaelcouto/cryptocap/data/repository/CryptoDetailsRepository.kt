package br.eti.rafaelcouto.cryptocap.data.repository

import br.eti.rafaelcouto.cryptocap.data.api.CryptoDetailsApi
import br.eti.rafaelcouto.cryptocap.data.repository.abs.CryptoDetailsRepositoryAbs
import kotlinx.coroutines.flow.flow

class CryptoDetailsRepository(
    private val api: CryptoDetailsApi
) : CryptoDetailsRepositoryAbs {

    override fun fetchDetails(id: Long) = flow { emit(api.fetchInfo(id)) }
    override fun fetchQuotes(id: Long) = flow { emit(api.fetchLatestQuotes(id)) }
}
