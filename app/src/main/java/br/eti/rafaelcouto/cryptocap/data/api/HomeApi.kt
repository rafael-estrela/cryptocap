package br.eti.rafaelcouto.cryptocap.data.api

import br.eti.rafaelcouto.cryptocap.application.network.model.Result
import br.eti.rafaelcouto.cryptocap.data.model.CryptoItem
import retrofit2.http.GET

interface HomeApi {

    @GET("listings/latest")
    suspend fun fetchAll(): Result<List<CryptoItem>>
}
