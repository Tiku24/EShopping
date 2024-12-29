package com.example.eshopping.data.model

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
    val createdBy:String=""
)