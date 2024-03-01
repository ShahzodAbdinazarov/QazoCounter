package org.hamroh.qazo.ui.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.liveData

class MonthViewModel : ViewModel() {

    fun getList(day: Long) = Pager(PagingConfig(pageSize = 12)) { MonthPaging(day) }.liveData.cachedIn(viewModelScope)

}