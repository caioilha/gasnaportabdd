package com.gasnaporta.gasnaporta.Service

import com.gasnaporta.gasnaporta.Model.InAddSupplier
import com.gasnaporta.gasnaporta.Model.Score
import com.gasnaporta.gasnaporta.Model.Supplier
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface SupplierService {
    @GET("supplier/all?dist=10000")  // Pega até 10km de distâncias
    fun list(@Query("lat") lat: Double, @Query("long") long: Double): Call<List<Supplier>>

    @POST("supplier")
    fun addSupplier(@Body supplier: InAddSupplier): Call<Void>

    @POST("supplier/addScore")
    fun addScore(@Body scores: Score): Call<Void> //não dá pra ser só a string com o i
}