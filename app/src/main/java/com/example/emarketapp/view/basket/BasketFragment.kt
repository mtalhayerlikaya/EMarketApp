package com.example.emarketapp.view.basket

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.emarketapp.R
import com.example.emarketapp.adapter.BasketAdapter
import com.example.emarketapp.base.BaseFragment
import com.example.emarketapp.databinding.FragmentBasketBinding
import com.example.emarketapp.model.ProductListUIModel
import com.example.emarketapp.utils.Resource
import com.example.emarketapp.utils.Spinner
import com.example.emarketapp.view.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BasketFragment : BaseFragment<FragmentBasketBinding>(FragmentBasketBinding::inflate) {
    private val basketViewModel: BasketViewModel by viewModels()
    private lateinit var adapter: BasketAdapter
    private lateinit var mActivity: MainActivity

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mActivity = activity as MainActivity
        observeFlow()
    }

    override fun onResume() {
        super.onResume()
        basketViewModel.getBasketProductList()

    }

    private fun observeFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                basketViewModel.basketProducts.collect { result ->
                    when (result) {
                        is Resource.Failure -> {
                            Spinner.hide()
                            Toast.makeText(requireContext(), result.exceptionMessage, Toast.LENGTH_SHORT).show()
                        }

                        is Resource.Loading -> {
                            Spinner.show(requireContext())
                        }

                        is Resource.Success -> {
                            Spinner.hide()
                            val basketList = result.result
                            handleAdapter(basketList.toMutableList())
                        }
                    }
                }
            }
        }
    }

    private fun handleAdapter(list: MutableList<ProductListUIModel>) {
        adapter = BasketAdapter(mActivity, basketViewModel, list, {
            val productList = adapter.getAdapterList().filter { it.basketItemCount > 0 }
            mActivity.setBadge(productList.size)
        }, { isEmpty ->
            if (isEmpty) {
                binding.emptyLayout.visibility = View.VISIBLE
                mActivity.setBadge(0)
            } else {
                binding.emptyLayout.visibility = View.GONE
            }
        }) { totalPrice ->
            binding.totalPriceTv.text = "$totalPrice₺"
        }
        if (adapter.itemCount == 0)
            binding.totalPriceTv.text = "0.0₺"
        else
            binding.totalPriceTv.text = "$adapter.totalPrice₺"

        if (adapter.itemCount == 0) {
            binding.totalPriceTv.text = "0.0₺"
            binding.emptyLayout.visibility = View.VISIBLE
        } else {
            binding.totalPriceTv.text = "$adapter.totalPrice₺"
            binding.emptyLayout.visibility = View.GONE
        }

        binding.basketRv.adapter = adapter
        binding.basketRv.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)

        binding.completeButton.setOnClickListener {
            if (adapter.itemCount > 0) {
                val updatedList = adapter.getAdapterList().onEach { it.basketItemCount = 0 }
                basketViewModel.updateProductListAfterPurhasing(updatedList)
                binding.totalPriceTv.text = "0.0₺"
                adapter.getAdapterList().clear()
                adapter.notifyDataSetChanged()
                showDialogAfterOrder()
            } else {
                Toast.makeText(requireContext(), getString(R.string.basket_error), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDialogAfterOrder() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        val dialogView: View = layoutInflater.inflate(R.layout.complete_order_dialog, null)
        builder.setView(dialogView)

        val okBtn = dialogView.findViewById<Button>(R.id.okBtn)

        val dialog: AlertDialog = builder.create()
        dialog.show()

        okBtn.setOnClickListener {
            mActivity.binding.bottomNavigationView.selectedItemId = R.id.homeFragment
            dialog.dismiss()
        }
    }

}