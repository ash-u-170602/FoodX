package com.example.foodx.ui.viewModels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.foodx.FoodApplication
import com.example.foodx.models.CategoriesResponse
import com.example.foodx.models.Category
import com.example.foodx.models.CategoryList
import com.example.foodx.models.CategoryMeals
import com.example.foodx.models.Cuisine
import com.example.foodx.models.CuisineList
import com.example.foodx.models.Meal
import com.example.foodx.models.MealResponse
import com.example.foodx.repository.FoodRepository
import com.example.foodx.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import kotlin.random.Random

class HomeViewModel(
    app: Application,
    private val foodRepository: FoodRepository
) : AndroidViewModel(app) {

    val randomMealLiveData: MutableLiveData<Resource<MealResponse>> = MutableLiveData()
    var trendingMealLiveData: MutableLiveData<Resource<List<CategoryMeals>>> = MutableLiveData()
    var cuisineMealLiveData: MutableLiveData<Resource<List<CategoryMeals>>> = MutableLiveData()
    val mealDetailLiveData: MutableLiveData<Resource<Meal>> = MutableLiveData()
    val searchMealLiveData: MutableLiveData<Resource<List<Meal>>> = MutableLiveData()
    val categoriesLiveData: MutableLiveData<Resource<List<Category>>> = MutableLiveData()
    val cuisinesLiveData: MutableLiveData<Resource<List<Cuisine>>> = MutableLiveData()
    val mealByCategoryLiveData: MutableLiveData<Resource<List<CategoryMeals>>> = MutableLiveData()
    val favouritesMealLiveData = getSavedMeal()

    private val _strMeal = MutableLiveData<String>()
    val strMeal: MutableLiveData<String> = _strMeal

    fun setStrMeal(data: String) {
        _strMeal.value = data
    }

    private val _strArea = MutableLiveData<String>()
    val strArea: MutableLiveData<String> = _strArea

    fun setArea(data: String) {
        _strArea.value = data
    }

    init {

        when(Random.nextInt(1, 6)){
            1 -> getTrendingMeal("Beef")
            2 -> getTrendingMeal("Breakfast")
            3 -> getTrendingMeal("Chicken")
            4 -> getTrendingMeal("Pork")
            5 -> getTrendingMeal("Vegetarian")
            6 -> getTrendingMeal("Dessert")
        }

        getRandomMeal()
        getCategories()
        getCuisines()
    }

    fun saveMeal(meal: CategoryMeals) = viewModelScope.launch {
        foodRepository.upsert(meal)
    }

    private fun getSavedMeal() = foodRepository.getSavedMeals()

    fun deleteMeal(meal: CategoryMeals) = viewModelScope.launch {
        foodRepository.deleteMeal(meal)
    }

    fun getCategories() = viewModelScope.launch {
        safeCategoriesCall()
    }

    fun getCuisines() = viewModelScope.launch {
        safeCuisinesCall()
    }


    fun getMealDetails(id: String) = viewModelScope.launch {
        safeMealDetailsCall(id)
    }

    fun searchMeals(searchQuery: String) = viewModelScope.launch {
        safeSearchMealsCall(searchQuery)
    }

    fun getRandomMeal() = viewModelScope.launch {
        safeRandomMealCall()
    }

    private fun getTrendingMeal(categoryName: String) = viewModelScope.launch {
        safeTrendingMealCall(categoryName)
    }

    fun getCuisineMeal(cuisineName: String) = viewModelScope.launch {
        safeCuisineMealCall(cuisineName)
    }

    fun getMealsByCategory(categoryName: String) = viewModelScope.launch {
        safeMealsByCategoryCall(categoryName)
    }

    private suspend fun safeMealsByCategoryCall(categoryName: String) {
        mealByCategoryLiveData.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = foodRepository.getMealsByCategory(categoryName)
                mealByCategoryLiveData.postValue(handleMealByCategoryResponse(response))
            } else {
                mealByCategoryLiveData.postValue(Resource.Error("No Internet Connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> mealByCategoryLiveData.postValue(Resource.Error("Network Failure"))
                else -> mealByCategoryLiveData.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private suspend fun safeMealDetailsCall(id: String) {
        mealDetailLiveData.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = foodRepository.getMealDetails(id)
                mealDetailLiveData.postValue(handleMealDetailResponse(response))
            } else {
                mealDetailLiveData.postValue(Resource.Error("No Internet Connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> mealDetailLiveData.postValue(Resource.Error("Network Failure"))
                else -> mealDetailLiveData.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private suspend fun safeSearchMealsCall(searchQuery: String) {
        searchMealLiveData.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = foodRepository.searchMeals(searchQuery)
                searchMealLiveData.postValue(handleSearchMealResponse(response))
            } else {
                searchMealLiveData.postValue(Resource.Error("No Internet Connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> searchMealLiveData.postValue(Resource.Error("Network Failure"))
                else -> searchMealLiveData.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private suspend fun safeTrendingMealCall(categoryName: String) {
        trendingMealLiveData.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = foodRepository.getTrendingMeal(categoryName)
                trendingMealLiveData.postValue(handleTrendingMealResponse(response))
            } else {
                trendingMealLiveData.postValue(Resource.Error("No Internet Connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> trendingMealLiveData.postValue(Resource.Error("Network Failure"))
                else -> trendingMealLiveData.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private suspend fun safeCuisineMealCall(countryName: String) {
        cuisineMealLiveData.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = foodRepository.getCuisineMeal(countryName)
                cuisineMealLiveData.postValue(handleCuisineMealResponse(response))
            } else {
                cuisineMealLiveData.postValue(Resource.Error("No Internet Connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> cuisineMealLiveData.postValue(Resource.Error("Network Failure"))
                else -> cuisineMealLiveData.postValue(Resource.Error("Conversion Error"))
            }
        }
    }


    private suspend fun safeRandomMealCall() {
        randomMealLiveData.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = foodRepository.getRandomMeal()
                randomMealLiveData.postValue(handleRandomMealResponse(response))
            } else {
                randomMealLiveData.postValue(Resource.Error("No Internet Connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> randomMealLiveData.postValue(Resource.Error("Network Failure"))
                else -> randomMealLiveData.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private suspend fun safeCategoriesCall() {
        categoriesLiveData.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = foodRepository.getCategories()
                categoriesLiveData.postValue(handleCategoryResponse(response))
            } else {
                categoriesLiveData.postValue(Resource.Error("No Internet Connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> categoriesLiveData.postValue(Resource.Error("Network Failure"))
                else -> categoriesLiveData.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private suspend fun safeCuisinesCall() {
        cuisinesLiveData.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = foodRepository.getCuisines()
                cuisinesLiveData.postValue(handleCuisinesResponse(response))
            } else {
                cuisinesLiveData.postValue(Resource.Error("No Internet Connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> cuisinesLiveData.postValue(Resource.Error("Network Failure"))
                else -> cuisinesLiveData.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun handleCategoryResponse(response: Response<CategoryList>): Resource<List<Category>> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse.categories)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleCuisinesResponse(response: Response<CuisineList>): Resource<List<Cuisine>> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse.meals)
            }
        }
        return Resource.Error(response.message())
    }


    private fun handleTrendingMealResponse(response: Response<CategoriesResponse>): Resource<List<CategoryMeals>> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse.meals)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleCuisineMealResponse(response: Response<CategoriesResponse>): Resource<List<CategoryMeals>> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse.meals)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleMealByCategoryResponse(response: Response<CategoriesResponse>): Resource<List<CategoryMeals>> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse.meals)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleRandomMealResponse(response: Response<MealResponse>): Resource<MealResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleMealDetailResponse(response: Response<MealResponse>): Resource<Meal> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse.meals[0])
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchMealResponse(response: Response<MealResponse>): Resource<List<Meal>> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse.meals)
            }
        }
        return Resource.Error(response.message())
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<FoodApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }


}