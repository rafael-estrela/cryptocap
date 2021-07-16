package br.eti.rafaelcouto.cryptocap.router.abs

interface CryptoDetailsRouterAbs {

    fun goToCompareSelection()
    fun goToCompare(fromId: Long, toId: Long)
}
