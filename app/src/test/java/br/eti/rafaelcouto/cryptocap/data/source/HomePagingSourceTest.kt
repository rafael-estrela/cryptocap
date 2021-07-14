package br.eti.rafaelcouto.cryptocap.data.source

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import br.eti.rafaelcouto.cryptocap.application.network.model.Result
import br.eti.rafaelcouto.cryptocap.data.api.HomeApi
import br.eti.rafaelcouto.cryptocap.data.model.CryptoItem
import br.eti.rafaelcouto.cryptocap.data.repository.HomeRepository
import br.eti.rafaelcouto.cryptocap.testhelper.factory.HomeFactory
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class HomePagingSourceTest {

    @MockK private lateinit var mockApi: HomeApi

    private lateinit var sut: PagingSource<Int, CryptoItem>

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        sut = HomePagingSource(api = mockApi)
    }

    @Test
    fun loadSuccessTest() = runBlocking {
        val expected = HomeFactory.cryptoList
        coEvery { mockApi.fetchAll(any(), any()) }.returns(Result.success(expected))

        val actual = sut.load(PagingSource.LoadParams.Refresh(null, HomeRepository.DEFAULT_LIST_SIZE, false))

        coVerify { mockApi.fetchAll(HomePagingSource.INITIAL_START, HomeRepository.DEFAULT_LIST_SIZE) }
        assertThat(actual).isInstanceOf(PagingSource.LoadResult.Page::class.java)

        (actual as PagingSource.LoadResult.Page).let {
            assertThat(it.prevKey).isNull()
            assertThat(it.nextKey).isEqualTo(HomeRepository.DEFAULT_LIST_SIZE)
            assertThat(it.data).isEqualTo(expected)
        }
    }

    @Test
    fun loadErrorTest() = runBlocking {
        coEvery { mockApi.fetchAll(any(), any()) }.returns(Result.error("dummy error"))

        val actual = sut.load(PagingSource.LoadParams.Refresh(null, HomeRepository.DEFAULT_LIST_SIZE, false))

        coVerify { mockApi.fetchAll(HomePagingSource.INITIAL_START, HomeRepository.DEFAULT_LIST_SIZE) }
        assertThat(actual).isInstanceOf(PagingSource.LoadResult.Error::class.java)

        (actual as PagingSource.LoadResult.Error).let {
            assertThat(it.throwable.localizedMessage).isEqualTo("dummy error")
        }
    }

    @Test
    fun paginateSuccessTest() = runBlocking {
        val start = HomeRepository.DEFAULT_LIST_SIZE + HomePagingSource.INITIAL_START
        val limit = HomeRepository.DEFAULT_LIST_SIZE + HomeRepository.DEFAULT_LIST_SIZE
        val expected = HomeFactory.cryptoList

        coEvery { mockApi.fetchAll(any(), any()) }.returns(Result.success(expected))

        val actual = sut.load(
            PagingSource.LoadParams.Refresh(
                HomeRepository.DEFAULT_LIST_SIZE,
                HomeRepository.DEFAULT_LIST_SIZE,
                false
            )
        )

        coVerify { mockApi.fetchAll(start, limit) }
        assertThat(actual).isInstanceOf(PagingSource.LoadResult.Page::class.java)

        (actual as PagingSource.LoadResult.Page).let {
            assertThat(it.prevKey).isEqualTo(start)
            assertThat(it.nextKey).isEqualTo(limit)
            assertThat(it.data).isEqualTo(expected)
        }
    }

    @Test
    fun paginateErrorTest() = runBlocking {
        val start = HomeRepository.DEFAULT_LIST_SIZE + HomePagingSource.INITIAL_START
        val limit = HomeRepository.DEFAULT_LIST_SIZE + HomeRepository.DEFAULT_LIST_SIZE

        coEvery { mockApi.fetchAll(any(), any()) }.returns(Result.error("dummy error"))

        val actual = sut.load(
            PagingSource.LoadParams.Refresh(
                HomeRepository.DEFAULT_LIST_SIZE,
                HomeRepository.DEFAULT_LIST_SIZE,
                false
            )
        )

        coVerify { mockApi.fetchAll(start, limit) }
        assertThat(actual).isInstanceOf(PagingSource.LoadResult.Error::class.java)

        (actual as PagingSource.LoadResult.Error).let {
            assertThat(it.throwable.localizedMessage).isEqualTo("dummy error")
        }
    }

    @Test
    fun getRefreshKeySuccessTest() = runBlocking {
        val prevKey = HomeRepository.DEFAULT_LIST_SIZE + HomePagingSource.INITIAL_START
        val nextKey = HomeRepository.DEFAULT_LIST_SIZE + HomeRepository.DEFAULT_LIST_SIZE

        val actual = sut.getRefreshKey(
            PagingState(
                listOf(PagingSource.LoadResult.Page(HomeFactory.cryptoList, prevKey, nextKey)),
                0,
                PagingConfig(pageSize = HomeRepository.DEFAULT_LIST_SIZE),
                0
            )
        )

        assertThat(actual).isEqualTo(prevKey)
    }

    @Test
    fun getNullRefreshKeyByAnchorTest() = runBlocking {
        val actual = sut.getRefreshKey(
            PagingState(
                emptyList(),
                null,
                PagingConfig(pageSize = HomeRepository.DEFAULT_LIST_SIZE),
                0
            )
        )

        assertThat(actual).isNull()
    }

    @Test
    fun getNullRefreshKeyByClosestTest() = runBlocking {
        val actual = sut.getRefreshKey(
            PagingState(
                emptyList(),
                0,
                PagingConfig(pageSize = HomeRepository.DEFAULT_LIST_SIZE),
                0
            )
        )

        assertThat(actual).isNull()
    }
}
