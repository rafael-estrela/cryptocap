package br.eti.rafaelcouto.cryptocap.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.eti.rafaelcouto.cryptocap.R
import br.eti.rafaelcouto.cryptocap.application.network.model.Result
import br.eti.rafaelcouto.cryptocap.domain.usecase.abs.CryptoDetailsUseCaseAbs
import br.eti.rafaelcouto.cryptocap.testhelper.factory.DetailsFactory
import br.eti.rafaelcouto.cryptocap.testhelper.rule.CoroutinesTestRule
import com.google.common.truth.Truth.assertThat
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
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
class CryptoDetailsViewModelTest {

    @get:Rule val instantTaskExecutor = InstantTaskExecutorRule()
    @get:Rule @ExperimentalCoroutinesApi val coroutineRule = CoroutinesTestRule()

    @RelaxedMockK private lateinit var mockUseCase: CryptoDetailsUseCaseAbs

    private lateinit var sut: CryptoDetailsViewModel

    private val stateSequence = mutableListOf<Result.Status>()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        sut = CryptoDetailsViewModel(useCase = mockUseCase, dispatcher = Dispatchers.Main)

        sut.status.observeForever { stateSequence.add(it) }
        sut.errorMessage.observeForever {}
        sut.content.observeForever {}
        sut.variation.observeForever {}
        sut.variationColor.observeForever {}
        sut.isFavorite.observeForever {}

        stateSequence.clear()
    }

    @Test
    fun loadDataSuccessTest() = runBlocking {
        val expected = DetailsFactory.itemDetailsUi
        every { mockUseCase.fetchDetails(any()) }.returns(flowOf(Result.success(expected)))
        coEvery { mockUseCase.isFavorite(any()) }.returns(true)

        assertThat(sut.content.value).isNull()
        assertThat(sut.errorMessage.value).isNull()
        assertThat(sut.status.value).isNull()
        assertThat(sut.variation.value).isNull()
        assertThat(sut.variationColor.value).isNull()
        assertThat(sut.isFavorite.value).isNull()

        sut.loadData(1)

        verify { mockUseCase.fetchDetails(1) }
        coVerify { mockUseCase.isFavorite(1) }

        assertThat(sut.content.value).isEqualTo(expected)
        assertThat(sut.errorMessage.value).isNull()
        assertThat(sut.status.value).isNotNull()
        assertThat(sut.variation.value).isEqualTo(expected.dayVariation)
        assertThat(sut.variationColor.value).isEqualTo(R.color.green500)
        assertThat(sut.isFavorite.value).isTrue()
        assertThat(stateSequence).containsExactly(Result.Status.LOADING, Result.Status.SUCCESS).inOrder()
    }

    @Test
    fun loadDataErrorTest() = runBlocking {
        every { mockUseCase.fetchDetails(any()) }.returns(flowOf(Result.error("dummy error")))
        coEvery { mockUseCase.isFavorite(any()) }.returns(false)

        assertThat(sut.content.value).isNull()
        assertThat(sut.errorMessage.value).isNull()
        assertThat(sut.status.value).isNull()
        assertThat(sut.variation.value).isNull()
        assertThat(sut.variationColor.value).isNull()
        assertThat(sut.isFavorite.value).isNull()

        sut.loadData(1)

        verify { mockUseCase.fetchDetails(1) }
        coVerify { mockUseCase.isFavorite(1) }

        assertThat(sut.content.value).isNull()
        assertThat(sut.errorMessage.value).isEqualTo("dummy error")
        assertThat(sut.status.value).isNotNull()
        assertThat(sut.variation.value).isNull()
        assertThat(sut.variationColor.value).isNull()
        assertThat(sut.isFavorite.value).isFalse()
        assertThat(stateSequence).containsExactly(Result.Status.LOADING, Result.Status.ERROR).inOrder()
    }

    @Test
    fun updateSelectionSuccessTest() = runBlocking {
        val expected = DetailsFactory.itemDetailsUi
        every { mockUseCase.fetchDetails(any()) }.returns(flowOf(Result.success(expected)))
        coEvery { mockUseCase.isFavorite(any()) }.returns(true)

        sut.loadData(1)

        assertThat(sut.variation.value).isEqualTo(expected.dayVariation)
        assertThat(sut.variationColor.value).isEqualTo(R.color.green500)

        sut.updateSelection(R.id.rbWeek)

        assertThat(sut.variation.value).isEqualTo(expected.weekVariation)
        assertThat(sut.variationColor.value).isEqualTo(R.color.green500)

        sut.updateSelection(R.id.rbMonth)

        assertThat(sut.variation.value).isEqualTo(expected.monthVariation)
        assertThat(sut.variationColor.value).isEqualTo(R.color.red500)

        sut.updateSelection(R.id.rbDay)

        assertThat(sut.variation.value).isEqualTo(expected.dayVariation)
        assertThat(sut.variationColor.value).isEqualTo(R.color.green500)
    }

    @Test
    fun updateSelectionFallbackTest() = runBlocking {
        val variationSequence = mutableListOf<String>()
        sut.variation.observeForever { variationSequence.add(it) }

        val expected = DetailsFactory.itemDetailsUi
        every { mockUseCase.fetchDetails(any()) }.returns(flowOf(Result.success(expected)))
        coEvery { mockUseCase.isFavorite(any()) }.returns(true)

        assertThat(variationSequence).isEmpty()

        sut.loadData(1)

        assertThat(sut.variation.value).isEqualTo(expected.dayVariation)
        assertThat(variationSequence).hasSize(1)

        sut.updateSelection(123)

        assertThat(sut.variation.value).isEqualTo(expected.dayVariation)
        assertThat(variationSequence).hasSize(1)
    }

    @Test
    fun handleFavoriteSuccessTest() = runBlocking {
        every { mockUseCase.fetchDetails(any()) }.returns(flowOf(Result.success(DetailsFactory.itemDetailsUi)))
        coEvery { mockUseCase.isFavorite(any()) }.returns(true)

        sut.loadData(1)

        assertThat(sut.isFavorite.value).isTrue()

        sut.handleFavorite()

        coVerify { mockUseCase.removeFromFavorites(1) }
        assertThat(sut.isFavorite.value).isFalse()

        sut.handleFavorite()

        coVerify { mockUseCase.saveToFavorites(1) }
        assertThat(sut.isFavorite.value).isTrue()
    }

    @Test
    fun handleFavoriteErrorTest() = runBlocking {
        assertThat(sut.isFavorite.value).isNull()

        sut.handleFavorite()

        assertThat(sut.isFavorite.value).isNull()
        coVerify(exactly = 0) { mockUseCase.removeFromFavorites(any()) }
        coVerify(exactly = 0) { mockUseCase.saveToFavorites(any()) }
    }
}
