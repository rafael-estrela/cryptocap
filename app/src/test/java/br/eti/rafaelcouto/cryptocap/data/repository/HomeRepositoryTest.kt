package br.eti.rafaelcouto.cryptocap.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.Pager
import androidx.paging.PagingData
import br.eti.rafaelcouto.cryptocap.data.model.CryptoItem
import br.eti.rafaelcouto.cryptocap.data.repository.abs.HomeRepositoryAbs
import br.eti.rafaelcouto.cryptocap.testhelper.base.PagingDataTest
import br.eti.rafaelcouto.cryptocap.testhelper.factory.HomeFactory
import br.eti.rafaelcouto.cryptocap.testhelper.rule.CoroutinesTestRule
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class HomeRepositoryTest : PagingDataTest() {

    @get:Rule val instantTaskExecutor = InstantTaskExecutorRule()
    @get:Rule @ExperimentalCoroutinesApi val coroutineRule = CoroutinesTestRule()

    @MockK private lateinit var mockPager: Pager<Int, CryptoItem>

    private lateinit var sut: HomeRepositoryAbs

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        sut = HomeRepository(pager = mockPager)
    }

    @Test
    fun fetchAllSuccessTest() = runBlocking {
        val expected = HomeFactory.cryptoList
        every { mockPager.flow }.returns(flowOf(PagingData.from(expected)))

        sut.fetchAll().collect {
            val actual = getDifferSnapshot(it)

            assertThat(actual).isNotEmpty()
            assertThat(actual.items).isNotEmpty()
            assertThat(actual.items).isEqualTo(expected)
        }
    }

    @Test
    fun fetchAllErrorTest() = runBlocking {
        every { mockPager.flow }.returns(flowOf(PagingData.empty()))

        val data = sut.fetchAll().singleOrNull()
        val actual = getDifferSnapshot(data!!)

        assertThat(actual).isEmpty()
    }
}
