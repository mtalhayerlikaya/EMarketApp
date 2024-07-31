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
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.emarketapp.R
import com.example.emarketapp.adapter.ProductAdapter
import com.example.emarketapp.base.BaseFragment
import com.example.emarketapp.databinding.FragmentHomeBinding
import com.example.emarketapp.utils.Spinner
import com.example.emarketapp.view.MainActivity
import com.google.android.material.slider.Slider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val homeViewModel: HomeViewModel by viewModels({ requireActivity() })
    private lateinit var mActivity: MainActivity
    private lateinit var adapter: ProductAdapter

    private var searchJob: Job? = null
    private var filterJob: Job? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mActivity = activity as MainActivity
        //mActivity.setBadge()
        observeFlow()
        initUI()
        //  homeViewModel.getProductList()
    }

    private fun initUI() {
        binding.homeSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchJob?.cancel()
                searchJob = lifecycleScope.launch(Dispatchers.IO) {
                    delay(300) // Add a delay for debouncing
                    newText?.let { query ->
                        homeViewModel.searchProduct(query).collect { result ->
                            withContext(Dispatchers.Main) {
                                adapter.submitData(result)
                            }
                        }
                    }
                }
                return true
            }
        })

        binding.selectFilter.setOnClickListener {
            showFilterDialog()
        }
    }

    private fun observeFlow() {
        adapter = ProductAdapter(mActivity, homeViewModel) { productID ->
            val action = HomeFragmentDirections.homeFragmentToDetailFragment(productID)
            findNavController().navigate(action)
        }
        binding.rv.adapter = adapter
        binding.rv.layoutManager = GridLayoutManager(mActivity, 2, LinearLayoutManager.VERTICAL, false)

        viewLifecycleOwner.lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadState ->
                when {
                    loadState.refresh is LoadState.Loading || loadState.append is LoadState.Loading -> {
                        Spinner.show(requireContext())
                    }

                    loadState.refresh is LoadState.NotLoading || loadState.append is LoadState.NotLoading -> {
                        Spinner.hide()
                    }

                    loadState.refresh is LoadState.Error -> {
                        Spinner.hide()
                        Toast.makeText(
                            requireContext(),
                            (loadState.refresh as LoadState.Error).error.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    loadState.append is LoadState.Error -> {
                        Spinner.hide()
                        Toast.makeText(
                            requireContext(),
                            (loadState.append as LoadState.Error).error.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            homeViewModel.products.collect { result ->
                adapter.submitData(result)
            }
        }
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
            filterJob?.cancel()
            filterJob = lifecycleScope.launch(Dispatchers.IO) {
                delay(300) // Add a delay for debouncing
                homeViewModel.getProductsBetweenRange(minPrice.toDouble(), maxPrice.toDouble()).collect { result ->
                    withContext(Dispatchers.Main) {
                        adapter.submitData(result)
                    }
                }
            }
            dialog.dismiss()
        }

        dialog.show()
    }


}