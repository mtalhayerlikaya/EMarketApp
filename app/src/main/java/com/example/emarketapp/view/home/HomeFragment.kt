package com.example.emarketapp.view.home

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.emarketapp.R
import com.example.emarketapp.adapter.ProductAdapter
import com.example.emarketapp.base.BaseFragment
import com.example.emarketapp.databinding.FragmentHomeBinding
import com.example.emarketapp.model.ProductListUIModel
import com.example.emarketapp.utils.Resource
import com.example.emarketapp.utils.Spinner
import com.example.emarketapp.utils.gone
import com.example.emarketapp.utils.visible
import com.example.emarketapp.view.MainActivity
import com.google.android.material.slider.Slider
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val homeViewModel: HomeViewModel by viewModels({ requireActivity() })
    private lateinit var mActivity: MainActivity
    private lateinit var adapter: ProductAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mActivity = activity as MainActivity
        observeFlow()
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

        binding.selectFilter.setOnClickListener {
            showFilterDialog()
        }

    }

    private fun observeFlow() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            homeViewModel.allProducts.collect() { result ->
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
                        val productList = result.result
                        if (productList.isEmpty()) binding.emptyLayout.visible()
                        else binding.emptyLayout.gone()
                        val badgeCount = productList.filter { it.basketItemCount > 0 }
                        mActivity.setBadge(badgeCount.size)
                        handleAdapter(productList.toMutableList())
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            homeViewModel.searchProductFlow.collect() { result ->
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
                        val productList = result.result
                        adapter.updateProductList(productList)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            homeViewModel.filterPriceRange.collect() { result ->
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
                        val productListBetweenRange = result.result
                        if (productListBetweenRange.isEmpty()) binding.emptyLayout.visible()
                        else binding.emptyLayout.gone()
                        adapter.updateProductList(productListBetweenRange)
                    }
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        if (!binding.homeSearchView.query.isNullOrEmpty()) {
            homeViewModel.searchProduct(binding.homeSearchView.query.toString())
        } else {
            homeViewModel.getProductList()
        }
    }


    private fun handleAdapter(list: MutableList<ProductListUIModel>) {
        adapter = ProductAdapter(mActivity, homeViewModel, list, {
            val productList = adapter.getAdapterList().filter { it.basketItemCount > 0 }
            mActivity.setBadge(productList.size)
        }) { productID ->
            val action = HomeFragmentDirections.homeFragmentToDetailFragment(productID)
            findNavController().navigate(action)
        }
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

        minPriceSlider.value = 0f
        maxPriceSlider.value = 0f

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