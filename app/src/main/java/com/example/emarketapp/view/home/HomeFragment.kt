package com.example.emarketapp.view.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.emarketapp.adapter.ProductAdapter
import com.example.emarketapp.base.BaseFragment
import com.example.emarketapp.databinding.FragmentHomeBinding
import com.example.emarketapp.model.ProductListUIModel
import com.example.emarketapp.utils.Resource
import com.example.emarketapp.utils.pksCollectResult
import com.example.emarketapp.view.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var mActivity: MainActivity
    private lateinit var adapter: ProductAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mActivity = activity as MainActivity
        homeViewModel.getProductList()
        getProductList()
        queryTextListener()
    }

    private fun queryTextListener() {
        binding.homeSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    homeViewModel.getProductList()
                } else {
                    homeViewModel.searchProduct(newText)
                }
                return true
            }
        })

        homeViewModel.searchProductFlow.pksCollectResult(
            lifecycleScope = this,
            contextForShowSpinner = mActivity,
            resultError = { message ->
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        ) {
            val result = it as Resource.Success
            val productList = result.result
            adapter.updateProductList(productList)
        }
    }

    private fun getProductList() {
        homeViewModel.allProducts.pksCollectResult(
            lifecycleScope = this,
            contextForShowSpinner = mActivity,
            resultError = { message ->
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        ) {
            val result = it as Resource.Success
            val productList = result.result
            handleAdapter(productList.toMutableList())
        }
    }

    private fun handleAdapter(list: MutableList<ProductListUIModel>) {
        adapter = ProductAdapter(list) { _ -> }
        binding.rv.adapter = adapter
        binding.rv.layoutManager = GridLayoutManager(mActivity, 2, LinearLayoutManager.VERTICAL, false)
    }


}