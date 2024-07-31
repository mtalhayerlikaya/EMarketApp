package com.example.emarketapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.emarketapp.databinding.ItemBasketBinding
import com.example.emarketapp.model.ProductListUIModel
import com.example.emarketapp.view.basket.BasketViewModel

class BasketAdapter(
    private val context: Context,
    private val viewModel: BasketViewModel,
    private var items: MutableList<ProductListUIModel>,
    private val setTotalPrice: (totalPrice: Double) -> Unit,
) : RecyclerView.Adapter<BasketAdapter.ViewHolder>() {

    public var totalPrice = 0.0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBasketBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    override fun getItemCount() = items.size

    fun getAdapterList(): MutableList<ProductListUIModel> {
        return items
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateProductList(updateList: List<ProductListUIModel>) {
        items.clear()
        items.addAll(updateList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemBasketBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ProductListUIModel, position: Int) {
            binding.apply {

                productName.text = item.name
                productPrice.text = item.price
                totalPrice += item.price.toDouble() * item.basketItemCount
                totalPriceTv.text = item.basketItemCount.toString()
                increasePriceCardView.setOnClickListener {
                    item.basketItemCount += 1
                    totalPriceTv.text = item.basketItemCount.toString()
                    viewModel.updateProduct(item)
                    totalPrice += item.price.toDouble()
                    setTotalPrice(totalPrice)
                }
                decreasePriceCardView.setOnClickListener {
                    if (item.basketItemCount > 0) {
                        item.basketItemCount -= 1
                        totalPriceTv.text = item.basketItemCount.toString()
                        viewModel.updateProduct(item)
                        if (item.basketItemCount == 0) {
                            totalPrice = 0.0
                            if (items.size == 1) setTotalPrice(totalPrice)
                            removeBasketProduct(position)
                        } else {
                            totalPrice -= item.price.toDouble()
                            setTotalPrice(totalPrice)
                        }
                    } else {
                        Toast.makeText(context, "Item can not be lower than zero", Toast.LENGTH_SHORT).show()
                    }
                }
                if (items.last() == item) setTotalPrice(totalPrice)
            }

        }

        private fun removeBasketProduct(position: Int) {
            items.removeAt(position)
            notifyItemRemoved(position)
            notifyDataSetChanged()
        }
    }
}