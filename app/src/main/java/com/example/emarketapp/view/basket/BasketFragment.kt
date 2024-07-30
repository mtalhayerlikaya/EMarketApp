package com.example.emarketapp.view.basket

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

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
        adapter = BasketAdapter(mActivity, basketViewModel, list) { totalPrice ->
            binding.totalPriceTv.text = "$totalPrice₺"
        }
        if (adapter.itemCount == 0)
            binding.totalPriceTv.text = "0.0₺"
        else
            binding.totalPriceTv.text = "$adapter.totalPrice₺"

        binding.basketRv.adapter = adapter
        binding.basketRv.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)
    }

}