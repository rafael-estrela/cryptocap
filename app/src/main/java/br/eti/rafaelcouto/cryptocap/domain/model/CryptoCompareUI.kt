package br.eti.rafaelcouto.cryptocap.domain.model

data class CryptoCompareUI(
    val from: Element,
    val to: Element,
    val fromToRatio: Double
) {

    data class Element(
        val symbol: String,
        val logoUrl: String,
        val usdValue: Double
    )
}
