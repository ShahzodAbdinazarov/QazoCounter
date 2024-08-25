package org.hamroh.qazo.ui.calendar

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import org.hamroh.qazo.databinding.ItemCalendarBinding
import org.hamroh.qazo.infra.utils.Month
import org.hamroh.qazo.infra.utils.getDayOfWeek
import org.hamroh.qazo.infra.utils.timeFormat

class CalendarPagingAdapter(private var onItemClick: ((Long, Int) -> Unit)? = null) : PagingDataAdapter<Month, CalendarPagingAdapter.ViewHolder>(DIFF_UTIL) {

    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<Month>() {
            override fun areItemsTheSame(oldItem: Month, newItem: Month): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Month, newItem: Month): Boolean {
                return oldItem == newItem
            }

        }
    }


    class ViewHolder(
        private val binding: ItemCalendarBinding,
        private val onItemClick: ((Long, Int) -> Unit)?,
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var month: Month

        @SuppressLint("ResourceAsColor", "SetTextI18n")
        fun bind(data: Month) {
            month = data

            binding.tvMonth.text = month.millisStartOfMonth?.timeFormat("MMMM yyyy")
            val list = month.millisStartOfDayInMonth ?: arrayListOf()
            if (list.size > 0) repeat(list[0].getDayOfWeek()) { list.add(0, "") }
            setupDays(month.millisStartOfDayInMonth ?: arrayListOf())
        }

        private fun setupDays(transactions: ArrayList<String>) {
            val transactionAdapter = MonthAdapter { onItemClick?.invoke(it, absoluteAdapterPosition) }
            transactionAdapter.submitList(transactions)
            binding.rvMonth.apply {
                layoutManager = StaggeredGridLayoutManager(7, StaggeredGridLayoutManager.VERTICAL)
                adapter = transactionAdapter
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(ItemCalendarBinding.inflate(LayoutInflater.from(parent.context), parent, false), onItemClick)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position)!!)
}

