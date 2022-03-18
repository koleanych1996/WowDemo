package com.example.wowdemo.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wowdemo.R
import com.example.wowdemo.model.Product

class ProductsRecyclerViewAdapter internal constructor(context: Context, data: List<Product>) :
    RecyclerView.Adapter<ProductsRecyclerViewAdapter.ViewHolder>() {
    private val mContext: Context = context
    private val mData: List<Product> = data
    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private var mClickListener: ItemClickListener? = null

    // inflates the row layout from xml when needed
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = mInflater.inflate(R.layout.item_products, parent, false)
        return ViewHolder(view)
    }

    // binds the data to the TextView in each row
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = mData[position]

        val priceStr = "$ ${product.price},-"

        holder.productTitle.text = product.name
        holder.productDetails.text = product.details
        holder.productPrice.text = priceStr
        holder.productPrice2.text = priceStr

        Glide.with(mContext)
            .load(product.category.icon)
            .placeholder(R.drawable.ic_logo)
            .circleCrop()
            .into(holder.productIV)
    }

    // total number of rows
    override fun getItemCount(): Int {
        return mData.size
    }

    // stores and recycles views as they are scrolled off screen
    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var productTitle: TextView = itemView.findViewById(R.id.product_title)
        var productDetails: TextView = itemView.findViewById(R.id.product_details)
        var productPrice: TextView = itemView.findViewById(R.id.product_price)
        var productPrice2: TextView = itemView.findViewById(R.id.product_price_2)
        var productIV: ImageView = itemView.findViewById(R.id.product_iv)
        override fun onClick(view: View?) {
            if (mClickListener != null) mClickListener!!.onItemClick(view, adapterPosition)
        }

        init {
            itemView.setOnClickListener(this)
        }
    }

    // convenience method for getting data at click position
    fun getItem(id: Int): Product {
        return mData[id]
    }

    // allows clicks events to be caught
    fun setClickListener(itemClickListener: ItemClickListener?) {
        mClickListener = itemClickListener
    }

    // parent activity will implement this method to respond to click events
    interface ItemClickListener {
        fun onItemClick(view: View?, position: Int)
    }

}