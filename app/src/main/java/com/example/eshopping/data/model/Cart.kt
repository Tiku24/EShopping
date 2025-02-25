package com.example.eshopping.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Cart(
    var cId : String = "",
    val name :String="",
    val price : String = "",
    val finalPrice : String = "",
    val description : String = "",
    val category : String = "",
    val quantity : Int = 0,
    val image: String = "",
    val date : Long = System.currentTimeMillis(),
    val createdBy:String="",
    val colors: String ="",
    val sizes: String =""
)