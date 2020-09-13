package com.farmbuy.datamodel

data class Products(
    val orderId: String,
    val productName: String,
    val description: String,
    val units: String,
    val price: String,
    val farmersLoc: String,
    val productImage: String,
    val farmersId:String,
    val dateUploaded:String,
    val phone:String
)