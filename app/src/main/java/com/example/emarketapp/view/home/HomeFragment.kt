package com.example.emarketapp.view.home

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.emarketapp.R
import com.example.emarketapp.adapter.ProductAdapter
import com.example.emarketapp.base.BaseFragment
import com.example.emarketapp.databinding.FragmentHomeBinding
import com.example.emarketapp.model.ProductListUIModel
import com.example.emarketapp.utils.Resource
import com.example.emarketapp.utils.pksCollectResult
import com.example.emarketapp.view.MainActivity
import com.google.android.material.slider.Slider
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
        initUI()
    }

    private fun initUI() {
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
        homeViewModel.filterPriceRange.pksCollectResult(
            lifecycleScope = this,
            contextForShowSpinner = mActivity,
            resultError = { message ->
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        ) {
            val result = it as Resource.Success
            val productListBetweenRange = result.result
            adapter.updateProductList(productListBetweenRange)
        }
        binding.selectFilter.setOnClickListener {
            showFilterDialog()
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

    private fun showFilterDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        val dialogView: View = layoutInflater.inflate(R.layout.dialog_filter, null)
        builder.setView(dialogView)

        val minPriceSlider = dialogView.findViewById<Slider>(R.id.minPriceSlider)
        val maxPriceSlider = dialogView.findViewById<Slider>(R.id.maxPriceSlider)
        val filterButton = dialogView.findViewById<Button>(R.id.filterButton)
        val minPriceTextView = dialogView.findViewById<TextView>(R.id.minPriceTextView)
        val maxPriceTextView = dialogView.findViewById<TextView>(R.id.maxPriceTextView)

        // Initialize sliders with default values
        minPriceSlider.value = 0f
        maxPriceSlider.value = 0f

        // Set up listeners to update TextViews based on slider values
        minPriceSlider.addOnChangeListener { slider, value, _ ->
            minPriceTextView.text = getString(R.string.min_price) + ": $value"
        }
        maxPriceSlider.addOnChangeListener { slider, value, _ ->
            maxPriceTextView.text = getString(R.string.max_price) + ": $value"
        }

        builder.setTitle(getString(R.string.price_range))
        val dialog: AlertDialog = builder.create()

        filterButton.setOnClickListener {
            val minPrice = minPriceSlider.value
            val maxPrice = maxPriceSlider.value
            if (minPrice == 0f && maxPrice == 0f) {
                Toast.makeText(
                    context,
                    "Minimum and Maximum Price cannot be equal zero at same time.",
                    Toast.LENGTH_SHORT
                )
                    .show()
                return@setOnClickListener
            }
            // Ensure minPrice is not greater than maxPrice
            if (minPrice > maxPrice) {
                Toast.makeText(context, "Minimum price cannot be greater than maximum price.", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            homeViewModel.getProductsBetweenRange(minPrice.toDouble(), maxPrice.toDouble())
            dialog.dismiss()
        }

        dialog.show()
    }


}