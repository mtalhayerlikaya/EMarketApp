package com.example.emarketapp.view.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.emarketapp.base.BaseFragment
import com.example.emarketapp.databinding.FragmentHomeBinding
import com.example.emarketapp.utils.Resource
import com.example.emarketapp.utils.pksCollectResult
import com.example.emarketapp.view.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var mActivity: MainActivity

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mActivity = activity as MainActivity
        homeViewModel.getProductList()
        getProductList()
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
        }
    }


}