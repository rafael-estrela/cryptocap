package br.eti.rafaelcouto.cryptocap.domain.model

data class CryptoItemUI(
    val id: Long,
    val name: String,
    val symbol: String,
    val dollarPrice: String,
    val recentVariation: String
)
