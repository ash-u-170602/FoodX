package com.example.foodx.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.foodx.FoodApplication
import com.example.foodx.models.MealResponse
import com.example.foodx.repository.FoodRepository
import com.example.foodx.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class FoodViewModel(
    app: Application,
    val foodRepository: FoodRepository
) : AndroidViewModel(app) {

    val randomMeal: MutableLiveData<Resource<MealResponse>> = MutableLiveData()

    init {
        getRandomMeal()
    }

    fun getRandomMeal() = viewModelScope.launch {
        safeRandomMealCall()
    }


    private suspend fun safeRandomMealCall() {
        randomMeal.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = foodRepository.getRandomMeal()
                randomMeal.postValue(handleRandomMealResponse(response))
            } else {
                randomMeal.postValue(Resource.Error("No Internet Connection"))
            }
        } catch (t: Throwable){
            when (t){
                is IOException -> randomMeal.postValue(Resource.Error("Network Failure"))
                else -> randomMeal.postValue(Resource.Error("Conversion Error"))
            }
        }
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