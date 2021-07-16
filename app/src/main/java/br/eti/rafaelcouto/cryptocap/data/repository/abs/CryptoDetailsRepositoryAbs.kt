package br.eti.rafaelcouto.cryptocap.data.repository.abs

import br.eti.rafaelcouto.cryptocap.application.network.model.Result
import br.eti.rafaelcouto.cryptocap.data.model.CryptoDetails
import br.eti.rafaelcouto.cryptocap.data.model.Favorite
import br.eti.rafaelcouto.cryptocap.data.model.QuoteDetails
import kotlinx.coroutines.flow.Flow

interface CryptoDetailsRepositoryAbs {

    fun fetchDetails(id: Long): Flow<Result<CryptoDetails>>
    fun fetchQuotes(id: Long): Flow<Result<QuoteDetails>>
    suspend fun fetchFavorite(id: Long): Favorite?
    suspend fun addToFavorites(favorite: Favorite)
    suspend fun removeFromFavorites(favorite: Favorite)
}
