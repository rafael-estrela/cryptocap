package br.eti.rafaelcouto.cryptocap.data.api

import br.eti.rafaelcouto.cryptocap.application.network.model.Result
import br.eti.rafaelcouto.cryptocap.data.model.CryptoDetails
import br.eti.rafaelcouto.cryptocap.data.model.QuoteDetails
import retrofit2.http.GET
import retrofit2.http.Query

interface CryptoDetailsApi {

    @GET("info")
    suspend fun fetchInfo(
        @Query("id") id: Long
    ): Result<CryptoDetails>

    @GET("quotes/latest")
    suspend fun fetchLatestQuotes(
        @Query("id") id: Long
    ): Result<QuoteDetails>
}
