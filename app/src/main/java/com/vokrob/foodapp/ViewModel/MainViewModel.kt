package com.vokrob.foodapp.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.vokrob.foodapp.Model.CategoryModel
import com.vokrob.foodapp.Model.FoodModel
import com.vokrob.foodapp.Repository.MainRepository

class MainViewModel : ViewModel() {
    private val repository = MainRepository()

    fun loadCategory(): LiveData<MutableList<CategoryModel>> {
        return repository.loadCategory()
    }

    fun loadPopular(): LiveData<MutableList<FoodModel>> {
        return repository.loadPopular()
    }
}


























