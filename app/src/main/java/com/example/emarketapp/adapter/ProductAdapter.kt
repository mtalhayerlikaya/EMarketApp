package com.example.emarketapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.emarketapp.databinding.ItemProductBinding
import com.example.emarketapp.model.ProductListUIModel


class ProductAdapter(
    private var items: MutableList<ProductListUIModel>,
    val removeBlock: (blockedContact: ProductListUIModel) -> Unit,
) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {
    private lateinit var binding: ItemProductBinding
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ProductAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ItemProductBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductAdapter.ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateProductList(updateList: List<ProductListUIModel>) {
        items.clear()
        items.addAll(updateList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ProductListUIModel) {
            binding.apply {
                txtName.text = item.name
                txtPrice.text = item.price
                Glide.with(itemView.context)
                    .load(item.image)
                    .into(productImageView)
            }
        }
    }
}
