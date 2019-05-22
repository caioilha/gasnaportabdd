package com.gasnaporta.gasnaporta.WebClient

import android.support.design.widget.Snackbar
import android.util.Log
import android.view.View
import com.gasnaporta.gasnaporta.Model.*
import com.gasnaporta.gasnaporta.Retrofit.RetrofitInitializer
import kotlinx.android.synthetic.main.activity_login.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * Created by 15104291 on 04/05/18.
 */
class UserWebClient {

    fun loginCadastro(login: InLogin, success: (login: OutLogin) -> Unit) {
        val call = RetrofitInitializer().loginService().login(login)

        call.enqueue(object : Callback<OutLogin?> {

            override fun onResponse(call: Call<OutLogin?>?,
                                    response: Response<OutLogin?>?) {
                response?.body()?.let {
                    success(it)
                }
            }

            override fun onFailure(call: Call<OutLogin?>?,
                                   t: Throwable?) {
                Log.e("onFailure error", t?.message)
            }
        })
    }

    fun login(viewLogin: View, login: InLogin, success: (login: OutLogin) -> Unit) {
        val call = RetrofitInitializer().loginService().login(login)

        call.enqueue(object : Callback<OutLogin?> {

            override fun onResponse(call: Call<OutLogin?>?,
                                    response: Response<OutLogin?>?) {
                response?.body()?.let {
                    success(it)
                }
            }

            override fun onFailure(call: Call<OutLogin?>?,
                                   t: Throwable?) {
                Log.e("onFailure error", t?.message)
                viewLogin.progressLayout2.visibility=View.INVISIBLE
                viewLogin.layoutLogin.visibility=View.VISIBLE
                Snackbar.make(viewLogin, "Não foi possível se conectar ao servidor.", Snackbar.LENGTH_LONG).show()
            }
        })
    }

    fun addUser(user: InAddUser, success: (success: Boolean) -> Unit) {
        val call = RetrofitInitializer().userService().addUser(user)

        call.enqueue(object : Callback<Void?> {

            override fun onResponse(call: Call<Void?>?,
                                    response: Response<Void?>?) {

                if (response?.code() != 200) {
                    success(false)
                } else {
                    success(true) //cadastro deu bom
                }
                Log.e("awtwtwootwo", response.toString())
            }


            override fun onFailure(call: Call<Void?>?,
                                   t: Throwable?) {
                Log.e("onFailure error", t?.message)
            }
        })
    }

    fun listFavorites(token: String, success: (suppliers: List<Supplier>) -> Unit) {
        val call = RetrofitInitializer().userService().listFavorites(token)

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
                Log.e("onFailure error", t?.message)
            }
        })
    }

    fun addFavorite(token: String, supplier: SupplierId, success: () -> Unit) {
        val call = RetrofitInitializer().userService().addFavorite(token, supplier)

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

    fun deleteFavorite(token: String, supplier: SupplierId, success: () -> Unit) {
        val call = RetrofitInitializer().userService().deleteFavorite(token, supplier)

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