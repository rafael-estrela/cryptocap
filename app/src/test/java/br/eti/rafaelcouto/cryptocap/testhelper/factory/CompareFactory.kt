package br.eti.rafaelcouto.cryptocap.testhelper.factory

import br.eti.rafaelcouto.cryptocap.domain.model.CryptoCompareUI
import kotlin.random.Random

object CompareFactory {

    val element get() = CryptoCompareUI.Element(
        Random.nextInt().toString(),
        "http://",
        1412.09
    )

    val biggerFrom get() = element(4.0) to element(2.0)
    val biggerTo get() = element(2.0) to element(4.0)
    val equalFromTo get() = element(1.0) to element(1.0)

    val increasing get() = CryptoCompareUI(element, element, 2.0)
    val decreasing get() = CryptoCompareUI(element, element, 0.5)

    fun fromElements(from: CryptoCompareUI.Element, to: CryptoCompareUI.Element) = CryptoCompareUI(
        from, to, 1.0
    )
    private fun element(usdValue: Double) = CryptoCompareUI.Element(
        Random.nextInt().toString(),
        "http://",
        usdValue
    )
}
