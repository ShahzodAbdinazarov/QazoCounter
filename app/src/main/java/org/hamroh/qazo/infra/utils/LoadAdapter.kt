package org.hamroh.qazo.infra.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import org.hamroh.qazo.databinding.ItemLoadingBinding

class LoadAdapter(private val retry: (() -> Unit?)? = null) : LoadStateAdapter<LoadAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder =
        ViewHolder(ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false), retry)


    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) = holder.bind(loadState)

    class ViewHolder(
        private val binding: ItemLoadingBinding,
        private val retry: (() -> Unit?)? = null
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.retryButton.setOnClickListener {
                retry?.invoke()
            }
        }

        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Loading) {
                binding.retryButton.visibility = View.VISIBLE
            } else {
                binding.retryButton.visibility = View.GONE
            }

//            if (loadState is LoadState.Error) {
//                binding.errorMsg.visibility = View.VISIBLE
//                binding.errorMsg.text = loadState.error.localizedMessage
//                binding.retryButton.visibility = View.VISIBLE
//            } else {
//                binding.errorMsg.visibility = View.GONE
//                binding.retryButton.visibility = View.GONE
//            }
        }
    }
}
