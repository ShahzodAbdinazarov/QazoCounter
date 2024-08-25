package org.hamroh.qazo.ui.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.hamroh.qazo.R
import org.hamroh.qazo.databinding.ItemMonthBinding
import org.hamroh.qazo.infra.utils.PrayTime
import org.hamroh.qazo.infra.utils.PrayType
import org.hamroh.qazo.infra.utils.SharedPrefs
import org.hamroh.qazo.infra.utils.dp
import org.hamroh.qazo.infra.utils.getToday
import org.hamroh.qazo.infra.utils.timeFormat

class MonthAdapter(
    private var onItemClick: ((Long) -> Unit)? = null,
) : ListAdapter<String, MonthAdapter.ViewHolder>(DIFF_UTIL()) {

    private class DIFF_UTIL : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(
            oldItem: String,
            newItem: String,
        ): Boolean = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: String,
            newItem: String,
        ): Boolean = oldItem == newItem
    }

    class ViewHolder(
        private val binding: ItemMonthBinding,
        private val onItemClick: ((Long) -> Unit)?,
    ) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var day: String
        private var bg: Int = R.drawable.gr_ado

        init {
            binding.tvDay.setOnClickListener { onItemClick?.invoke(day.toLong()) }
        }

        fun bind(newDay: String) {
            this.day = newDay

            binding.card.strokeColor = binding.root.context.getColor(
                if (day == getToday().toString()) R.color.ado
                else R.color.bg
            )
            binding.card.strokeWidth = if (day == getToday().toString()) 2.dp else 0.dp

            if (day.isNotEmpty()) binding.tvDay.text = day.toLong().timeFormat("dd")
            else binding.card.visibility = View.INVISIBLE
            if (day.isNotEmpty()) setUp(day)
        }

        private fun setUp(prayTime: String) {
            setGradient(SharedPrefs(binding.root.context).get("$prayTime${PrayTime.FAJR}", String::class.java), prayTime)
            setGradient(SharedPrefs(binding.root.context).get("$prayTime${PrayTime.DHUHR}", String::class.java), prayTime)
            setGradient(SharedPrefs(binding.root.context).get("$prayTime${PrayTime.ASR}", String::class.java), prayTime)
            setGradient(SharedPrefs(binding.root.context).get("$prayTime${PrayTime.MAGHRIB}", String::class.java), prayTime)
            setGradient(SharedPrefs(binding.root.context).get("$prayTime${PrayTime.ISHA}", String::class.java), prayTime)
        }

        private fun setGradient(prayType: String, prayTime: String) {
            val current = getGradient(prayType)
            bg = when (current) {
                R.drawable.gr_yet -> R.drawable.gr_yet
                R.drawable.gr_qazo -> when (bg) {
                    R.drawable.gr_yet -> R.drawable.gr_yet
                    else -> R.drawable.gr_qazo
                }

                R.drawable.gr_pray -> when (bg) {
                    R.drawable.gr_yet -> R.drawable.gr_yet
                    R.drawable.gr_qazo -> R.drawable.gr_qazo
                    else -> R.drawable.gr_pray
                }

                else -> when (bg) {
                    R.drawable.gr_yet -> R.drawable.gr_yet
                    R.drawable.gr_qazo -> R.drawable.gr_qazo
                    R.drawable.gr_pray -> R.drawable.gr_pray
                    else -> R.drawable.gr_ado
                }
            }
            if (prayTime.toLong() > getToday())
                binding.bgDay.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.middle))
            else binding.bgDay.setBackgroundResource(bg)
        }

        private fun getGradient(prayType: String): Int {
            return when (prayType) {
                PrayType.ADO.toString() -> R.drawable.gr_ado
                PrayType.PRAY.toString() -> R.drawable.gr_pray
                PrayType.QAZO.toString() -> R.drawable.gr_qazo
                else -> R.drawable.gr_yet
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(ItemMonthBinding.inflate(LayoutInflater.from(parent.context), parent, false), onItemClick)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))
}

