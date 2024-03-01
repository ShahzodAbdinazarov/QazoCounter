package org.hamroh.qazo.ui.calendar

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import org.hamroh.qazo.databinding.ItemCalendarBinding
import org.hamroh.qazo.infra.utils.MonthModel
import org.hamroh.qazo.infra.utils.getDate

class CalendarPagingAdapter(private var onItemClick: ((String, Int) -> Unit)? = null) : PagingDataAdapter<MonthModel, CalendarPagingAdapter.ViewHolder>(DIFF_UTIL) {

    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<MonthModel>() {
            override fun areItemsTheSame(oldItem: MonthModel, newItem: MonthModel): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: MonthModel, newItem: MonthModel): Boolean {
                return oldItem == newItem
            }

        }
    }


    class ViewHolder(private val binding: ItemCalendarBinding, private val onItemClick: ((String, Int) -> Unit)?, private val context: Context) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var month: MonthModel

        init {
//            binding.title.setOnClickListener { onItemClick?.invoke(prayTime, absoluteAdapterPosition) }
//
//            binding.tvFajr.setOnClickListener { onItemClick?.invoke("$prayTime${PrayTime.FAJR}", absoluteAdapterPosition) }
//            binding.tvDhuhr.setOnClickListener { onItemClick?.invoke("$prayTime${PrayTime.DHUHR}", absoluteAdapterPosition) }
//            binding.tvAsr.setOnClickListener { onItemClick?.invoke("$prayTime${PrayTime.ASR}", absoluteAdapterPosition) }
//            binding.tvMaghrib.setOnClickListener { onItemClick?.invoke("$prayTime${PrayTime.MAGHRIB}", absoluteAdapterPosition) }
//            binding.tvIsha.setOnClickListener { onItemClick?.invoke("$prayTime${PrayTime.ISHA}", absoluteAdapterPosition) }
        }

        @SuppressLint("ResourceAsColor", "SetTextI18n")
        fun bind(data: MonthModel) {
//
            month = data
//            binding.tvDate.text = prayTime.toLong().getDate("dd-MMMM, EEEE yyyy") + "-yil"
//            setUp(prayTime)
            binding.title.text = month.timeInMillis?.getDate("MMMM")
            setupDays(month.daysOfMonth ?: arrayListOf())
        }

        private fun setupDays(transactions: ArrayList<String>) {
            val transactionAdapter = MonthAdapter({ onItemClick?.invoke(it, absoluteAdapterPosition) })
            transactionAdapter.submitList(transactions)
            binding.rvMonth.apply {
                layoutManager = StaggeredGridLayoutManager(7, StaggeredGridLayoutManager.VERTICAL)
                adapter = transactionAdapter
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(ItemCalendarBinding.inflate(LayoutInflater.from(parent.context), parent, false), onItemClick, parent.context)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position)!!)
}

