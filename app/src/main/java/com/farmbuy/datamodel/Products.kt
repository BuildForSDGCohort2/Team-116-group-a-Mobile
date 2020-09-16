package com.farmbuy.datamodel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

data class Products(
    val productName: String = "",
    val description: String = "",
    val units: String = "",
    val price: String = "",
    val farmersLoc: String = "",
    val imageUrl: String = "default",
    val farmersId: String = "",
    val dateUploaded: String = "",
    val phone: String = "",
    val buyerId: String = "",
    val productId: String = ""
) : Serializable