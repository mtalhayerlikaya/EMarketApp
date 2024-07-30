package com.example.emarketapp.view.favorite

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.emarketapp.adapter.FavoriteAdapter
import com.example.emarketapp.base.BaseFragment
import com.example.emarketapp.databinding.FragmentFavoriteBinding
import com.example.emarketapp.model.ProductListUIModel
import com.example.emarketapp.utils.Resource
import com.example.emarketapp.utils.Spinner
import com.example.emarketapp.view.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteFragment : BaseFragment<FragmentFavoriteBinding>(FragmentFavoriteBinding::inflate) {
    private val favViewModel: FavoriteViewModel by viewModels()
    private lateinit var adapter: FavoriteAdapter
    private lateinit var mActivity: MainActivity

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mActivity = activity as MainActivity
        observeFlow()
    }

    private fun observeFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                favViewModel.favProducts.collect { result ->
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
                            val favList = result.result
                            handleAdapter(favList.toMutableList())
                        }
                    }
                }
            }
        }
    }


    override fun onResume() {
        super.onResume()
        favViewModel.getFavProductList()
    }

    private fun handleAdapter(list: MutableList<ProductListUIModel>) {
        adapter = FavoriteAdapter(mActivity, favViewModel, list) { productID ->
            val action = FavoriteFragmentDirections.favoriteFragmentToDetailFragment(productID)
            findNavController().navigate(action)
        }
        binding.favRv.adapter = adapter
        binding.favRv.layoutManager = GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false)
    }
}


