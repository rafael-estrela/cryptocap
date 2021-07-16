package br.eti.rafaelcouto.cryptocap.router

import androidx.navigation.NavController
import br.eti.rafaelcouto.cryptocap.router.abs.CryptoDetailsRouterAbs
import br.eti.rafaelcouto.cryptocap.view.details.CryptoDetailsFragmentDirections

class CryptoDetailsRouter(
    private val navController: NavController
) : CryptoDetailsRouterAbs {

    override fun goToCompareSelection() {
        val directions = CryptoDetailsFragmentDirections.fragmentDetailsToFragmentCompareSelect(true)
        navController.navigate(directions)
    }

    override fun goToCompare(fromId: Long, toId: Long) {
        val directions = CryptoDetailsFragmentDirections.fragmentDetailsToFragmentCompare(fromId, toId)
        navController.navigate(directions)
    }
}
