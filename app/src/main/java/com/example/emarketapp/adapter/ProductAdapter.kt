/*
package com.example.emarketapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.emarketapp.databinding.ItemProductBinding
import com.example.emarketapp.model.ProductListUIModel
import com.example.emarketapp.view.home.HomeViewModel


class ProductAdapter(
    val context: Context,
    val viewModel: HomeViewModel,
    private var items: MutableList<ProductListUIModel>,
    val addOrRemoveClick: (product: ProductListUIModel) -> Unit,
) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ProductAdapter.ViewHolader {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

    inner class ViewHolder(private val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {
         fun bind(item: ProductListUIModel) {
            binding.apply {
                txtName.text = item.name
                txtPrice.text = item.price

                Glide.with(itemView.context)
                    .load(item.image)
                    .into(productImageView)

                if(item.isInBasket){
                    btnAddToCard.text = "Remove from Basket"
                }else{
                    btnAddToCard.text = "Add to Basket"
                }
                btnAddToCard.setOnClickListener {
                   val product = viewModel.getProduct(item.id)

                    addOrRemoveClick(item)
                       if(product.isInBasket){
                           viewModel.setInBasket(item.id,false)
                           btnAddToCard.text = "Add to Basket"
                       }else{
                           viewModel.setInBasket(item.id,true)
                           btnAddToCard.text = "Remove from Basket"
                       }
                }
            }
        }
    }
}
*/

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.emarketapp.databinding.ItemProductBinding
import com.example.emarketapp.model.ProductListUIModel
import com.example.emarketapp.view.home.HomeViewModel

class ProductAdapter(
    private val context: Context,
    private val viewModel: HomeViewModel,
    private var items: MutableList<ProductListUIModel>,
    private val addOrRemoveClick: (product: ProductListUIModel) -> Unit,
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

                Glide.with(itemView.context)
                    .load(item.image)
                    .into(productImageView)

                updateButtonState(item)

                btnAddToCard.setOnClickListener {
                    addOrRemoveClick(item)
                    item.isInBasket = !item.isInBasket
                    updateButtonState(item)
                    viewModel.setInBasket(item.id, item.isInBasket)
                }
            }
        }

        private fun updateButtonState(item: ProductListUIModel) {
            binding.btnAddToCard.text = if (item.isInBasket) "Remove from Basket" else "Add to Basket"
        }
    }
}