package com.example.wowdemo.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.wowdemo.R
import com.example.wowdemo.databinding.FragmentProductDetailsBinding
import com.example.wowdemo.model.Product
import com.example.wowdemo.viewModel.ProductDetailsFragmentViewModel
import com.example.wowdemo.viewModel.ProductDetailsStateEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {

    private var _binding: FragmentProductDetailsBinding? = null
    private val binding get() = _binding!!

    private val args: ProductDetailsFragmentArgs by navArgs()

    private val viewModel: ProductDetailsFragmentViewModel by viewModels()

    private var myProduct: Product? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeObservers()

        viewModel.setStateEvent(ProductDetailsStateEvent.GetProductStateEvent(args.productId))

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun subscribeObservers() {
        viewModel.viewState.observe(viewLifecycleOwner) { viewState ->

            viewState.product?.let {
                myProduct = it
                fillViewsWithData()
            }

        }
    }

    private fun fillViewsWithData() {

        myProduct?.let {
            val priceStr = "$ ${it.price},-"

            binding.productTitle.text = it.name
            binding.productDetails.text = it.details
            binding.productPrice.text = priceStr
            binding.productPrice2.text = priceStr
            binding.informationTextTv.text = it.details

            Glide.with(requireContext())
                .load(it.category.icon)
                .placeholder(R.drawable.ic_logo)
                .circleCrop()
                .into(binding.productIv)

        }
    }

}