package br.eti.rafaelcouto.cryptocap.application.network.model

import com.squareup.moshi.Json

data class Status(
    @field:Json(name = "error_message") val errorMessage: String?
)
