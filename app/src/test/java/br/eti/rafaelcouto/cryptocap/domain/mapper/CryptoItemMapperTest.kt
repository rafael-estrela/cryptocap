package br.eti.rafaelcouto.cryptocap.domain.mapper

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.ItemSnapshotList
import androidx.paging.PagingData
import br.eti.rafaelcouto.cryptocap.data.model.CryptoItem
import br.eti.rafaelcouto.cryptocap.domain.mapper.abs.CryptoItemMapperAbs
import br.eti.rafaelcouto.cryptocap.domain.model.CryptoItemUI
import br.eti.rafaelcouto.cryptocap.testhelper.dummy.DummyDiffCallback
import br.eti.rafaelcouto.cryptocap.testhelper.dummy.DummyListUpdateCallback
import br.eti.rafaelcouto.cryptocap.testhelper.factory.HomeFactory
import br.eti.rafaelcouto.cryptocap.testhelper.rule.CoroutinesTestRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class CryptoItemMapperTest {

    @get:Rule val instantTaskExecutor = InstantTaskExecutorRule()
    @get:Rule @ExperimentalCoroutinesApi val coroutineRule = CoroutinesTestRule()

    private lateinit var sut: CryptoItemMapperAbs

    @Before
    fun setUp() {
        sut = CryptoItemMapper()
    }

    @Test
    fun mapErrorDataTest() = runBlocking {
        val input = PagingData.empty<CryptoItem>()
        val mappedPagingData = sut.map(input)

        val actual = getDifferSnapshot(mappedPagingData)

        assertThat(actual).isEmpty()
        assertThat(actual.items).isEmpty()
    }

    @Test
    fun mapSuccessDataTest() = runBlocking {
        val input = HomeFactory.cryptoList
        val mappedPagingData = sut.map(PagingData.from(input))
        val actual = getDifferSnapshot(mappedPagingData)

        assertThat(actual).isNotEmpty()
        assertThat(actual.items).isNotEmpty()

        actual.items.forEachIndexed { index, item ->
            assertThat(item.id).isEqualTo(input[index].id)
            assertThat(item.name).isEqualTo("Dummy Coin")
            assertThat(item.symbol).isEqualTo("DMC")
            assertThat(item.dollarPrice).isEqualTo("US$ 1,412.09")
            assertThat(item.recentVariation).isEqualTo("51.33%")
        }
    }

    private suspend fun getDifferSnapshot(
        data: PagingData<CryptoItemUI>
    ): ItemSnapshotList<CryptoItemUI> {
        val differ = AsyncPagingDataDiffer(
            diffCallback = DummyDiffCallback<CryptoItemUI>(),
            updateCallback = DummyListUpdateCallback(),
            workerDispatcher = Dispatchers.Main
        )

        differ.submitData(data)

        return differ.snapshot()
    }
}
