package br.eti.rafaelcouto.cryptocap.data.api

import br.eti.rafaelcouto.cryptocap.application.network.model.Result
import br.eti.rafaelcouto.cryptocap.data.model.CryptoItem
import retrofit2.http.GET
import retrofit2.http.Query

interface HomeApi {

    @GET("listings/latest")
    suspend fun fetchAll(
        @Query("start") start: Int,
        @Query("limit") limit: Int
    ): Result<List<CryptoItem>>
}
