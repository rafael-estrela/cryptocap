package br.eti.rafaelcouto.cryptocap.data.model

import com.squareup.moshi.Json

data class CryptoItem(
    val id: Long,
    val name: String,
    val symbol: String,
    val quote: Quote
) {

    data class Quote(
        @field:Json(name = "USD") val usdQuote: QuoteItem
    )
}
