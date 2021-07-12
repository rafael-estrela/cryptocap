package br.eti.rafaelcouto.cryptocap.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import br.eti.rafaelcouto.cryptocap.data.model.CryptoItem
import br.eti.rafaelcouto.cryptocap.data.repository.abs.HomeRepositoryAbs
import br.eti.rafaelcouto.cryptocap.domain.mapper.abs.CryptoItemMapperAbs
import br.eti.rafaelcouto.cryptocap.domain.model.CryptoItemUI
import br.eti.rafaelcouto.cryptocap.domain.usecase.abs.HomeUseCaseAbs
import br.eti.rafaelcouto.cryptocap.testhelper.base.PagingDataTest
import br.eti.rafaelcouto.cryptocap.testhelper.factory.HomeFactory
import br.eti.rafaelcouto.cryptocap.testhelper.rule.CoroutinesTestRule
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class HomeUseCaseTest : PagingDataTest() {

    @get:Rule val instantTaskExecutor = InstantTaskExecutorRule()
    @get:Rule @ExperimentalCoroutinesApi val coroutineRule = CoroutinesTestRule()

    @MockK private lateinit var mockRepository: HomeRepositoryAbs
    @MockK private lateinit var mockMapper: CryptoItemMapperAbs

    private lateinit var sut: HomeUseCaseAbs

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        sut = HomeUseCase(
            repository = mockRepository,
            cryptoItemMapper = mockMapper
        )
    }

    @Test
    fun fetchAllSuccessTest() = runBlocking {
        val mapperInput = PagingData.from(HomeFactory.cryptoList)

        val expected = HomeFactory.cryptoListUI

        coEvery { mockRepository.fetchAll() }.returns(flowOf(mapperInput))
        every { mockMapper.map(any()) }.returns(PagingData.from(expected))

        sut.fetchAll().collect {
            val actual = getDifferSnapshot(it)

            assertThat(actual).isNotEmpty()
            assertThat(actual.items).isNotEmpty()
            assertThat(actual.items).isEqualTo(expected)
        }

        verify { mockMapper.map(mapperInput) }
    }

    @Test
    fun fetchAllErrorTest() = runBlocking {
        val mapperInput = PagingData.empty<CryptoItem>()
        val expected = PagingData.empty<CryptoItemUI>()

        coEvery { mockRepository.fetchAll() }.returns(flowOf(mapperInput))
        every { mockMapper.map(any()) }.returns(expected)

        sut.fetchAll().collect {
            val actual = getDifferSnapshot(it)

            assertThat(actual).isEmpty()
        }

        verify { mockMapper.map(mapperInput) }
    }
}
