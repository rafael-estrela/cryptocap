package br.eti.rafaelcouto.cryptocap.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.eti.rafaelcouto.cryptocap.application.network.model.Result
import br.eti.rafaelcouto.cryptocap.domain.usecase.abs.CryptoCompareUseCaseAbs
import br.eti.rafaelcouto.cryptocap.testhelper.factory.CompareFactory
import br.eti.rafaelcouto.cryptocap.testhelper.rule.CoroutinesTestRule
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class CryptoCompareViewModelTest {

    @get:Rule val instantTaskExecutor = InstantTaskExecutorRule()
    @get:Rule @ExperimentalCoroutinesApi val coroutineRule = CoroutinesTestRule()

    @MockK private lateinit var mockUseCase: CryptoCompareUseCaseAbs

    private lateinit var sut: CryptoCompareViewModel

    private val stateSequence = mutableListOf<Result.Status>()

    private companion object {
        const val FROM_ID = 1L
        const val TO_ID = 2L
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        sut = CryptoCompareViewModel(useCase = mockUseCase, dispatcher = Dispatchers.Main)

        sut.status.observeForever { stateSequence.add(it) }
        sut.fromAmount.observeForever {}
        sut.errorMessage.observeForever {}
        sut.from.observeForever {}
        sut.to.observeForever {}
        sut.converted.observeForever {}

        stateSequence.clear()
    }

    @Test
    fun loadDataSuccessTest() = runBlocking {
        assertThat(sut.status.value).isNull()
        assertThat(sut.fromAmount.value).isEqualTo("1")
        assertThat(sut.from.value).isNull()
        assertThat(sut.errorMessage.value).isNull()
        assertThat(sut.to.value).isNull()
        assertThat(sut.converted.value).isNull()

        val expected = CompareFactory.increasing

        every { mockUseCase.fetchInfo(any(), any()) }.returns(flowOf(Result.success(expected)))

        sut.loadData(FROM_ID, TO_ID)

        verify { mockUseCase.fetchInfo(FROM_ID, TO_ID) }

        assertThat(sut.status.value).isNotNull()
        assertThat(sut.fromAmount.value).isEqualTo("1")
        assertThat(sut.from.value).isEqualTo(expected.from)
        assertThat(sut.errorMessage.value).isNull()
        assertThat(sut.to.value).isEqualTo(expected.to)
        assertThat(sut.converted.value).isNotNull()

        assertThat(stateSequence).containsExactly(Result.Status.LOADING, Result.Status.SUCCESS).inOrder()
    }

    @Test
    fun loadDataErrorTest() = runBlocking {
        assertThat(sut.status.value).isNull()
        assertThat(sut.fromAmount.value).isEqualTo("1")
        assertThat(sut.from.value).isNull()
        assertThat(sut.errorMessage.value).isNull()
        assertThat(sut.to.value).isNull()
        assertThat(sut.converted.value).isNull()

        every { mockUseCase.fetchInfo(any(), any()) }.returns(flowOf(Result.error("dummy error")))

        sut.loadData(FROM_ID, TO_ID)

        verify { mockUseCase.fetchInfo(FROM_ID, TO_ID) }

        assertThat(sut.status.value).isNotNull()
        assertThat(sut.fromAmount.value).isEqualTo("1")
        assertThat(sut.from.value).isNull()
        assertThat(sut.errorMessage.value).isEqualTo("dummy error")
        assertThat(sut.to.value).isNull()
        assertThat(sut.converted.value).isNull()

        assertThat(stateSequence).containsExactly(Result.Status.LOADING, Result.Status.ERROR).inOrder()
    }

    @Test
    fun increasingConvertedValueTest() = runBlocking {
        assertThat(sut.fromAmount.value).isEqualTo("1")
        assertThat(sut.converted.value).isNull()

        every { mockUseCase.fetchInfo(any(), any()) }
            .returns(flowOf(Result.success(CompareFactory.increasing)))

        sut.loadData(FROM_ID, TO_ID)

        assertThat(sut.fromAmount.value).isEqualTo("1")
        assertThat(sut.converted.value).isEqualTo("2.00")
    }

    @Test
    fun decreasingConvertedValueTest() = runBlocking {
        assertThat(sut.fromAmount.value).isEqualTo("1")
        assertThat(sut.converted.value).isNull()

        every { mockUseCase.fetchInfo(any(), any()) }
            .returns(flowOf(Result.success(CompareFactory.decreasing)))

        sut.loadData(FROM_ID, TO_ID)

        assertThat(sut.fromAmount.value).isEqualTo("1")
        assertThat(sut.converted.value).isEqualTo("0.50")
    }

    @Test
    fun swapSuccessTest() = runBlocking {
        val expected = CompareFactory.increasing

        every { mockUseCase.fetchInfo(any(), any()) }
            .returns(flowOf(Result.success(expected)))

        sut.loadData(FROM_ID, TO_ID)

        every { mockUseCase.swap(any()) }.returns(expected)

        sut.swap()

        verify { mockUseCase.swap(expected) }
    }

    @Test
    fun swapErrorTest() = runBlocking {
        sut.swap()

        verify(exactly = 0) { mockUseCase.swap(any()) }
    }

    @Test
    fun editAmountSuccessTest() = runBlocking {
        assertThat(sut.converted.value).isNull()

        val expected = CompareFactory.increasing

        every { mockUseCase.fetchInfo(any(), any()) }
            .returns(flowOf(Result.success(expected)))

        sut.loadData(FROM_ID, TO_ID)

        assertThat(sut.converted.value).isEqualTo("2.00")

        sut.fromAmount.value = "2"

        assertThat(sut.converted.value).isEqualTo("4.00")
    }

    @Test
    fun editAmountErrorTest() = runBlocking {
        assertThat(sut.converted.value).isNull()

        sut.fromAmount.value = "2"

        assertThat(sut.converted.value).isNull()
    }
}
