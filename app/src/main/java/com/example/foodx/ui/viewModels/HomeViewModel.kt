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
import com.example.foodx.models.CategoryMeals
import com.example.foodx.models.MealResponse
import com.example.foodx.repository.FoodRepository
import com.example.foodx.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class HomeViewModel(
    app: Application,
    val foodRepository: FoodRepository
) : AndroidViewModel(app) {

    val randomMealLiveData: MutableLiveData<Resource<MealResponse>> = MutableLiveData()
    var trendingMealLiveData: MutableLiveData<Resource<List<CategoryMeals>>> = MutableLiveData()

    init {
        getRandomMeal()
        getTrendingMeal("Seafood")
    }

    fun getRandomMeal() = viewModelScope.launch {
        safeRandomMealCall()
    }

    fun getTrendingMeal(categoryName: String) = viewModelScope.launch {
        safeTrendingMealCall(categoryName)
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


    private fun handleTrendingMealResponse(response: Response<CategoriesResponse>): Resource<List<CategoryMeals>> {
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