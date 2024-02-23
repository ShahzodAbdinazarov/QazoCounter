package org.hamroh.qazo.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import org.hamroh.qazo.R
import org.hamroh.qazo.databinding.FragmentMainBinding
import org.hamroh.qazo.infra.utils.SharedPrefs
import org.hamroh.qazo.infra.utils.getToday
import org.hamroh.qazo.infra.utils.hide
import org.hamroh.qazo.infra.utils.show
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

        setupProfile()
        setupAblution()
        setupList()
        setupData()
        setupQazo()
        setupToday()

        return binding.root
    }

    private fun setupToday() {
        binding.rvDay.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (isRecyclerAtTop() && dy > 0 && binding.mvToday.isShown) binding.mvToday.hide()
                else if (!isRecyclerAtTop() && dy < 0 && !binding.mvToday.isShown) binding.mvToday.show()
            }
        })
        binding.bnToday.setOnClickListener { viewModel.getList(getToday()).observe(viewLifecycleOwner) { dayAdapter.submitData(lifecycle, it) } }
    }

    private fun isRecyclerAtTop(): Boolean {
        val layoutManager = binding.rvDay.layoutManager as StaggeredGridLayoutManager
        val firstVisibleItems = IntArray(layoutManager.spanCount)
        layoutManager.findFirstCompletelyVisibleItemPositions(firstVisibleItems)
        for (position in firstVisibleItems) if (position == 0) return true
        return false
    }

    private fun setupQazo() {
        binding.tvQazo.setOnClickListener {
            Log.e("TAG", "setupQazo: ")
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

    private fun setupAblution() {
        checkAblution()
        binding.bnAblution.setOnClickListener {
            if (SharedPrefs(requireContext()).ablution) SharedPrefs(requireContext()).setAblution(false)
            else SharedPrefs(requireContext()).setAblution(true)
            checkAblution()
        }
    }

    private fun checkAblution() {
        if (SharedPrefs(requireContext()).ablution) {
            binding.bgAblution.setBackgroundResource(R.drawable.gr_ado)
            binding.ivHave.visibility = View.VISIBLE
            binding.ivHaveNot.visibility = View.INVISIBLE
        } else {
            binding.bgAblution.setBackgroundResource(R.drawable.gr_qazo)
            binding.ivHaveNot.visibility = View.VISIBLE
            binding.ivHave.visibility = View.INVISIBLE
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupData() {
        binding.tvQazo.text = "${SharedPrefs(requireContext()).allQazo()} ta"
        binding.tvPray.text = "${SharedPrefs(requireContext()).allPray()} ta"
        binding.tvAdo.text = "${SharedPrefs(requireContext()).allAdo()} ta"
    }

    private fun setupList() {
        dayAdapter = DayPagingAdapter(::changeStatus)
        viewModel.getList(getToday()).observe(viewLifecycleOwner) { dayAdapter.submitData(lifecycle, it) }
        binding.rvDay.apply {
            layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL).apply { reverseLayout = true }
            adapter = dayAdapter
        }
        val snapHelper: SnapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.rvDay)
    }

    private fun changeStatus(key: String, pos: Int) {
        val prayTime = key.substring(13)
        val statusDialog = StatusDialog()
        statusDialog.onClick = { prayType ->
            SharedPrefs(requireContext()).decrease(SharedPrefs(requireContext()).get(key, String::class.java) + prayTime)
            SharedPrefs(requireContext()).put(key, prayType)
            SharedPrefs(requireContext()).increase(prayType + prayTime)
            Log.e("TAG", "changeStatus: ${prayType + prayTime}")
            dayAdapter.notifyItemChanged(pos)
            setupData()
        }
        statusDialog.show(requireActivity().supportFragmentManager, "StatusDialog")
    }

}

