package org.hamroh.qazo.ui.calendar

import androidx.paging.PagingSource
import androidx.paging.PagingState
import org.hamroh.qazo.infra.utils.MonthModel
import org.hamroh.qazo.infra.utils.getMonthList

class MonthPaging(private val day: Long) : PagingSource<Int, MonthModel>() {
    override fun getRefreshKey(state: PagingState<Int, MonthModel>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MonthModel> {
        val page = params.key ?: 1

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