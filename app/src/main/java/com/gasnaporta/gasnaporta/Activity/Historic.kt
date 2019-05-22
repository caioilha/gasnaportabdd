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
import com.gasnaporta.gasnaporta.WebClient.HistoricWebClient
import com.gasnaporta.gasnaporta.WebClient.UserWebClient
import kotlinx.android.synthetic.main.activity_historico.*
import java.util.*

class Historic : AppCompatActivity() {

    private val suppliers: ArrayList<Supplier> = arrayListOf()
    private val idFavorites: ArrayList<String> = arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historico)
        val actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)
        actionBar.title = "Histórico"


        //Exemplo cadastrar "order"
        //val inOrder = InOrder(UserUtil().getUserId(this), "5afe1c98ce19af408cc89388")
        //HistoricWebClient().save(UserUtil().getToken(this), inOrder, {
        //   Toast.makeText(this, "5afce9914ce7b50b66dd6db2 adiocinado historico", Toast.LENGTH_SHORT).show()
        //})


        listSuppliers()


        initAdapter(suppliers)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


    private fun listSuppliers() {
        HistoricWebClient().list(UserUtil().getToken(this), {
            suppliers.clear()
            suppliers.addAll(filterSuppliers(it))

            val random = Random()
            suppliers.forEach {
                Log.d("id", "${it._id}")
                it.distance = 2.0
            }
            if(suppliers.isEmpty()){
                placeholder.visibility= View.VISIBLE
            }

            UserWebClient().listFavorites(UserUtil().getToken(this), { favList ->
                idFavorites.clear()
                idFavorites.addAll(favList.map { fav -> fav._id })
                suppliers.forEach { supplier ->
                    Log.d("id", "${supplier._id}")

                    supplier.isFavorite = supplier._id in idFavorites
                }
                mainRecyclerView.adapter.notifyDataSetChanged()
            })
        })
    }

    private fun filterSuppliers(suppliers: List<Supplier>): ArrayList<Supplier>{
        val filteredSuppliers = mutableListOf<Supplier>()
        suppliers.forEach {
            if (it._id !in filteredSuppliers.map{it._id}){
                filteredSuppliers.add(it)
            }
        }

        return filteredSuppliers as ArrayList<Supplier>
    }

    private fun initAdapter(suppliers: List<Supplier>) {
        mainRecyclerView.layoutManager = LinearLayoutManager(this)
        mainRecyclerView.adapter = SuppliersAdapter(suppliers, this)
    }


    override fun onResume() {
        super.onResume()
        listSuppliers()
    }
}
