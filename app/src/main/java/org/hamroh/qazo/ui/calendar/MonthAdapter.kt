package org.hamroh.qazo.ui.calendar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.hamroh.qazo.databinding.ItemMonthBinding
import org.hamroh.qazo.infra.utils.getDate

class MonthAdapter(private var onItemClick: ((String) -> Unit)? = null, private var onTagClick: ((String) -> Unit)? = null) : ListAdapter<String, MonthAdapter.ViewHolder>(DIFF_UTIL()) {

    private class DIFF_UTIL : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem
        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem
    }

    class ViewHolder(private val binding: ItemMonthBinding, private val onItemClick: ((String) -> Unit)?, private val onTagClick: ((String) -> Unit)?) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var day: String

        init {
            binding.title.setOnClickListener { onItemClick?.invoke(day) }
        }

        fun bind(newDay: String) {
            this.day = newDay

            binding.title.text = day.toLong().getDate("dd")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(ItemMonthBinding.inflate(LayoutInflater.from(parent.context), parent, false), onItemClick, onTagClick)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))
}

