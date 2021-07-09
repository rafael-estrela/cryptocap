package br.eti.rafaelcouto.cryptocap.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.eti.rafaelcouto.cryptocap.CoroutinesTestRule
import br.eti.rafaelcouto.cryptocap.application.network.model.Result
import br.eti.rafaelcouto.cryptocap.domain.usecase.abs.HomeUseCaseAbs
import br.eti.rafaelcouto.cryptocap.factory.HomeFactory
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
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
class HomeViewModelTest {

    @get:Rule val instantTaskExecutor = InstantTaskExecutorRule()
    @get:Rule @ExperimentalCoroutinesApi val coroutineRule = CoroutinesTestRule()

    @MockK private lateinit var mockUseCase: HomeUseCaseAbs

    private lateinit var sut: HomeViewModel

    private val statusSequence = mutableListOf<Result.Status>()
    private val errorMessageSequence = mutableListOf<String?>()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        sut = HomeViewModel(
            useCase = mockUseCase,
            dispatcher = Dispatchers.Main
        ).apply {
            status.observeForever { statusSequence.add(it) }
            errorMessage.observeForever { errorMessageSequence.add(it) }
            data.observeForever {  }
        }

        statusSequence.clear()
        errorMessageSequence.clear()
    }

    @Test
    fun loadDataSuccessTest() = runBlocking {
        assertThat(sut.data.value).isNull()
        assertThat(sut.status.value).isNull()
        assertThat(sut.errorMessage.value).isNull()

        val expected = HomeFactory.cryptoListUI
        coEvery { mockUseCase.fetchAll() }.returns(flowOf(Result.success(expected)))

        sut.loadData()

        assertThat(sut.data.value).isEqualTo(expected)
        assertThat(statusSequence).containsExactly(Result.Status.LOADING, Result.Status.SUCCESS).inOrder()
        assertThat(errorMessageSequence).containsExactly(null, null).inOrder()
    }

    @Test
    fun loadDataFailureTest() = runBlocking {
        assertThat(sut.data.value).isNull()
        assertThat(sut.status.value).isNull()
        assertThat(sut.errorMessage.value).isNull()

        val expected = "dummy message"
        coEvery { mockUseCase.fetchAll() }.returns(flowOf(Result.error(expected)))

        sut.loadData()

        assertThat(sut.data.value).isEmpty()
        assertThat(statusSequence).containsExactly(Result.Status.LOADING, Result.Status.ERROR).inOrder()
        assertThat(errorMessageSequence).containsExactly(null, expected).inOrder()
    }
}
