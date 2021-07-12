package br.eti.rafaelcouto.cryptocap.testhelper.factory

import br.eti.rafaelcouto.cryptocap.data.model.CryptoItem
import br.eti.rafaelcouto.cryptocap.data.model.QuoteItem
import br.eti.rafaelcouto.cryptocap.domain.model.CryptoItemUI
import kotlin.random.Random

object HomeFactory {

    val cryptoList get() = listOf(cryptoItem, cryptoItem, cryptoItem, cryptoItem, cryptoItem)

    val cryptoItem get() = CryptoItem(
        id = Random.nextLong(),
        name = "Dummy Coin",
        symbol = "DMC",
        quote = quote
    )

    val quote get() = CryptoItem.Quote(usdQuote = quoteItem)
    val quoteItem get() = QuoteItem(price = 1412.09, dayVariation = 51.33)

    val cryptoListUI get() = listOf(cryptoItemUI, cryptoItemUI, cryptoItemUI, cryptoItemUI, cryptoItemUI)

    val cryptoItemUI get() = CryptoItemUI(
        id = Random.nextLong(),
        name = "Dummy Coin",
        symbol = "DMC",
        dollarPrice = "US$ 1,412.09",
        recentVariation = "51.33%"
    )
}
