package com.farmbuy.datamodel

data class User(
    val username: String = "",
    val userTpe: String = "",
    val email: String = "",
    val phone_number:String = "",
    val address:String = "",
    val profileImage: String = "default"
)