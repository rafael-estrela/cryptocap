package br.eti.rafaelcouto.cryptocap.data.model

data class CryptoDetails(
    val id: Long,
    val name: String,
    val symbol: String,
    val description: String,
    val logo: String
)
