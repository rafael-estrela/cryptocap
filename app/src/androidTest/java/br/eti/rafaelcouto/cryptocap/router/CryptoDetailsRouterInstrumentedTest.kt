package br.eti.rafaelcouto.cryptocap.router

import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.testing.TestNavHostController
import androidx.test.annotation.UiThreadTest
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.eti.rafaelcouto.cryptocap.R
import br.eti.rafaelcouto.cryptocap.router.abs.CryptoDetailsRouterAbs
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class) @UiThreadTest
class CryptoDetailsRouterInstrumentedTest {

    private companion object {
        const val FROM_ID = 2L
        const val TO_ID = 3L
    }

    private lateinit var navController: NavController
    private lateinit var sut: CryptoDetailsRouterAbs

    @Before
    fun setUp() {
        navController = TestNavHostController(ApplicationProvider.getApplicationContext()).apply {
            setGraph(R.navigation.main_graph)
            setCurrentDestination(R.id.fragment_details, bundleOf("id" to FROM_ID))
        }

        sut = CryptoDetailsRouter(navController)
    }

    @Test
    fun validateInitialStateTest() {
        assertThat(navController.currentDestination).isNotNull()
        assertThat(navController.currentDestination?.id).isEqualTo(R.id.fragment_details)
        assertThat(navController.backStack.last.arguments?.containsKey("id")).isTrue()
        assertThat(navController.backStack.last.arguments?.get("id")).isEqualTo(FROM_ID)
    }

    @Test
    fun goToCompareSelectionTest() {
        sut.goToCompareSelection()

        assertThat(navController.currentDestination?.id).isEqualTo(R.id.fragment_compare_select)
        assertThat(navController.backStack.last.arguments?.containsKey("isComparing")).isTrue()
        assertThat(navController.backStack.last.arguments?.get("isComparing")).isEqualTo(true)
    }

    @Test
    fun goToCompareTest() {
        sut.goToCompare(FROM_ID, TO_ID)

        assertThat(navController.currentDestination?.id).isEqualTo(R.id.fragment_compare)
        assertThat(navController.backStack.last.arguments?.containsKey("fromId")).isTrue()
        assertThat(navController.backStack.last.arguments?.containsKey("toId")).isTrue()
        assertThat(navController.backStack.last.arguments?.get("fromId")).isEqualTo(FROM_ID)
        assertThat(navController.backStack.last.arguments?.get("toId")).isEqualTo(TO_ID)
    }
}
