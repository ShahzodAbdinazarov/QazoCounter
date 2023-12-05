package org.hamroh.qazo.ui.status

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.hamroh.qazo.R
import org.hamroh.qazo.databinding.FragmentStatusBinding
import org.hamroh.qazo.infra.utils.PrayType

class StatusDialog : BottomSheetDialogFragment() {

    private var _binding: FragmentStatusBinding? = null
    private val binding get() = _binding!!
    var onClick: ((String) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentStatusBinding.inflate(inflater, container, false)

        binding.bnAdo.setOnClickListener { backInvoke(PrayType.ADO.toString()) }
        binding.bnPray.setOnClickListener { backInvoke(PrayType.PRAY.toString()) }
        binding.bnQazo.setOnClickListener { backInvoke(PrayType.QAZO.toString()) }
        binding.bnYet.setOnClickListener { backInvoke(PrayType.YET.toString()) }

        return binding.root
    }

    private fun backInvoke(prayType: String) {
        onClick?.invoke(prayType)
        dismiss()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.BottomSheetDialog)
    }
}