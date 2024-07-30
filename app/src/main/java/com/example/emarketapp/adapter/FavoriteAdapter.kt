package com.example.emarketapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.emarketapp.R
import com.example.emarketapp.databinding.ItemProductFavoriteBinding
import com.example.emarketapp.model.ProductListUIModel
import com.example.emarketapp.view.favorite.FavoriteViewModel

class FavoriteAdapter(
    private val context: Context,
    private val viewModel: FavoriteViewModel,
    private var items: MutableList<ProductListUIModel>,
    private val addOrRemoveClick: (product: ProductListUIModel) -> Unit,
) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProductFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    override fun getItemCount() = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateProductList(updateList: List<ProductListUIModel>) {
        items.clear()
        items.addAll(updateList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemProductFavoriteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ProductListUIModel, position: Int) {
            binding.apply {
                favTxtName.text = item.name
                favTxtPrice.text = item.price

                Glide.with(itemView.context)
                    .load(item.image)
                    .into(favProductImageView)

                updateFavoriteState(item, binding)

                favAddToFavorite.setOnClickListener {
                    addOrRemoveClick(item)
                    removeFavoriteProduct(position)
                    item.isFavorite = !item.isFavorite
                    updateFavoriteState(item, binding)
                    viewModel.updateProduct(item)
                }
            }
        }

        private fun removeFavoriteProduct(position: Int) {
            items.removeAt(position)
            notifyItemRemoved(position)
            notifyDataSetChanged()
        }

        private fun updateFavoriteState(item: ProductListUIModel, binding: ItemProductFavoriteBinding) {
            if (item.isFavorite)
                binding.favAddToFavorite.setImageDrawable(context.getDrawable(R.drawable.icon_star_liked))
            else
                binding.favAddToFavorite.setImageDrawable(context.getDrawable(R.drawable.icon_star_white))
        }
    }
}