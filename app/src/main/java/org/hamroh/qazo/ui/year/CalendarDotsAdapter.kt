package org.hamroh.qazo.ui.year

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.hamroh.qazo.R
import org.hamroh.qazo.databinding.ItemCalendarDotsBinding
import org.hamroh.qazo.infra.utils.PrayType

class CalendarDotsAdapter : ListAdapter<String, CalendarDotsAdapter.ViewHolder>(DIFF_UTIL()) {

    private class DIFF_UTIL : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
            oldItem == newItem
    }

    class ViewHolder(
        private val binding: ItemCalendarDotsBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var data: String

        fun bind(newData: String) {
            this.data = newData

            binding.mvDot.setCardBackgroundColor(
                binding.root.context.getColor(
                    getTypeColor(data)
                )
            )
        }

        private fun getTypeColor(prayType: String): Int = when (prayType) {
            PrayType.ADO.toString() -> R.color.ado
            PrayType.PRAY.toString() -> R.color.pray
            PrayType.QAZO.toString() -> R.color.qazo
            else -> R.color.middle
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ) = ViewHolder(
        ItemCalendarDotsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) = holder.bind(getItem(position))
}

