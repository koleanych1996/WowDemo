package com.example.wowdemo.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.wowdemo.Constants
import com.example.wowdemo.R
import com.example.wowdemo.databinding.FragmentProductDetailsBinding
import com.example.wowdemo.model.Product
import com.example.wowdemo.viewModel.ProductDetailsFragmentViewModel
import com.example.wowdemo.viewModel.ProductDetailsStateEvent
import com.example.wowdemo.viewModel.common.MessageType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ProductDetailsFragment : BaseFragment() {

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

        viewModel.setStateEvent(ProductDetailsStateEvent.GetProductStateEvent(args.productId))

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun setupChannel() {
        viewModel.setupChannel()
    }

    override fun registerObservers() {
        viewModel.viewState.observe(viewLifecycleOwner) { viewState ->

            viewState.product?.let {
                myProduct = it
                fillViewsWithData()
            }

        }

        viewModel.stateMessage.observe(viewLifecycleOwner) { stateMessage ->
            stateMessage?.let {
                Log.d(
                    Constants.TAG,
                    "StateMessage in ${ProductsFragment::class.java.simpleName} = $stateMessage"
                )
                val message = it.response.message
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                viewModel.clearStateMessage()
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
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(binding.productIv)

        }
    }

}