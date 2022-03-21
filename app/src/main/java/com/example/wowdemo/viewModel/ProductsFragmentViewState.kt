package com.example.wowdemo.viewModel

import com.example.wowdemo.model.Product

data class ProductsFragmentViewState(
    var productsList: List<Product>? = null,
    var page: Int = 1
)