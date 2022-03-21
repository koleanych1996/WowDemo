package com.example.wowdemo.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wowdemo.Constants.TAG
import com.example.wowdemo.R
import com.example.wowdemo.databinding.FragmentProductsBinding
import com.example.wowdemo.model.Product
import com.example.wowdemo.viewModel.ProductsFragmentViewModel
import com.example.wowdemo.viewModel.ProductsFragmentViewState
import com.example.wowdemo.viewModel.ProductsStateEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview


@FlowPreview
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ProductsFragment : BaseFragment() {

    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProductsFragmentViewModel by viewModels()

    private var onlyFavourites: Boolean = false

    private var allProductsData: MutableList<Product> = mutableListOf()
    private var productsList: MutableList<Product> = mutableListOf()
    private lateinit var adapter: ProductsRecyclerViewAdapter

    private var currentPage = 1
    private var loading = false

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

        setupProductsRecyclerView()

        viewModel.setStateEvent(ProductsStateEvent.GetProductsStateEvent(currentPage))

        binding.listFormatRadioGroup.setOnCheckedChangeListener { _, _ ->

            if (binding.listFormatRadioBtn.isChecked) {
                val layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                binding.productsRecycler.layoutManager = layoutManager
            } else {

                Toast.makeText(requireContext(), "Coming soon", Toast.LENGTH_SHORT).show()

                // TODO: uncomment whet grid design appears
                /** val layoutManager =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
                binding.productsRecycler.layoutManager = layoutManager*/
            }
        }

        binding.goToFavouritesBtn.setOnClickListener {
            if (onlyFavourites) {
                onlyFavourites = false
                binding.goToFavouritesBtn.setImageResource(R.drawable.ic_heart)
                productsList.clear()
                productsList.addAll(allProductsData)
                adapter.notifyDataSetChanged()
            } else {
                onlyFavourites = true
                binding.goToFavouritesBtn.setImageResource(R.drawable.ic_heart_filed_white)
                productsList.clear()
                productsList.addAll(allProductsData.filter { it.isFavourite })
                adapter.notifyDataSetChanged()
            }
        }

        adapter.setClickListener(
            object : ProductsRecyclerViewAdapter.ItemClickListener {
                override fun onItemClick(view: View?, position: Int) {
                    val action = ProductsFragmentDirections
                        .actionProductsFragmentToProductDetailsFragment(productId = allProductsData[position].id)
                    findNavController().navigate(action)
                }
            }
        )

        adapter.setFavouriteClickListener(
            object : ProductsRecyclerViewAdapter.FavouriteClickListener {
                override fun onFavouriteClick(view: View?, position: Int) {
                    val productId = productsList[position].id
                    viewModel.setStateEvent(
                        ProductsStateEvent.SetProductFavouriteStateEvent(
                            productId
                        )
                    )
                }
            }
        )
    }

    override fun setupChannel() {
        viewModel.setupChannel()
    }

    override fun registerObservers() {
        viewModel.viewState.observe(viewLifecycleOwner) { viewState ->

            viewState.productsList?.let {

                allProductsData.clear()
                allProductsData.addAll(it)

                productsList.clear()
                if (onlyFavourites) {
                    productsList.addAll(it.filter { it.isFavourite })
                } else {
                    productsList.addAll(it)
                }

                adapter.notifyDataSetChanged()
                loading = false
            }

        }

        viewModel.stateMessage.observe(viewLifecycleOwner) { stateMessage ->
            stateMessage?.let {
                Log.d(
                    TAG,
                    "StateMessage in ${ProductsFragment::class.java.simpleName} = $stateMessage"
                )
                val message = it.response.message
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                viewModel.clearStateMessage()
            }
        }

    }

    private fun setupProductsRecyclerView() {

        currentPage = 1

        val linearLayoutManager = LinearLayoutManager(requireContext())
        binding.productsRecycler.layoutManager = linearLayoutManager
        adapter = ProductsRecyclerViewAdapter(requireContext(), productsList)
        binding.productsRecycler.adapter = adapter

        val dividerItemDecoration = DividerItemDecoration(
            binding.productsRecycler.context,
            (binding.productsRecycler.layoutManager as LinearLayoutManager).orientation
        )
        binding.productsRecycler.addItemDecoration(dividerItemDecoration)


        binding.productsRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) { //check for scroll down
                    val visibleItemCount = linearLayoutManager.childCount
                    val totalItemCount = linearLayoutManager.itemCount
                    val pastVisibleItems = linearLayoutManager.findFirstVisibleItemPosition()

                    if (loading) {
                        return
                    }

                    if (visibleItemCount + pastVisibleItems >= totalItemCount) {
                        loading = true
                        currentPage += 1
                        viewModel.setStateEvent(
                            stateEvent = ProductsStateEvent.GetProductsStateEvent(
                                page = currentPage
                            )
                        )
                    }
                }
            }
        })

    }


}