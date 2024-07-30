package com.example.emarketapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.emarketapp.R
import com.example.emarketapp.databinding.ItemProductBinding
import com.example.emarketapp.model.ProductListUIModel
import com.example.emarketapp.view.home.HomeViewModel

class ProductAdapter(
    private val context: Context,
    private val viewModel: HomeViewModel,
    private var items: MutableList<ProductListUIModel>,
    private val productClick: (productID: String) -> Unit,
) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateProductList(updateList: List<ProductListUIModel>) {
        items.clear()
        items.addAll(updateList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ProductListUIModel) {
            binding.apply {
                txtName.text = item.name
                txtPrice.text = item.price
                productRootLy.setOnClickListener {
                    productClick(item.id)
                }
                Glide.with(itemView.context)
                    .load(item.image)
                    .into(productImageView)

                updateButtonState(item)
                updateFavoriteState(item, binding)
                btnAddToCard.setOnClickListener {
                    item.basketItemCount = if (item.basketItemCount != 0) 0 else 1
                    updateButtonState(item)
                    viewModel.updateProduct(item)
                }
                addFavorite.setOnClickListener {
                    item.isFavorite = !item.isFavorite
                    updateFavoriteState(item, binding)
                    viewModel.updateProduct(item)
                }
            }
        }

        private fun updateButtonState(item: ProductListUIModel) {
            binding.btnAddToCard.text = if (item.basketItemCount != 0) "Remove from Basket" else "Add to Basket"
        }
        private fun updateFavoriteState(item: ProductListUIModel, binding: ItemProductBinding) {
            if (item.isFavorite)
                binding.addFavorite.setImageDrawable(context.getDrawable(R.drawable.icon_star_liked))
            else
                binding.addFavorite.setImageDrawable(context.getDrawable(R.drawable.icon_star_white))
        }
    }
}