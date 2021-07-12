package br.eti.rafaelcouto.cryptocap.data.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import br.eti.rafaelcouto.cryptocap.application.network.model.Result
import br.eti.rafaelcouto.cryptocap.data.api.HomeApi
import br.eti.rafaelcouto.cryptocap.data.model.CryptoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HomePagingSource(
    private val api: HomeApi
) : PagingSource<Int, CryptoItem>() {

    companion object {
        const val INITIAL_START = 1
    }

    override fun getRefreshKey(state: PagingState<Int, CryptoItem>) = state.anchorPosition?.let {
        state.closestPageToPosition(it)?.prevKey
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CryptoItem> {
        val start = params.key?.let { it + INITIAL_START } ?: INITIAL_START
        val limit = params.key?.let { it + params.loadSize } ?: params.loadSize

        val data = withContext(Dispatchers.IO) { api.fetchAll(start, limit) }

        return if (data.status == Result.Status.ERROR) {
            LoadResult.Error(Exception(data.error.orEmpty()))
        } else {
            LoadResult.Page(
                data.data.orEmpty(),
                prevKey = if (start == INITIAL_START) null else start,
                nextKey = limit
            )
        }
    }
}
