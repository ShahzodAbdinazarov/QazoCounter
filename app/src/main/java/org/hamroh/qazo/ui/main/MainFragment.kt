package org.hamroh.qazo.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.SnapHelper
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import org.hamroh.qazo.databinding.FragmentMainBinding
import org.hamroh.qazo.infra.utils.SharedPrefs
import org.hamroh.qazo.ui.MainActivity
import org.hamroh.qazo.ui.status.StatusDialog


class MainFragment : Fragment() {

    private val viewModel: DayViewModel by viewModels()
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)

        setupData()
        setupList()

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun setupData() {
        binding.tvQazo.text = "${SharedPrefs(requireContext()).qazo()} ta"
        binding.tvPray.text = "${SharedPrefs(requireContext()).pray()} ta"
        binding.tvAdo.text = "${SharedPrefs(requireContext()).ado()} ta"
    }

    private fun setupList() {
        val dayAdapter = DayPagingAdapter(requireActivity() as MainActivity).apply {
            onItemClick = { key, pos ->
                val prayTime = key.substring(13)
                val statusDialog = StatusDialog()
                statusDialog.onClick = { prayType ->
                    SharedPrefs(requireContext()).decrease(SharedPrefs(requireContext()).get(key, String::class.java) + prayTime)
                    SharedPrefs(requireContext()).put(key, prayType)
                    SharedPrefs(requireContext()).increase(prayType + prayTime)
                    notifyItemChanged(pos)
                    setupData()
                }
                statusDialog.show(requireActivity().supportFragmentManager, "StatusDialog")
            }
        }

        viewModel.getList().observe(viewLifecycleOwner) { dayAdapter.submitData(lifecycle, it) }

        binding.rvDay.apply {
            layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL).apply { reverseLayout = true }
            adapter = dayAdapter
        }

        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.rvDay)

    }

}