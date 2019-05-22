package com.gasnaporta.gasnaporta.WebClient

import android.support.design.widget.Snackbar
import android.view.View
import com.gasnaporta.gasnaporta.Model.InAddSupplier
import com.gasnaporta.gasnaporta.Model.Score
import com.gasnaporta.gasnaporta.Model.Supplier
import com.gasnaporta.gasnaporta.Retrofit.RetrofitInitializer
import kotlinx.android.synthetic.main.content_main.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SupplierWebClient {

    fun list(viewMain: View, lat: Double, long: Double, success: (suppliers: List<Supplier>) -> Unit) {
        val call = RetrofitInitializer().supplierService().list(lat, long)

        call.enqueue(object : Callback<List<Supplier>?> {
            override fun onResponse(call: Call<List<Supplier>?>?,
                                    response: Response<List<Supplier>?>?) {
                response?.body()?.let {
                    val suppliers = it
                    success(suppliers)

                }
            }

            override fun onFailure(call: Call<List<Supplier>?>?,
                                   t: Throwable?) {
                Snackbar.make(viewMain, "Sem conexão", Snackbar.LENGTH_LONG).show()
                viewMain.progressLayout.visibility = View.GONE
                viewMain.swipeContainer.visibility = View.VISIBLE
            }
        })
    }

    fun addSupplier(supplier: InAddSupplier, success: () -> Unit) {
        val call = RetrofitInitializer().supplierService().addSupplier(supplier)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>?,
                                    response: Response<Void>?) {
                success()
            }

            override fun onFailure(call: Call<Void>?,
                                   t: Throwable?) {
                //Log.e("onFailure error", t?.message)
            }
        })
    }

    fun addScore(score: Score, success: () -> Unit) {
        val call = RetrofitInitializer().supplierService().addScore(score)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>?,
                                    response: Response<Void>?) {
                success()
            }

            override fun onFailure(call: Call<Void>?,
                                   t: Throwable?) {
                //Log.e("onFailure error", t?.message)
            }
        })
    }
}