package com.gasnaporta.gasnaporta.Service

import com.gasnaporta.gasnaporta.Model.InLogin
import com.gasnaporta.gasnaporta.Model.OutLogin
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by 15104291 on 04/05/18.
 */
interface LoginService {
    @POST("login")
    fun login(@Body inLogin: InLogin): Call<OutLogin>


/*
    Efetua login

    {
        email: {type: String, required: true },
        password: {type: String, required: true }
    }
    retorna

    return res.json({
        success: true,
        token: token,
        user: user
    });*/
}