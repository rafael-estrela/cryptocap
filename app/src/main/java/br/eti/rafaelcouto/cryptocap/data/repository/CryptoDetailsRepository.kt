package br.eti.rafaelcouto.cryptocap.data.repository

import br.eti.rafaelcouto.cryptocap.data.api.CryptoDetailsApi
import br.eti.rafaelcouto.cryptocap.data.local.dao.FavoriteDao
import br.eti.rafaelcouto.cryptocap.data.model.Favorite
import br.eti.rafaelcouto.cryptocap.data.repository.abs.CryptoDetailsRepositoryAbs
import kotlinx.coroutines.flow.flow

class CryptoDetailsRepository(
    private val api: CryptoDetailsApi,
    private val dao: FavoriteDao
) : CryptoDetailsRepositoryAbs {

    override fun fetchDetails(id: Long) = flow { emit(api.fetchInfo(id)) }
    override fun fetchQuotes(id: Long) = flow { emit(api.fetchLatestQuotes(id)) }
    override suspend fun fetchFavorite(id: Long) = dao.findById(id)
    override suspend fun addToFavorites(favorite: Favorite) = dao.insert(favorite)
    override suspend fun removeFromFavorites(favorite: Favorite) = dao.delete(favorite)
}
