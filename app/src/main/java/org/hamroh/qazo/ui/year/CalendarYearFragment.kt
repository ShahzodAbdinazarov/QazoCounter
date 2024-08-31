package org.hamroh.qazo.ui.year

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import com.kizitonwose.calendar.view.ViewContainer
import org.hamroh.qazo.R
import org.hamroh.qazo.databinding.FragmentCalendarYearBinding
import org.hamroh.qazo.databinding.ItemCalendarDayBinding
import org.hamroh.qazo.databinding.ItemCalendarHeaderBinding
import org.hamroh.qazo.infra.utils.PrayTime
import org.hamroh.qazo.infra.utils.PrayType
import org.hamroh.qazo.infra.utils.SharedPrefs
import org.hamroh.qazo.infra.utils.getToday
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId


class CalendarYearFragment : BottomSheetDialogFragment() {

    private val today = LocalDate.now()
    private var selectedDate: LocalDate? = null
    var onClick: ((Long) -> Unit)? = null
    private var bg: Int = R.drawable.gr_ado

    private val binding get() = _binding!!
    private var _binding: FragmentCalendarYearBinding? = null

    override fun onStart() {
        super.onStart()
        val bottomSheet = dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.layoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.BottomSheetDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCalendarYearBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.back.setOnClickListener { dismiss() }

        val daysOfWeek = daysOfWeek(DayOfWeek.MONDAY)
        configureBinders()
        binding.exTwoCalendar.setup(
            YearMonth.now().minusMonths(200),
            YearMonth.now().plusMonths(200),
            daysOfWeek.first(),
        )
        val firstDayOfMonth = today.withDayOfMonth(1)
        val targetDate = firstDayOfMonth.minusDays(1) // Adjust as needed
        binding.exTwoCalendar.scrollToDate(targetDate)
    }

    private fun configureBinders() {
        val calendarView = binding.exTwoCalendar

        class DayViewContainer(view: View) : ViewContainer(view) {

            // Will be set when this container is bound. See the dayBinder.
            lateinit var day: CalendarDay
            var adapter = CalendarDotsAdapter()
            val textView = ItemCalendarDayBinding.bind(view).exTwoDayText
            val rvDots = ItemCalendarDayBinding.bind(view).rvDots

            init {
                rvDots.layoutManager = StaggeredGridLayoutManager(1, 0)
                rvDots.adapter = adapter

                textView.setOnClickListener {
                    if (day.position == DayPosition.MonthDate) {
                        if (selectedDate == day.date) {
                            selectedDate = null
                            calendarView.notifyDayChanged(day)
                        } else {
                            val oldDate = selectedDate
                            selectedDate = day.date
                            calendarView.notifyDateChanged(day.date)
                            oldDate?.let { calendarView.notifyDateChanged(oldDate) }
                        }
                        val millis = day.date
                            .atStartOfDay(ZoneId.systemDefault())
                            .toInstant()
                            .toEpochMilli()
                        openDaily(millis)
                    }
                }
            }
        }

        calendarView.dayBinder = object : MonthDayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)

            override fun bind(container: DayViewContainer, data: CalendarDay) {
                container.day = data
                val textView = container.textView
                val adapter = container.adapter
                textView.text = data.date.dayOfMonth.toString()

                val millis = data.date
                    .atStartOfDay(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli()

                if (data.position == DayPosition.MonthDate) {
                    textView.makeVisible()

                    val dots = arrayListOf(
                        millis.toType(PrayTime.FAJR),
                        millis.toType(PrayTime.DHUHR),
                        millis.toType(PrayTime.ASR),
                        millis.toType(PrayTime.MAGHRIB),
                        millis.toType(PrayTime.ISHA),
                    )
                    adapter.submitList(dots)

//                    card.strokeWidth = if (data.date == today) 2.dp else 0.dp
                    textView.setBackgroundResource(
                        setUp(millis.toString())
                    )

                } else textView.makeInVisible()
            }
        }

        class MonthViewContainer(view: View) : ViewContainer(view) {
            val textView = ItemCalendarHeaderBinding.bind(view).exTwoHeaderText
        }
        calendarView.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View) = MonthViewContainer(view)

            override fun bind(container: MonthViewContainer, data: CalendarMonth) {
                container.textView.text = data.yearMonth.displayText(false)
            }
        }
    }

    private fun openDaily(day: Long) {
        onClick?.invoke(day)
        dismiss()
    }

    private fun Long.toType(
        prayTime: PrayTime,
    ): String = SharedPrefs(requireContext()).get(
        "$this${prayTime}",
        String::class.java
    )

    private fun setUp(prayTime: String): Int {
        setGradient(SharedPrefs(binding.root.context).get("$prayTime${PrayTime.FAJR}", String::class.java), prayTime)
        setGradient(SharedPrefs(binding.root.context).get("$prayTime${PrayTime.DHUHR}", String::class.java), prayTime)
        setGradient(SharedPrefs(binding.root.context).get("$prayTime${PrayTime.ASR}", String::class.java), prayTime)
        setGradient(SharedPrefs(binding.root.context).get("$prayTime${PrayTime.MAGHRIB}", String::class.java), prayTime)
        return setGradient(SharedPrefs(binding.root.context).get("$prayTime${PrayTime.ISHA}", String::class.java), prayTime)
    }

    private fun setGradient(prayType: String, prayTime: String): Int {
        val current = getGradient(prayType)

        return if (prayTime.toLong() > getToday()) R.drawable.gr_will_round
        else when (current) {
            R.drawable.gr_yet_round -> R.drawable.gr_yet_round
            R.drawable.gr_qazo_round -> when (bg) {
                R.drawable.gr_yet_round -> R.drawable.gr_yet_round
                else -> R.drawable.gr_qazo_round
            }

            R.drawable.gr_pray_round -> when (bg) {
                R.drawable.gr_yet_round -> R.drawable.gr_yet_round
                R.drawable.gr_qazo_round -> R.drawable.gr_qazo_round
                else -> R.drawable.gr_pray_round
            }

            else -> when (bg) {
                R.drawable.gr_yet_round -> R.drawable.gr_yet_round
                R.drawable.gr_qazo_round -> R.drawable.gr_qazo_round
                R.drawable.gr_pray_round -> R.drawable.gr_pray_round
                else -> R.drawable.gr_ado_round
            }
        }
    }

    private fun getGradient(prayType: String): Int {
        return when (prayType) {
            PrayType.ADO.toString() -> R.drawable.gr_ado_round
            PrayType.PRAY.toString() -> R.drawable.gr_pray_round
            PrayType.QAZO.toString() -> R.drawable.gr_qazo_round
            else -> R.drawable.gr_yet_round
        }
    }
}