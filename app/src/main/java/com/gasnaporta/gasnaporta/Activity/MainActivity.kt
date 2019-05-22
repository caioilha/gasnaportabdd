package com.gasnaporta.gasnaporta.Activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.gasnaporta.gasnaporta.Adapter.SuppliersAdapter
import com.gasnaporta.gasnaporta.Model.Supplier
import com.gasnaporta.gasnaporta.R
import com.gasnaporta.gasnaporta.Util.UserUtil
import com.gasnaporta.gasnaporta.WebClient.SupplierWebClient
import com.gasnaporta.gasnaporta.WebClient.UserWebClient
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.content_main.view.*
import java.util.*

private val context: Context? = null
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener  {

    private val suppliers: ArrayList<Supplier> = arrayListOf()
    private val idFavorites: ArrayList<String> = arrayListOf()
    private var order = 1

    var permissoes = false
    var latitude  = -30.028473
    var longitude = -51.228149

    private var userAgreePermissionCode = 1

    private lateinit var userLocationClient: FusedLocationProviderClient
    private lateinit var userLocationCallback: LocationCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        progressLayout.visibility = View.VISIBLE

        val permissaoGps = android.Manifest.permission.ACCESS_FINE_LOCATION

        if(ActivityCompat.checkSelfPermission(this, permissaoGps) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(permissaoGps), userAgreePermissionCode)
        }else{
            permissoes = true
            getLastLocation()
            Handler().postDelayed({
                listSuppliers(order)

            }, 1000)
        }

        swipeContainer.setOnRefreshListener {
            listSuppliers(order)
            swipeContainer.isRefreshing = false
        }

        initAdapter(suppliers)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        // Preenche nome e email
        var headerView = nav_view.getHeaderView(0)

        var headerUser: TextView = headerView.findViewById(R.id.headerUser)
        var headerEmail: TextView = headerView.findViewById(R.id.headerEmail)

        headerUser.text = UserUtil().getUserName(this)
        headerEmail.text = UserUtil().getUserEmail(this)

    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            userAgreePermissionCode -> {
                for( i in 0..(grantResults.size-1) ){
                    if ((grantResults.isNotEmpty() && grantResults[i] == PackageManager.PERMISSION_GRANTED)) {
                        permissoes = true
                        getLastLocation()
                    }
                    else {
                        finish()
                    }
                }
                return
            }
        }
    }
    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putParcelableArrayList("suppliers", suppliers)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation(){
        userLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val userLocationRequest = LocationRequest().apply {
            interval = 1000
            fastestInterval = 1000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        userLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations){
                    latitude = location.latitude
                    longitude = location.longitude
                    userLocationClient.removeLocationUpdates(userLocationCallback)
                    listSuppliers(order)
                }
            }
        }

        userLocationClient.requestLocationUpdates(userLocationRequest,userLocationCallback,null)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml

        when (item.itemId) {
            R.id.ordenar_distancia -> {
                suppliers.sortWith(compareBy({ it.distance }))
                item.isChecked = true
                order = 1
            }
            R.id.ordenar_preco -> {
                suppliers.sortWith(compareBy({ it.priceP13 }))
                item.isChecked = true
                order = 2
            }
            R.id.ordenar_ranking -> {
                suppliers.sortWith(compareByDescending({it.scoresAvg}))
                item.isChecked = true
                order = 3
            }
            else -> return super.onOptionsItemSelected(item)
        }
        mainRecyclerView.adapter.notifyDataSetChanged()
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here. (Hamburger menu)
        when (item.itemId) {
            R.id.nav_addSupplier -> {
                val intent = Intent(this, AddSupplier::class.java)
                startActivity(intent)
            }
            R.id.nav_favoritos -> {
                val intent = Intent(this, Favorites::class.java)
                startActivity(intent)
            }
            R.id.nav_historico -> {
                val intent = Intent(this, Historic::class.java)
                startActivity(intent)
            }
            R.id.nav_sair -> {
                logout()
            }
        }

        return true
    }

    private fun killActivity() {
        finish()
    }

    private fun logout() {

        UserUtil().logout(this)
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        killActivity()
    }

    private fun listSuppliers(savedInstanceState: Bundle?) {
        if (savedInstanceState == null || savedInstanceState.isEmpty) {
            listSuppliers(order)
        } else {
            suppliers.clear()
            suppliers.addAll(savedInstanceState.getParcelableArrayList("suppliers"))
            mainRecyclerView.adapter.notifyDataSetChanged()
        }
    }

    private fun listSuppliers(order: Int) {
        SupplierWebClient().list(viewMain,latitude,longitude) { supplierList ->
            suppliers.clear()
            suppliers.addAll(supplierList)
            val random = Random()

            UserWebClient().listFavorites(UserUtil().getToken(this), { favList ->
                idFavorites.clear()
                idFavorites.addAll(favList.map { fav -> fav._id })
                suppliers.forEach { supplier ->
                    //Log.d("id", "${supplier._id}")

                    supplier.isFavorite = supplier._id in idFavorites

                }
                if(order == 1) {
                    suppliers.sortWith(compareBy({ it.distance }))
                } else if(order == 2) {
                    suppliers.sortWith(compareBy({ it.priceP13 }))
                } else if(order == 3) {
                    suppliers.sortWith(compareByDescending({ it.scoresAvg }))
                }
                mainRecyclerView.adapter.notifyDataSetChanged()
                viewMain.progressLayout.visibility = View.GONE
                viewMain.swipeContainer.visibility = View.VISIBLE
            })

        }

    }

    private fun initAdapter(suppliers: List<Supplier>) {
        mainRecyclerView.layoutManager = LinearLayoutManager(this)
        mainRecyclerView.adapter = SuppliersAdapter(suppliers, this)
    }


    override fun onResume() {
        super.onResume()
        if(permissoes) {
            listSuppliers(order)
        }

    }


}
