package org.hamroh.qazo.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.hamroh.qazo.R
import org.hamroh.qazo.databinding.FragmentCalendarBinding
import org.hamroh.qazo.infra.utils.getToday

class CalendarFragment : BottomSheetDialogFragment() {

    private val binding get() = _binding!!
    private var _binding: FragmentCalendarBinding? = null
    private val viewModel: MonthViewModel by viewModels()
    private lateinit var dayAdapter: CalendarPagingAdapter
    var onClick: ((Long) -> Unit)? = null
    var day = getToday()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.back.setOnClickListener { dismiss() }
        setupList()
    }

    private fun setupList() {
        dayAdapter = CalendarPagingAdapter { key, _ -> onClick?.invoke(key); dismiss() }
        viewModel.getList(day).observe(viewLifecycleOwner) { dayAdapter.submitData(lifecycle, it) }
        binding.rvCalendar.apply {
            layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            adapter = dayAdapter
        }
    }

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

}