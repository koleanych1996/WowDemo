package com.example.wowdemo.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wowdemo.databinding.FragmentProductsBinding
import com.example.wowdemo.model.Product
import com.example.wowdemo.viewModel.ProductsFragmentViewModel
import com.example.wowdemo.viewModel.ProductsStateEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ProductsFragment : Fragment() {

    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProductsFragmentViewModel by viewModels()

    private var productsList: MutableList<Product> = mutableListOf()
    private lateinit var adapter: ProductsRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeObservers()

        setupProductsRecyclerView()

        viewModel.setStateEvent(ProductsStateEvent.PingProductsStateEvent)
    }

    private fun setupProductsRecyclerView() {
        binding.productsRecycler.layoutManager = LinearLayoutManager(requireContext())
        adapter = ProductsRecyclerViewAdapter(requireContext(), productsList)
        binding.productsRecycler.adapter = adapter
    }

    private fun subscribeObservers() {
        viewModel.viewState.observe(viewLifecycleOwner) { viewState ->

            viewState.productsList?.let {
                productsList.clear()
                productsList.addAll(it)
                adapter.notifyDataSetChanged()
            }

        }
    }

}