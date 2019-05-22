package com.gasnaporta.gasnaporta.Service

import com.gasnaporta.gasnaporta.Model.Gas
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by 15104291 on 04/05/18.
 */
interface GasService {
    @GET("gas/{id}") //a url pode ser diferente
    fun list(@Path("id") id: String): Call<List<Gas>>
}