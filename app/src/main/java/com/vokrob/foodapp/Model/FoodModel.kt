package com.vokrob.foodapp.Model

import java.io.Serializable

data class FoodModel(
    var title: String = "",
    var picUrl: String = "",
    var description: String = "",
    var price: Double = 0.0,
    var categoryId: String = "",
    var numberInCart: Int = 0,
) : Serializable





















