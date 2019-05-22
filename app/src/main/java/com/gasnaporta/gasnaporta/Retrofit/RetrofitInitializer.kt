package com.gasnaporta.gasnaporta.Retrofit

import com.gasnaporta.gasnaporta.Service.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInitializer {

    private val retrofit = Retrofit.Builder()
            .baseUrl(ConnectionProperties.SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    fun supplierService() = retrofit.create(SupplierService::class.java)

    fun favoriteService() = retrofit.create(LoginService::class.java)

    fun historicService() = retrofit.create(HistoricService::class.java)

    fun gasService() = retrofit.create(GasService::class.java)

    fun userService() = retrofit.create(UserService::class.java)

    fun loginService() = retrofit.create(LoginService::class.java)
}