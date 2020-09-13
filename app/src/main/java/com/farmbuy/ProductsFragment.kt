package com.farmbuy

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.farmbuy.adapters.OnUserClick
import com.farmbuy.adapters.ProductsAdapter
import com.farmbuy.buyer.ui.OrderActivity
import com.farmbuy.datamodel.Order
import com.farmbuy.datamodel.Products


class ProductsFragment : Fragment(), OnUserClick {

    private lateinit var recyclerView: RecyclerView
    private lateinit var productsList: MutableList<Products>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_products, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerview)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity) as RecyclerView.LayoutManager?

        }

        productsList = mutableListOf()
        recyclerView.setHasFixedSize(true)


        val adapter = ProductsAdapter(
            productsList,
            this
        )
        recyclerView.adapter = adapter
        // progressBar.visibility = View.GONE
        adapter.notifyDataSetChanged()
    }

    override fun onUserClick(products: Products, position: Int) {
        val intent = Intent(activity, OrderActivity::class.java)
        val bundle = bundleOf()
        intent.putExtra("product", bundle)
        startActivity(intent)
    }


}