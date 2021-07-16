package br.eti.rafaelcouto.cryptocap.testhelper.factory

import br.eti.rafaelcouto.cryptocap.data.model.CryptoDetails
import br.eti.rafaelcouto.cryptocap.data.model.QuoteDetails
import br.eti.rafaelcouto.cryptocap.domain.model.CryptoDetailsUI
import kotlin.random.Random

object DetailsFactory {

    val itemDetails get() = CryptoDetails(
        Random.nextLong(),
        "Bitcoin",
        "BTC",
        "Bitcoin (BTC) is a cryptocurrency . Users are able to generate BTC through the " +
            "process of mining. Bitcoin has a current supply of 18,754,631. The last known price " +
            "of Bitcoin is 32,906.01273372 USD and is down -3.03 over the last 24 hours. It is " +
            "currently trading on 9018 active market(s) with \$24,692,225,809.62 traded over the " +
            "last 24 hours. More information can be found at https://bitcoin.org/.",
        "https://s2.coinmarketcap.com/static/img/coins/64x64/1.png"
    )

    val itemQuotes get() = QuoteDetails(HomeFactory.quote)

    val itemDetailsUi get() = CryptoDetailsUI(
        Random.nextLong(),
        "Bitcoin (BTC)",
        "Bitcoin (BTC) is a cryptocurrency . Users are able to generate BTC through the " +
            "process of mining. Bitcoin has a current supply of 18,754,631. The last known price " +
            "of Bitcoin is 32,906.01273372 USD and is down -3.03 over the last 24 hours. It is " +
            "currently trading on 9018 active market(s) with \$24,692,225,809.62 traded over the " +
            "last 24 hours. More information can be found at https://bitcoin.org/.",
        "https://s2.coinmarketcap.com/static/img/coins/64x64/1.png",
        "US$ 1,412.09",
        "51.33%",
        "0.378978%",
        "-12.7%"
    )
}
