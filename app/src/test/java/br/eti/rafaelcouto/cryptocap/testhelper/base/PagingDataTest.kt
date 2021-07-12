package br.eti.rafaelcouto.cryptocap.testhelper.base

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.ItemSnapshotList
import androidx.paging.PagingData
import br.eti.rafaelcouto.cryptocap.testhelper.dummy.DummyDiffCallback
import br.eti.rafaelcouto.cryptocap.testhelper.dummy.DummyListUpdateCallback
import kotlinx.coroutines.Dispatchers

@Suppress("UnnecessaryAbstractClass")
abstract class PagingDataTest {

    protected suspend fun <T : Any> getDifferSnapshot(
        data: PagingData<T>
    ): ItemSnapshotList<T> {
        val differ = AsyncPagingDataDiffer(
            diffCallback = DummyDiffCallback<T>(),
            updateCallback = DummyListUpdateCallback(),
            workerDispatcher = Dispatchers.Main
        )

        differ.submitData(data)

        return differ.snapshot()
    }
}
