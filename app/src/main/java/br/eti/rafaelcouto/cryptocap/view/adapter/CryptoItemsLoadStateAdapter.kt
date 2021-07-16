package br.eti.rafaelcouto.cryptocap.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import br.eti.rafaelcouto.cryptocap.databinding.RowLoadingErrorBinding

class CryptoItemsLoadStateAdapter(
    private val onRetry: (() -> Unit)? = null
) : LoadStateAdapter<CryptoItemsLoadStateAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowLoadingErrorBinding.inflate(inflater, parent, false)

        return ViewHolder(binding)
    }

    inner class ViewHolder(
        private val binding: RowLoadingErrorBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(state: LoadState) = when (state) {
            is LoadState.NotLoading -> setupDefaultState()
            LoadState.Loading -> setupLoadingState()
            is LoadState.Error -> setupErrorState(state.error.localizedMessage.orEmpty())
        }

        private fun setupLoadingState() = binding.run {
            pbLoading.isVisible = true
            btRetry.isVisible = false
            tvError.isVisible = false
        }

        private fun setupErrorState(message: String) = binding.run {
            pbLoading.isVisible = false

            tvError.isVisible = true
            tvError.text = message

            btRetry.isVisible = true
            btRetry.setOnClickListener { onRetry?.invoke() }
        }

        private fun setupDefaultState() = binding.run {
            pbLoading.isVisible = false
            btRetry.isVisible = false
            tvError.isVisible = false
        }
    }
}
