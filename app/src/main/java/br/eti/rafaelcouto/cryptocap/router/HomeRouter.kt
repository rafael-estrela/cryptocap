package br.eti.rafaelcouto.cryptocap.router

import androidx.navigation.NavController
import br.eti.rafaelcouto.cryptocap.router.abs.HomeRouterAbs
import br.eti.rafaelcouto.cryptocap.view.fragment.HomeFragmentDirections

class HomeRouter(
    private val navController: NavController
) : HomeRouterAbs {

    override fun goToDetails(id: Long) {
        val directions = HomeFragmentDirections.fragmentHomeToFragmentDetails(id)
        navController.navigate(directions)
    }

    override fun goBack() {
        navController.navigateUp()
    }
}
