package com.example.emarketapp.view.detail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.emarketapp.R
import com.example.emarketapp.base.BaseFragment
import com.example.emarketapp.databinding.FragmentDetailBinding
import com.example.emarketapp.model.ProductListUIModel
import com.example.emarketapp.utils.Resource
import com.example.emarketapp.utils.Spinner
import com.example.emarketapp.utils.handleOnBackPressedExt
import com.example.emarketapp.view.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailFragment : BaseFragment<FragmentDetailBinding>(FragmentDetailBinding::inflate) {
    private val detailViewModel: DetailViewModel by viewModels()
    private lateinit var mActivity: MainActivity

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mActivity = activity as MainActivity
        initUI()
        val bundle: DetailFragmentArgs by navArgs()
        val productID = bundle.productID
        detailViewModel.getProductDetail(productID)
        observeFlow()
    }


    private fun initUI() {
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        this@DetailFragment.handleOnBackPressedExt(mActivity) {
            findNavController().popBackStack()
        }
    }

    private fun observeFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                detailViewModel.productDetail.collect { result ->
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
                            val detailProduct = result.result
                            handleResponse(detailProduct)
                        }
                    }
                }
            }
        }
    }

    private fun updateFavoriteState(product: ProductListUIModel) {
        if (product.isFavorite)
            binding.favAddToFavorite.setImageDrawable(
                AppCompatResources.getDrawable(
                    mActivity,
                    R.drawable.icon_star_liked
                )
            )
        else
            binding.favAddToFavorite.setImageDrawable(
                AppCompatResources.getDrawable(
                    mActivity,
                    R.drawable.icon_star_white
                )
            )
    }

    private fun updateButtonState(product: ProductListUIModel) {
        if (product.basketItemCount != 0)
            binding.btnAddToCard.text = getString(R.string.remove_from_basket_button)
        else
            binding.btnAddToCard.text = getString(R.string.add_to_basket_button)
    }

    private fun handleResponse(product: ProductListUIModel) {
        binding.detailHeaderTv.text = product.name
        binding.detailDescriptionTv.text = product.description
        binding.detailTotalPrice.text = "${product.price}₺"

        updateButtonState(product)

        updateFavoriteState(product)

        binding.btnAddToCard.setOnClickListener {
            product.basketItemCount = if (product.basketItemCount != 0) 0 else 1
            updateButtonState(product)
            detailViewModel.updateProduct(product)
        }
        binding.favAddToFavorite.setOnClickListener {
            product.isFavorite = !product.isFavorite
            updateFavoriteState(product)
            detailViewModel.updateProduct(product)
        }

        Glide.with(mActivity)
            .load(product.image)
            .into(binding.detailImageView)

    }

}