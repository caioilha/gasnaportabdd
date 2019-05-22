package com.gasnaporta.gasnaporta.Activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import com.gasnaporta.gasnaporta.Adapter.SuppliersAdapter
import com.gasnaporta.gasnaporta.Model.Supplier
import com.gasnaporta.gasnaporta.R
import com.gasnaporta.gasnaporta.Util.UserUtil
import com.gasnaporta.gasnaporta.WebClient.UserWebClient
import kotlinx.android.synthetic.main.activity_favoritos.*
import java.util.*


class Favorites : AppCompatActivity() {

    private val suppliers: ArrayList<Supplier> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favoritos)

        val actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)
        actionBar.title = "Favoritos"

        listSuppliers()

    }
    private fun listSuppliers() {

        UserWebClient().listFavorites(UserUtil().getToken(this), {

            suppliers.clear()
            suppliers.addAll(it)
            suppliers.forEach{it.isFavorite = true}

            if(suppliers.isEmpty()){
                placeholder.visibility= View.VISIBLE
            }
            Log.d("testeFavoritos", suppliers.toString())
            //  textView2.setText(suppliers.toString())
            initAdapter(suppliers)

        })
    }
    private fun initAdapter(suppliers: List<Supplier>) {
        mainRecyclerView.layoutManager = LinearLayoutManager(this)
        mainRecyclerView.adapter = SuppliersAdapter(suppliers, this)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onResume() {
        listSuppliers()
        super.onResume()

    }
}