package br.eti.rafaelcouto.cryptocap.view.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import br.eti.rafaelcouto.cryptocap.R
import br.eti.rafaelcouto.cryptocap.databinding.RowCryptoItemBinding
import br.eti.rafaelcouto.cryptocap.domain.model.CryptoItemUI

class CryptoItemsAdapter : RecyclerView.Adapter<CryptoItemsAdapter.ViewHolder>() {

    private val mutableItems: MutableList<CryptoItemUI> = mutableListOf()
    var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = DataBindingUtil.inflate<RowCryptoItemBinding>(
            inflater, viewType, parent, false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItemForPosition(position)
        holder.bind(item)
    }

    override fun getItemCount() = mutableItems.size
    override fun getItemViewType(position: Int) = R.layout.row_crypto_item

    fun setItems(items: List<CryptoItemUI>) {
        mutableItems.clear()
        mutableItems.addAll(items)
        notifyDataSetChanged()
    }

    private fun getItemForPosition(position: Int) = mutableItems[position]

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
}
