package org.hamroh.qazo.ui.qazo

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.isDigitsOnly
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.hamroh.qazo.R
import org.hamroh.qazo.databinding.FragmentQazoBinding
import org.hamroh.qazo.infra.utils.SharedPrefs
import org.hamroh.qazo.infra.utils.closeKeyboard
import org.hamroh.qazo.infra.utils.showSnackbar

class QazoFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentQazoBinding? = null
    private val binding get() = _binding!!
    var onClick: ((Boolean) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentQazoBinding.inflate(inflater, container, false)

        setupQazolar()
        setupChange()
        binding.ibSave.setOnClickListener { saveQazo() }

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun setupChange() {
        binding.bnPlusFajr.setOnClickListener { binding.etFajr.setText((binding.etFajr.text.toString().toLong() + 1).toString()) }
        binding.bnMinusFajr.setOnClickListener { binding.etFajr.setText((binding.etFajr.text.toString().toLong() - 1).toString()) }
        binding.bnPlusDhuhr.setOnClickListener { binding.etDhuhr.setText((binding.etDhuhr.text.toString().toLong() + 1).toString()) }
        binding.bnMinusDhuhr.setOnClickListener { binding.etDhuhr.setText((binding.etDhuhr.text.toString().toLong() - 1).toString()) }
        binding.bnPlusAsr.setOnClickListener { binding.etAsr.setText((binding.etAsr.text.toString().toLong() + 1).toString()) }
        binding.bnMinusAsr.setOnClickListener { binding.etAsr.setText((binding.etAsr.text.toString().toLong() - 1).toString()) }
        binding.bnPlusMaghrib.setOnClickListener { binding.etMaghrib.setText((binding.etMaghrib.text.toString().toLong() + 1).toString()) }
        binding.bnMinusMaghrib.setOnClickListener { binding.etMaghrib.setText((binding.etMaghrib.text.toString().toLong() - 1).toString()) }
        binding.bnPlusIsha.setOnClickListener { binding.etIsha.setText((binding.etIsha.text.toString().toLong() + 1).toString()) }
        binding.bnMinusIsha.setOnClickListener { binding.etIsha.setText((binding.etIsha.text.toString().toLong() - 1).toString()) }
    }

    private fun setupQazolar() {
        val shp = SharedPrefs(requireContext())
        binding.etFajr.setText(shp.getFajr().toString())
        binding.etDhuhr.setText(shp.getDhuhr().toString())
        binding.etAsr.setText(shp.getAsr().toString())
        binding.etMaghrib.setText(shp.getMaghrib().toString())
        binding.etIsha.setText(shp.getIsha().toString())
    }

    private fun saveQazo() {
        if (validations()) {
            val shp = SharedPrefs(requireContext())
            shp.setFajr(binding.etFajr.text.toString().toLong())
            shp.setDhuhr(binding.etDhuhr.text.toString().toLong())
            shp.setAsr(binding.etAsr.text.toString().toLong())
            shp.setMaghrib(binding.etMaghrib.text.toString().toLong())
            shp.setIsha(binding.etIsha.text.toString().toLong())
            binding.etFajr.requestFocus()
            requireActivity().closeKeyboard(binding.etFajr)
            onClick?.invoke(true)
            dismiss()
        }
    }

    private fun validations(): Boolean {
        if (binding.etFajr.text.toString().isEmpty()) {
            this@QazoFragment.showSnackbar("Bomdod namozi qazolari sonini kiriting!")
            return false
        }
        if (!binding.etFajr.text.toString().isDigitsOnly()) {
            this@QazoFragment.showSnackbar("Bomdod namozi qazolari soni faqat raqamlardan iborat bo'lishi kerak!")
            return false
        }
        if (binding.etDhuhr.text.toString().isEmpty()) {
            this@QazoFragment.showSnackbar("Peshin namozi qazolari sonini kiriting!")
            return false
        }
        if (!binding.etDhuhr.text.toString().isDigitsOnly()) {
            this@QazoFragment.showSnackbar("Peshin namozi qazolari soni faqat raqamlardan iborat bo'lishi kerak!")
            return false
        }
        if (binding.etAsr.text.toString().isEmpty()) {
            this@QazoFragment.showSnackbar("Asr namozi qazolari sonini kiriting!")
            return false
        }
        if (!binding.etAsr.text.toString().isDigitsOnly()) {
            this@QazoFragment.showSnackbar("Asr namozi qazolari soni faqat raqamlardan iborat bo'lishi kerak!")
            return false
        }
        if (binding.etMaghrib.text.toString().isEmpty()) {
            this@QazoFragment.showSnackbar("Shom namozi qazolari sonini kiriting!")
            return false
        }
        if (!binding.etMaghrib.text.toString().isDigitsOnly()) {
            this@QazoFragment.showSnackbar("Shom namozi qazolari soni faqat raqamlardan iborat bo'lishi kerak!")
            return false
        }
        if (binding.etIsha.text.toString().isEmpty()) {
            this@QazoFragment.showSnackbar("Xufton namozi qazolari sonini kiriting!")
            return false
        }
        if (!binding.etIsha.text.toString().isDigitsOnly()) {
            this@QazoFragment.showSnackbar("Xufton namozi qazolari soni faqat raqamlardan iborat bo'lishi kerak!")
            return false
        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.BottomSheetDialog)
    }
}