package com.gasnaporta.gasnaporta.Service

import com.gasnaporta.gasnaporta.Model.HistoricSupplier
import com.gasnaporta.gasnaporta.Model.InOrder
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * Created by 15104291 on 04/05/18.
 */
interface HistoricService {

    @GET("order/all?populate=true")
    fun list(@Header("Authorization") token: String): Call<List<HistoricSupplier>>

    @POST("order")
    fun save(@Header("Authorization") token: String, @Body inOrder: InOrder): Call<Void>


}