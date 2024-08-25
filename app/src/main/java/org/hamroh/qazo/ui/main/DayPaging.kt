package org.hamroh.qazo.ui.main

import androidx.paging.PagingSource
import androidx.paging.PagingState
import org.hamroh.qazo.infra.utils.getDayList

class DayPaging(private val day: Long) : PagingSource<Int, String>() {
    override fun getRefreshKey(state: PagingState<Int, String>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, String> {
        val page = params.key ?: 0

        return try {
            val data = page.getDayList(day)

            LoadResult.Page(
                data = data,
                prevKey = page - 1,
                nextKey = if (data.isEmpty()) null else page + 1
            )

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}