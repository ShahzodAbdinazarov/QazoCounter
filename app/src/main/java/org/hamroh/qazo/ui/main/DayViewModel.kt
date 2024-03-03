package org.hamroh.qazo.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.liveData
import org.hamroh.qazo.infra.utils.pageSize

class DayViewModel : ViewModel() {

    fun getList(day: Long) = Pager(PagingConfig(pageSize = pageSize)) { DayPaging(day) }.liveData.cachedIn(viewModelScope)

}