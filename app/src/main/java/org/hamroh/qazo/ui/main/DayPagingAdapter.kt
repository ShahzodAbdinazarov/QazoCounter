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
import org.hamroh.qazo.infra.utils.getDate

class DayPagingAdapter(private var onItemClick: ((String, Int) -> Unit)? = null) : PagingDataAdapter<String, DayPagingAdapter.ViewHolder>(DIFF_UTIL) {

    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

        }
    }


    class ViewHolder(private val binding: ItemDayBinding, onItemClick: ((String, Int) -> Unit)?, private val context: Context) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var prayTime: String

        init {
            binding.tvDate.setOnClickListener { onItemClick?.invoke(prayTime, absoluteAdapterPosition) }

            binding.tvFajr.setOnClickListener { onItemClick?.invoke("$prayTime${PrayTime.FAJR}", absoluteAdapterPosition) }
            binding.tvDhuhr.setOnClickListener { onItemClick?.invoke("$prayTime${PrayTime.DHUHR}", absoluteAdapterPosition) }
            binding.tvAsr.setOnClickListener { onItemClick?.invoke("$prayTime${PrayTime.ASR}", absoluteAdapterPosition) }
            binding.tvMaghrib.setOnClickListener { onItemClick?.invoke("$prayTime${PrayTime.MAGHRIB}", absoluteAdapterPosition) }
            binding.tvIsha.setOnClickListener { onItemClick?.invoke("$prayTime${PrayTime.ISHA}", absoluteAdapterPosition) }
        }

        @SuppressLint("ResourceAsColor", "SetTextI18n")
        fun bind(data: String) {

            prayTime = data
            binding.tvDate.text = prayTime.toLong().getDate("dd-MMMM, EEEE yyyy") + "-yil"
            setUp(prayTime)

        }

        private fun setUp(prayTime: String) {
            val fajr = SharedPrefs(context).get("$prayTime${PrayTime.FAJR}", String::class.java)
            binding.bgFajr.setBackgroundResource(getGradient(fajr))
            binding.tvFajr.text = getTypeText(fajr)

            val dhuhr = SharedPrefs(context).get("$prayTime${PrayTime.DHUHR}", String::class.java)
            binding.bgDhuhr.setBackgroundResource(getGradient(dhuhr))
            binding.tvDhuhr.text = getTypeText(dhuhr)

            val asr = SharedPrefs(context).get("$prayTime${PrayTime.ASR}", String::class.java)
            binding.bgAsr.setBackgroundResource(getGradient(asr))
            binding.tvAsr.text = getTypeText(asr)

            val maghrib = SharedPrefs(context).get("$prayTime${PrayTime.MAGHRIB}", String::class.java)
            binding.bgMaghrib.setBackgroundResource(getGradient(maghrib))
            binding.tvMaghrib.text = getTypeText(maghrib)

            val isha = SharedPrefs(context).get("$prayTime${PrayTime.ISHA}", String::class.java)
            binding.bgIsha.setBackgroundResource(getGradient(isha))
            binding.tvIsha.text = getTypeText(isha)

        }


        private fun getGradient(prayType: String): Int {
            return when (prayType) {
                PrayType.ADO.toString() -> R.drawable.gr_ado
                PrayType.PRAY.toString() -> R.drawable.gr_pray
                PrayType.QAZO.toString() -> R.drawable.gr_qazo
                else -> R.drawable.gr_yet
            }
        }

        private fun getTypeText(prayType: String): String {
            return when (prayType) {
                PrayType.ADO.toString() -> context.getString(R.string.ado)
                PrayType.PRAY.toString() -> context.getString(R.string.pray)
                PrayType.QAZO.toString() -> context.getString(R.string.qazo)
                else -> context.getString(R.string.yet)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(ItemDayBinding.inflate(LayoutInflater.from(parent.context), parent, false), onItemClick, parent.context)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position)!!)
}

