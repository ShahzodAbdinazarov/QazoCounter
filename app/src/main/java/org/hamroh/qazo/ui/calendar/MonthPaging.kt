package org.hamroh.qazo.ui.calendar

import androidx.paging.PagingSource
import androidx.paging.PagingState
import org.hamroh.qazo.infra.utils.Month
import org.hamroh.qazo.infra.utils.getMonthList

class MonthPaging(private val day: Long) : PagingSource<Int, Month>() {
    override fun getRefreshKey(state: PagingState<Int, Month>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Month> {
        val page = params.key ?: 0

        return try {
            val data = page.getMonthList(day)

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