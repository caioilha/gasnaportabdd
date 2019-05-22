package com.gasnaporta.gasnaporta.WebClient

import android.util.Log
import com.gasnaporta.gasnaporta.Model.HistoricSupplier
import com.gasnaporta.gasnaporta.Model.InOrder
import com.gasnaporta.gasnaporta.Model.Supplier
import com.gasnaporta.gasnaporta.Retrofit.RetrofitInitializer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoricWebClient {


    fun list(token: String, success: (suppliers: List<Supplier>) -> Unit) {
        val call = RetrofitInitializer().historicService().list(token)
        println("entrou aqui")

        call.enqueue(object : Callback<List<HistoricSupplier>?> {
            override fun onResponse(call: Call<List<HistoricSupplier>?>?,
                                    response: Response<List<HistoricSupplier>?>?) {
                println("success")
                println(response?.body())
                response?.body()?.let {
                    println(it)
                    val suppliers = it.map { it.supplier }
                            .filter{it != null}
                    success(suppliers)
                }
            }

            override fun onFailure(call: Call<List<HistoricSupplier>?>?,
                                   t: Throwable?) {
                Log.e("onFailure error", t?.message)
            }
        })
    }

    fun save(token: String, inOrder: InOrder, success: () -> Unit) {
        val call = RetrofitInitializer().historicService().save(token, inOrder)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>?,
                                    response: Response<Void>?) {
                success()
            }

            override fun onFailure(call: Call<Void>?,
                                   t: Throwable?) {
                Log.e("onFailure error", t?.message)
            }
        })
    }
}