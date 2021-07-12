package br.eti.rafaelcouto.cryptocap.data.repository

import androidx.paging.Pager
import br.eti.rafaelcouto.cryptocap.data.model.CryptoItem
import br.eti.rafaelcouto.cryptocap.data.repository.abs.HomeRepositoryAbs

class HomeRepository(
    private val pager: Pager<Int, CryptoItem>
) : HomeRepositoryAbs {

    companion object {
        const val DEFAULT_LIST_SIZE = 15
    }

    override suspend fun fetchAll() = pager.flow
}
