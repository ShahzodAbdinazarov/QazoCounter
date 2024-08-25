package org.hamroh.qazo.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.hamroh.qazo.R
import org.hamroh.qazo.databinding.ItemDayBinding
import org.hamroh.qazo.infra.utils.PrayTime
import org.hamroh.qazo.infra.utils.PrayType
import org.hamroh.qazo.infra.utils.SharedPrefs
import org.hamroh.qazo.infra.utils.dp
import org.hamroh.qazo.infra.utils.getToday
import org.hamroh.qazo.infra.utils.timeFormat

class DayPagingAdapter(
    private var onItemClick: ((String, Int) -> Unit)? = null,
) : PagingDataAdapter<String, DayPagingAdapter.ViewHolder>(DIFF_UTIL) {

    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(
                oldItem: String,
                newItem: String,
            ): Boolean = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: String,
                newItem: String,
            ): Boolean = oldItem == newItem
        }
    }


    class ViewHolder(
        private val binding: ItemDayBinding,
        onItemClick: ((String, Int) -> Unit)?, private val context: Context,
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var prayTime: String

        init {
            binding.tvDate.setOnClickListener { onItemClick?.invoke(prayTime, absoluteAdapterPosition) }

            binding.ivFajr.setOnClickListener { onItemClick?.invoke("$prayTime${PrayTime.FAJR}", absoluteAdapterPosition) }
            binding.ivDhuhr.setOnClickListener { onItemClick?.invoke("$prayTime${PrayTime.DHUHR}", absoluteAdapterPosition) }
            binding.ivAsr.setOnClickListener { onItemClick?.invoke("$prayTime${PrayTime.ASR}", absoluteAdapterPosition) }
            binding.ivMaghrib.setOnClickListener { onItemClick?.invoke("$prayTime${PrayTime.MAGHRIB}", absoluteAdapterPosition) }
            binding.ivIsha.setOnClickListener { onItemClick?.invoke("$prayTime${PrayTime.ISHA}", absoluteAdapterPosition) }
        }

        @SuppressLint("ResourceAsColor", "SetTextI18n")
        fun bind(data: String) {
            prayTime = data

            binding.mvDay.strokeColor = binding.root.context.getColor(
                if (prayTime == getToday().toString()) R.color.ado
                else R.color.textColor
            )
            binding.mvDay.strokeWidth = if (prayTime == getToday().toString()) 2.dp else 1.dp

            if (prayTime.isNotEmpty()) binding.tvDate.text = prayTime.toLong().timeFormat("dd-MMMM, EEEE yyyy") + "-yil"
            if (prayTime.isNotEmpty()) setUp()
        }

        private fun setUp() {
            val fajr = SharedPrefs(context).get("$prayTime${PrayTime.FAJR}", String::class.java)
            binding.bgFajr.setBackgroundResource(getGradient(fajr))

            val dhuhr = SharedPrefs(context).get("$prayTime${PrayTime.DHUHR}", String::class.java)
            binding.bgDhuhr.setBackgroundResource(getGradient(dhuhr))

            val asr = SharedPrefs(context).get("$prayTime${PrayTime.ASR}", String::class.java)
            binding.bgAsr.setBackgroundResource(getGradient(asr))

            val maghrib = SharedPrefs(context).get("$prayTime${PrayTime.MAGHRIB}", String::class.java)
            binding.bgMaghrib.setBackgroundResource(getGradient(maghrib))

            val isha = SharedPrefs(context).get("$prayTime${PrayTime.ISHA}", String::class.java)
            binding.bgIsha.setBackgroundResource(getGradient(isha))
        }

        private fun getGradient(prayType: String): Int = if (prayTime.toLong() > getToday())
            R.drawable.gr_will else when (prayType) {
            PrayType.ADO.toString() -> R.drawable.gr_ado
            PrayType.PRAY.toString() -> R.drawable.gr_pray
            PrayType.QAZO.toString() -> R.drawable.gr_qazo
            else -> R.drawable.gr_yet
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(ItemDayBinding.inflate(LayoutInflater.from(parent.context), parent, false), onItemClick, parent.context)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position)!!)
}

