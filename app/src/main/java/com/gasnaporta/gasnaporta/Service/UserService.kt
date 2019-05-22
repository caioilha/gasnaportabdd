package com.gasnaporta.gasnaporta.Service

import com.gasnaporta.gasnaporta.Model.InAddUser
import com.gasnaporta.gasnaporta.Model.Supplier
import com.gasnaporta.gasnaporta.Model.SupplierId
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by 15104291 on 04/05/18.
 */
interface UserService {

    @GET("user/fav")
    fun listFavorites(@Header("Authorization") token: String): Call<List<Supplier>>

    @POST("user/fav")
    fun addFavorite(@Header("Authorization") token: String, @Body supplier: SupplierId): Call<Void> //não dá pra ser só a string com o id e nao sei como faz pra mapear o nome certo do objeto esperado pela api :(

    @HTTP(method = "DELETE", path = "user/fav", hasBody = true) //retrofit não aceita body annotation delete deles :)
    fun deleteFavorite(@Header("Authorization") token: String, @Body supplier: SupplierId): Call<Void>

    @POST("user")
    fun addUser(@Body user: InAddUser): Call<Void>
}


/*
router.get('/fav', auth.authenticate(), function(req, res){ // localhost:8080/suplier/all
    userServices.getFav(req, res);
})

router.post("/fav", auth.authenticate() , function(req, res){
    userServices.addFav(req, res);
})

router.delete("/fav", auth.authenticate()  ,  function(req, res){
    userServices.removeFav(req, res);
})

router.post('/', function(req, res){
    userServices.create(req, res);
})
*/
