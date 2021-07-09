package br.eti.rafaelcouto.cryptocap.data.model

import com.squareup.moshi.Json

data class QuoteItem(
    val price: Double,
    @field:Json(name = "percent_change_24h") val dayVariation: Double
)
