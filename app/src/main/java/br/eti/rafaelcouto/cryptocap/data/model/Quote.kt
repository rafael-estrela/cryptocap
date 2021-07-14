package br.eti.rafaelcouto.cryptocap.data.model

import com.squareup.moshi.Json

data class Quote(
    @field:Json(name = "USD") val usdQuote: Item
) {

    data class Item(
        val price: Double,
        @field:Json(name = "percent_change_24h") val dayVariation: Double,
        @field:Json(name = "percent_change_7d") val weekVariation: Double,
        @field:Json(name = "percent_change_30d") val monthVariation: Double
    )
}
