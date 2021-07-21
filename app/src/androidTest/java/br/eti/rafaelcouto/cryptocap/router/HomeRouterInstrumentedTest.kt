package br.eti.rafaelcouto.cryptocap.router

import androidx.navigation.NavController
import androidx.navigation.testing.TestNavHostController
import androidx.test.annotation.UiThreadTest
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.eti.rafaelcouto.cryptocap.R
import br.eti.rafaelcouto.cryptocap.router.abs.HomeRouterAbs
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class) @UiThreadTest
class HomeRouterInstrumentedTest {

    private companion object {
        const val ID = 1L
    }

    private lateinit var navController: NavController
    private lateinit var sut: HomeRouterAbs

    @Before
    fun setUp() {
        navController = TestNavHostController(ApplicationProvider.getApplicationContext()).apply {
            setGraph(R.navigation.main_graph)
            setCurrentDestination(R.id.fragment_home)
        }

        sut = HomeRouter(navController)
    }

    @Test
    fun validateInitialStateTest() {
        assertThat(navController.currentDestination).isNotNull()
        assertThat(navController.currentDestination?.id).isEqualTo(R.id.fragment_home)
        assertThat(navController.currentDestination?.arguments).containsKey("isComparing")
        assertThat(navController.currentDestination?.arguments?.get("isComparing")?.isDefaultValuePresent).isTrue()
        assertThat(navController.currentDestination?.arguments?.get("isComparing")?.defaultValue).isEqualTo(false)

        assertThat(navController.backStack.last.arguments?.containsKey("isComparing")).isTrue()
        assertThat(navController.backStack.last.arguments?.get("isComparing")).isEqualTo(false)
    }

    @Test
    fun goToDetailsTest() {
        sut.goToDetails(ID)

        assertThat(navController.currentDestination?.id).isEqualTo(R.id.fragment_details)
        assertThat(navController.backStack.last.arguments?.containsKey("id")).isTrue()
        assertThat(navController.backStack.last.arguments?.get("id")).isEqualTo(ID)
    }

    @Test
    fun goBackTest() {
        sut.goToDetails(ID)

        assertThat(navController.currentDestination?.id).isNotEqualTo(R.id.fragment_home)

        sut.goBack()

        assertThat(navController.currentDestination?.id).isEqualTo(R.id.fragment_home)
    }
}
