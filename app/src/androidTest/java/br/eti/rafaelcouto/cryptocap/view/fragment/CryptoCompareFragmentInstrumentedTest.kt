package br.eti.rafaelcouto.cryptocap.view.fragment

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.core.os.bundleOf
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.*
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import br.eti.rafaelcouto.cryptocap.R
import br.eti.rafaelcouto.cryptocap.application.network.model.Result
import br.eti.rafaelcouto.cryptocap.domain.model.CryptoCompareUI
import br.eti.rafaelcouto.cryptocap.testhelper.factory.CompareFactory
import br.eti.rafaelcouto.cryptocap.testhelper.rule.CoroutinesTestRule
import br.eti.rafaelcouto.cryptocap.viewmodel.CryptoCompareViewModel
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest

@RunWith(AndroidJUnit4::class)
class CryptoCompareFragmentInstrumentedTest : AutoCloseKoinTest() {

    private companion object {
        const val FROM_ID = 1L
        const val TO_ID = 2L
    }

    @get:Rule val instantTaskExecutor = InstantTaskExecutorRule()
    @get:Rule @ExperimentalCoroutinesApi val coroutineRule = CoroutinesTestRule()

    @RelaxedMockK private lateinit var mockViewModel: CryptoCompareViewModel

    @RelaxedMockK private lateinit var mockFromAmount: MutableLiveData<String>
    @MockK private lateinit var mockStatus: LiveData<Result.Status>
    @MockK private lateinit var mockErrorMessage: LiveData<String?>
    @MockK private lateinit var mockFrom: LiveData<CryptoCompareUI.Element?>
    @MockK private lateinit var mockTo: LiveData<CryptoCompareUI.Element?>
    @MockK private lateinit var mockConverted: MediatorLiveData<String>

    private lateinit var fromAmountObserver: Observer<String>
    private lateinit var statusObserver: Observer<Result.Status>
    private lateinit var errorMessageObserver: Observer<String?>
    private lateinit var fromObserver: Observer<CryptoCompareUI.Element?>
    private lateinit var toObserver: Observer<CryptoCompareUI.Element?>
    private lateinit var convertedObserver: Observer<String>

    private lateinit var scenario: FragmentScenario<CryptoCompareFragment>

    private val args = bundleOf("fromId" to FROM_ID, "toId" to TO_ID)

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        stopKoin()

        startKoin {
            modules(
                module {
                    viewModel { mockViewModel }
                }
            )
        }

        mockLiveData()

        scenario = launchFragmentInContainer(
            themeResId = R.style.Theme_CryptoCap,
            fragmentArgs = args
        )

        scenario.moveToState(Lifecycle.State.STARTED)
    }

    @Test
    fun validateInitialStateTest() {
        onView(withId(R.id.pbLoading)).check(matches(not(isDisplayed())))
        onView(withId(R.id.tvError)).check(matches(not(isDisplayed())))

        onView(withId(R.id.clFrom)).check(matches(isDisplayed()))
        onView(withId(R.id.clTo)).check(matches(isDisplayed()))
        onView(withId(R.id.tvEqual)).check(matches(isDisplayed()))
        onView(withId(R.id.btnSwap)).check(matches(isDisplayed()))

        onView(withId(R.id.etAmountFrom)).check(matches(withText("1")))
        onView(withId(R.id.tvCryptoFrom)).check(matches(withText("")))
        onView(withId(R.id.tvAmountTo)).check(matches(withText("2")))
        onView(withId(R.id.tvCryptoTo)).check(matches(withText("")))

        verify(exactly = 1) { mockViewModel.loadData(FROM_ID, TO_ID) }
    }

    @Test
    fun loadCryptosLoadingStateTest() {
        mockLoadindCase()

        onView(withId(R.id.pbLoading)).check(matches(isDisplayed()))

        onView(withId(R.id.tvError)).check(matches(not(isDisplayed())))

        onView(withId(R.id.clFrom)).check(matches(not(isDisplayed())))
        onView(withId(R.id.clTo)).check(matches(not(isDisplayed())))
        onView(withId(R.id.tvEqual)).check(matches(not(isDisplayed())))
        onView(withId(R.id.btnSwap)).check(matches(not(isDisplayed())))
    }

    @Test
    fun loadCryptosSuccessStateTest() {
        val from = CompareFactory.element
        val to = CompareFactory.element
        val converted = (from.usdValue + to.usdValue).toString()

        mockSuccessCase(from, to)

        onView(withId(R.id.pbLoading)).check(matches(not(isDisplayed())))

        onView(withId(R.id.tvError)).check(matches(not(isDisplayed())))

        onView(withId(R.id.clFrom)).check(matches(isDisplayed()))
        onView(withId(R.id.clTo)).check(matches(isDisplayed()))
        onView(withId(R.id.tvEqual)).check(matches(isDisplayed()))
        onView(withId(R.id.btnSwap)).check(matches(isDisplayed()))

        onView(withId(R.id.tvCryptoFrom)).check(matches(withText(from.symbol)))
        onView(withId(R.id.ivLogoFrom)).check(matches(withContentDescription("Logo da criptomoeda ${from.symbol}")))

        onView(withId(R.id.tvCryptoTo)).check(matches(withText(to.symbol)))
        onView(withId(R.id.ivLogoTo)).check(matches(withContentDescription("Logo da criptomoeda ${to.symbol}")))
        onView(withId(R.id.tvAmountTo)).check(matches(withText(converted)))
    }

    @Test
    fun loadCryptosErrorTest() {
        mockErrorCase()

        onView(withId(R.id.pbLoading)).check(matches(not(isDisplayed())))

        onView(withId(R.id.tvError))
            .check(matches(isDisplayed()))
            .check(matches(withText("dummy error")))

        onView(withId(R.id.clFrom)).check(matches(not(isDisplayed())))
        onView(withId(R.id.clTo)).check(matches(not(isDisplayed())))
        onView(withId(R.id.tvEqual)).check(matches(not(isDisplayed())))
        onView(withId(R.id.btnSwap)).check(matches(not(isDisplayed())))
    }

    @Test
    fun updateCryptosFieldSuccessTest() {
        mockSuccessCase(CompareFactory.element, CompareFactory.element)

        onView(withId(R.id.etAmountFrom))
            .perform(clearText())
            .perform(typeText("10"))

        verify(exactly = 1) { mockFromAmount.value = "1" }
        verify(exactly = 1) { mockFromAmount.value = "10" }
    }

    @Test
    fun swapCryptosSuccessTest() {
        val from = CompareFactory.element
        val to = CompareFactory.element
        val converted = (from.usdValue + to.usdValue).toString()

        mockSuccessCase(from = from, to = to)

        onView(withId(R.id.tvCryptoFrom)).check(matches(withText(from.symbol)))
        onView(withId(R.id.tvCryptoTo)).check(matches(withText(to.symbol)))

        onView(withId(R.id.btnSwap)).perform(click())

        mockFromAndTo(from = to, to = from)

        verify(exactly = 1) { mockViewModel.swap() }

        onView(withId(R.id.tvCryptoFrom)).check(matches(withText(to.symbol)))
        onView(withId(R.id.ivLogoFrom)).check(matches(withContentDescription("Logo da criptomoeda ${to.symbol}")))

        onView(withId(R.id.tvCryptoTo)).check(matches(withText(from.symbol)))
        onView(withId(R.id.ivLogoTo)).check(matches(withContentDescription("Logo da criptomoeda ${from.symbol}")))
        onView(withId(R.id.tvAmountTo)).check(matches(withText(converted)))
    }

    private fun mockLiveData() {
        every { mockViewModel.fromAmount }.returns(mockFromAmount)
        every { mockViewModel.status }.returns(mockStatus)
        every { mockViewModel.errorMessage }.returns(mockErrorMessage)
        every { mockViewModel.from }.returns(mockFrom)
        every { mockViewModel.to }.returns(mockTo)
        every { mockViewModel.converted }.returns(mockConverted)

        every { mockFromAmount.observe(any(), any()) }.answers {
            fromAmountObserver = args.last() as Observer<String>
        }

        every { mockStatus.observe(any(), any()) }.answers {
            statusObserver = args.last() as Observer<Result.Status>
        }

        every { mockErrorMessage.observe(any(), any()) }.answers {
            errorMessageObserver = args.last() as Observer<String?>
        }

        every { mockFrom.observe(any(), any()) }.answers {
            fromObserver = args.last() as Observer<CryptoCompareUI.Element?>
        }

        every { mockTo.observe(any(), any()) }.answers {
            toObserver = args.last() as Observer<CryptoCompareUI.Element?>
        }

        every { mockConverted.observe(any(), any()) }.answers {
            convertedObserver = args.last() as Observer<String>
        }

        every { mockFromAmount.value }.returns("1")
        every { mockStatus.value }.returns(Result.Status.LOADING)
        every { mockErrorMessage.value }.returns(null)
        every { mockFrom.value }.returns(null)
        every { mockTo.value }.returns(null)
        every { mockConverted.value }.returns("2")
    }

    private fun mockLoadindCase() = runOnUiThread {
        every { mockStatus.value }.returns(Result.Status.LOADING)
        statusObserver.onChanged(Result.Status.LOADING)
    }

    private fun mockErrorCase() = runOnUiThread {
        every { mockStatus.value }.returns(Result.Status.ERROR)
        every { mockErrorMessage.value }.returns("dummy error")

        statusObserver.onChanged(Result.Status.ERROR)
        errorMessageObserver.onChanged("dummy error")
    }

    private fun mockSuccessCase(from: CryptoCompareUI.Element, to: CryptoCompareUI.Element) = runOnUiThread {
        every { mockStatus.value }.returns(Result.Status.SUCCESS)
        statusObserver.onChanged(Result.Status.SUCCESS)

        mockFromAndTo(from, to)
    }

    private fun mockFromAndTo(from: CryptoCompareUI.Element, to: CryptoCompareUI.Element) = runOnUiThread {
        val converted = (from.usdValue + to.usdValue).toString()

        every { mockFrom.value }.returns(from)
        every { mockTo.value }.returns(to)
        every { mockConverted.value }.returns(converted)

        fromObserver.onChanged(from)
        toObserver.onChanged(to)
        convertedObserver.onChanged(converted)
    }
}
