package org.hamroh.qazo.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.hamroh.qazo.R
import org.hamroh.qazo.databinding.FragmentMainBinding
import org.hamroh.qazo.infra.utils.LoadAdapter
import org.hamroh.qazo.infra.utils.SharedPrefs
import org.hamroh.qazo.infra.utils.getToday
import org.hamroh.qazo.infra.utils.hide
import org.hamroh.qazo.infra.utils.show
import org.hamroh.qazo.ui.calendar.CalendarFragment
import org.hamroh.qazo.ui.profile.ProfileDialog
import org.hamroh.qazo.ui.qazo.QazoFragment
import org.hamroh.qazo.ui.status.StatusDialog


class MainFragment : Fragment() {

    private val binding get() = _binding!!
    private var _binding: FragmentMainBinding? = null
    private val viewModel: DayViewModel by viewModels()
    private lateinit var dayAdapter: DayPagingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupProfile()

        setupList()
        setupData()
        setupQazo()
        setupToday()
    }

    private fun setupToday() {
        binding.rvDay.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as? LinearLayoutManager ?: return
                val adapter = recyclerView.adapter as? DayPagingAdapter ?: return

                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                val isTodayVisible = (firstVisibleItemPosition..lastVisibleItemPosition).any { position ->
                    val item = adapter.peek(position)
                    item == getToday().toString()
                }
                if (isTodayVisible) binding.mvToday.hide() else binding.mvToday.show()
            }
        })

        binding.bnToday.setOnClickListener {
            val adapter = binding.rvDay.adapter as? DayPagingAdapter ?: return@setOnClickListener

            val todayPosition = (0 until adapter.itemCount).find { position ->
                adapter.peek(position) == getToday().toString()
            }

            if (todayPosition != null) binding.rvDay.smoothScrollToPosition(todayPosition)
            else goTo(getToday())
        }
    }

    private fun goTo(day: Long) {
        dayAdapter.submitData(lifecycle, PagingData.from(emptyList()))
        viewModel.getList(day).observe(viewLifecycleOwner) { dayAdapter.submitData(lifecycle, it) }
    }

    private fun setupQazo() {
        binding.tvQazo.setOnClickListener {
            val qazoFragment = QazoFragment()
            qazoFragment.onClick = { requireActivity().recreate() }
            qazoFragment.show(requireActivity().supportFragmentManager, "QazoFragment")
        }
    }

    private fun setupProfile() {
        setName()
        binding.bnProfile.setOnClickListener {
            val profile = ProfileDialog()
            profile.onClick = { requireActivity().recreate() }
            profile.show(requireActivity().supportFragmentManager, "ProfileDialog")
        }
    }

    private fun setName() {
        val name = SharedPrefs(requireContext()).name
        binding.tvName.text = name.ifEmpty { getString(R.string.your_name) }
    }

    @SuppressLint("SetTextI18n")
    private fun setupData() {
        binding.tvQazo.text = "${SharedPrefs(requireContext()).allQazo()} ta"
        binding.tvPray.text = "${SharedPrefs(requireContext()).allPray()} ta"
        binding.tvAdo.text = "${SharedPrefs(requireContext()).allAdo()} ta"
    }

    private fun setupList() {
        dayAdapter = DayPagingAdapter(::changeStatus)
        dayAdapter.withLoadStateHeader(LoadAdapter())
        dayAdapter.withLoadStateFooter(LoadAdapter())
        goTo(getToday())
        binding.rvDay.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = dayAdapter
        }
    }

    private fun changeStatus(key: String, pos: Int) {
        if (key.length > 15) {
            val prayTime = key.substring(13)
            val statusDialog = StatusDialog()
            statusDialog.onClick = { prayType ->
                SharedPrefs(requireContext()).decrease(SharedPrefs(requireContext()).get(key, String::class.java) + prayTime)
                SharedPrefs(requireContext()).put(key, prayType)
                SharedPrefs(requireContext()).increase(prayType + prayTime)
                dayAdapter.notifyItemChanged(pos)
                setupData()
            }
            statusDialog.show(requireActivity().supportFragmentManager, "StatusDialog")
        } else {
            val calendarFragment = CalendarFragment()
            calendarFragment.day = key.toLong()
            calendarFragment.onClick = ::goTo
            calendarFragment.show(requireActivity().supportFragmentManager, "CalendarFragment")
        }
    }

}

