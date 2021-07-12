package br.eti.rafaelcouto.cryptocap.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import br.eti.rafaelcouto.cryptocap.domain.usecase.abs.HomeUseCaseAbs
import br.eti.rafaelcouto.cryptocap.testhelper.base.PagingDataTest
import br.eti.rafaelcouto.cryptocap.testhelper.factory.HomeFactory
import br.eti.rafaelcouto.cryptocap.testhelper.rule.CoroutinesTestRule
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class HomeViewModelTest : PagingDataTest() {

    @get:Rule val instantTaskExecutor = InstantTaskExecutorRule()
    @get:Rule @ExperimentalCoroutinesApi val coroutineRule = CoroutinesTestRule()

    @MockK private lateinit var mockUseCase: HomeUseCaseAbs

    private lateinit var sut: HomeViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        sut = HomeViewModel(useCase = mockUseCase).apply {
            data.observeForever { }
        }
    }

    @Test
    fun loadDataSuccessTest() = runBlocking {
        assertThat(sut.data.value).isNull()

        val expected = HomeFactory.cryptoListUI
        coEvery { mockUseCase.fetchAll() }.returns(flowOf(PagingData.from(expected)))

        sut.loadData()

        assertThat(sut.data.value).isNotNull()

        val actual = getDifferSnapshot(sut.data.value!!)

        assertThat(actual).isNotEmpty()
        assertThat(actual.items).isNotEmpty()
        assertThat(actual.items).isEqualTo(expected)
    }

    @Test
    fun loadDataFailureTest() = runBlocking {
        assertThat(sut.data.value).isNull()

        coEvery { mockUseCase.fetchAll() }.returns(flowOf(PagingData.empty()))

        sut.loadData()

        assertThat(sut.data.value).isNotNull()

        val actual = getDifferSnapshot(sut.data.value!!)
        assertThat(actual).isEmpty()
    }
}
