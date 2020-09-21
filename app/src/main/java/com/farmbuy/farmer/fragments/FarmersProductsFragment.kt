package com.farmbuy.farmer.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.farmbuy.R
import com.farmbuy.adapters.OnUserClick
import com.farmbuy.adapters.ProductsAdapter
import com.farmbuy.datamodel.Products
import com.farmbuy.farmer.CreateOrderActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_farmers_products.*
import kotlinx.android.synthetic.main.activity_farmers_products.fab
import kotlinx.android.synthetic.main.fragment_farmers_products.*


class FarmersProductsFragment : Fragment(),OnUserClick {
    private lateinit var recyclerView: RecyclerView
    private lateinit var productsList: MutableList<Products>
    var dbRef = Firebase.firestore.collection("Products")



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_farmers_products, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        fab.setOnClickListener {
            val intent = Intent(activity, CreateOrderActivity::class.java)
            startActivity(intent)
        }


        recyclerView = view.findViewById(R.id.recyclerview)
        productsList = mutableListOf()
        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity) as RecyclerView.LayoutManager?
            setHasFixedSize(true)
        }
        getProducts()

    }

    private fun getProducts()
    {
        val farmerId = FirebaseAuth.getInstance().currentUser?.uid
        dbRef.whereEqualTo("farmersId",farmerId)
            .addSnapshotListener{ value: QuerySnapshot?, error: FirebaseFirestoreException? ->
            error?.let {
                Toast.makeText(activity,"Sorry cant get Products at this time", Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }

            value?.let {
                for(documents in value.documents)
                {
                    val products = documents.toObject<Products>()
                    if (products != null) {
                        productsList.add(products)
                        Log.d("gads",productsList.toString())
                        Log.d("gads","DATA RECEIVED")

                        val adapter = ProductsAdapter(productsList, this)
                        Log.d("gads","ADAPTER SETTED")
                        recyclerView.adapter = adapter
                        adapter.notifyDataSetChanged()
                    }
                    else{
                        noproduct.visibility = View.VISIBLE
                    }

                }
            }


        }


    }

    override fun onUserClick(products: Products, position: Int) {
        val bundle = Bundle().apply {
            putSerializable("product",products)
        }

        findNavController().navigate(R.id.action_farmersProductsFragment_to_editFarmerOrderActivity,bundle)
    }
}