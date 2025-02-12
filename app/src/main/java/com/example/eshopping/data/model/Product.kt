package com.example.eshopping.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    var pId : String = "",
    val name :String="",
    val price : String = "",
    val finalPrice : String = "",
    val description : String = "",
    val category : String = "",
    val image: String = "",
//    val isAvailable : Boolean = true,
    val availableUnits : Int = 0,
    val date : Long = System.currentTimeMillis(),
    val createdBy:String="",
    val colors: List<String> = emptyList(),
    val sizes: List<String> = emptyList()
)