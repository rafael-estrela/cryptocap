package br.eti.rafaelcouto.cryptocap.domain.model

import br.eti.rafaelcouto.cryptocap.R

data class CryptoDetailsUI(
    val id: Long,
    val name: String,
    val description: String,
    val logoUrl: String,
    val dollarPrice: String,
    val dayVariation: String,
    val weekVariation: String,
    val monthVariation: String
) {
    enum class Variation(val id: Int) {
        DAY(R.id.rbDay), WEEK(R.id.rbWeek), MONTH(R.id.rbMonth)
    }
}
