package br.eti.rafaelcouto.cryptocap.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import br.eti.rafaelcouto.cryptocap.R
import br.eti.rafaelcouto.cryptocap.databinding.RowCryptoItemBinding
import br.eti.rafaelcouto.cryptocap.domain.model.CryptoItemUI

class CryptoItemsAdapter : PagingDataAdapter<CryptoItemUI, CryptoItemsAdapter.ViewHolder>(
    CryptoItemComparator
) {

    var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = DataBindingUtil.inflate<RowCryptoItemBinding>(
            inflater, viewType, parent, false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun getItemViewType(position: Int) = R.layout.row_crypto_item

    inner class ViewHolder(
        private val binding: RowCryptoItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CryptoItemUI) {
            binding.item = item
            binding.invalidateAll()

            onItemClickListener?.let { listener ->
                itemView.setOnClickListener { listener.onItemClick(item.id) }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(id: Long)
    }

    object CryptoItemComparator : DiffUtil.ItemCallback<CryptoItemUI>() {
        override fun areItemsTheSame(
            oldItem: CryptoItemUI,
            newItem: CryptoItemUI
        ) = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: CryptoItemUI,
            newItem: CryptoItemUI
        ) = oldItem == newItem
    }
}
