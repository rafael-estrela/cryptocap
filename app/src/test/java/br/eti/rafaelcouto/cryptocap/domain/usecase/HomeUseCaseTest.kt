package br.eti.rafaelcouto.cryptocap.domain.usecase

import br.eti.rafaelcouto.cryptocap.application.network.model.Result
import br.eti.rafaelcouto.cryptocap.data.model.CryptoItem
import br.eti.rafaelcouto.cryptocap.data.repository.abs.HomeRepositoryAbs
import br.eti.rafaelcouto.cryptocap.domain.mapper.abs.CryptoItemMapperAbs
import br.eti.rafaelcouto.cryptocap.domain.model.CryptoItemUI
import br.eti.rafaelcouto.cryptocap.domain.usecase.abs.HomeUseCaseAbs
import br.eti.rafaelcouto.cryptocap.factory.HomeFactory
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class HomeUseCaseTest {

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
        val mapperInput = Result.success(HomeFactory.cryptoList)
        val expected = Result.success(HomeFactory.cryptoListUI)

        coEvery { mockRepository.fetchAll() }.returns(flowOf(mapperInput))
        every { mockMapper.map(any()) }.returns(expected)

        sut.fetchAll().collect { actual ->
            assertThat(actual.status).isEqualTo(Result.Status.SUCCESS)
            assertThat(actual.error).isNull()
            assertThat(actual.data).isNotNull()
            assertThat(actual.data).isEqualTo(expected.data)
        }

        verify { mockMapper.map(mapperInput) }
    }

    @Test
    fun fetchAllErrorTest() = runBlocking {
        val mapperInput = Result.error<List<CryptoItem>>("dummy error")
        val expected = Result.error<List<CryptoItemUI>>("dummy error")

        coEvery { mockRepository.fetchAll() }.returns(flowOf(mapperInput))
        every { mockMapper.map(any()) }.returns(expected)

        sut.fetchAll().collect { actual ->
            assertThat(actual.status).isEqualTo(Result.Status.ERROR)
            assertThat(actual.error).isEqualTo(expected.error)
            assertThat(actual.data).isNull()
        }

        verify { mockMapper.map(mapperInput) }
    }
}
