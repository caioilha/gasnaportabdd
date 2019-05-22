package com.gasnaporta.gasnaporta.Activity

import android.location.Geocoder
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.gasnaporta.gasnaporta.Model.InAddSupplier
import com.gasnaporta.gasnaporta.Model.LatitudeLongitude
import com.gasnaporta.gasnaporta.Model.Loc
import com.gasnaporta.gasnaporta.R
import com.gasnaporta.gasnaporta.WebClient.SupplierWebClient
import kotlinx.android.synthetic.main.activity_add_supplier.*
import java.io.IOException
import java.util.*

class AddSupplier : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_supplier)

        val actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)
        actionBar.title = "Adicionar Distribuidor"


        btnAddSupplier.setOnClickListener {
            val name = supplierName.text.toString()
            val phone = supplierPhone.text.toString()
            val address = supplierAddress.text.toString()
            val priceP2 = if (p2Price.text.toString().isBlank()) 0.0 else p2Price.text.toString().toDouble()
            val priceP13 = if (p13Price.text.toString().isBlank()) 0.0 else p13Price.text.toString().toDouble()
            val priceP45 = if (p45Price.text.toString().isBlank()) 0.0 else p45Price.text.toString().toDouble()


            val latitudeLongitude = getCoordinates(address)

            val lat = latitudeLongitude.latitude
            val long = latitudeLongitude.longitude

            val coordenadas = FloatArray(2)
            coordenadas[0] = lat.toFloat()
            coordenadas[1] = long.toFloat()
            val loc = Loc(coordenadas)

            val supplier = InAddSupplier(loc, priceP2, priceP13, priceP45, address, name, phone)

            if (validateSupplier(supplier)) {

                SupplierWebClient().addSupplier(supplier, {
                    Snackbar.make(layoutAddSupplier, "Distribuidor ${name} cadastrado.", Snackbar.LENGTH_LONG).show()
                    supplierName.setText("")
                    supplierPhone.setText("")
                    supplierAddress.setText("")
                    p2Price.setText("")
                    p13Price.setText("")
                    p45Price.setText("")
                })
            }


        }


    }

    private fun getCoordinates(address: String): LatitudeLongitude {
        var latitude = 0.0
        var longitude = 0.0
        try {

            val geocoder = Geocoder(this, Locale.getDefault())
            val addresses = geocoder.getFromLocationName(address, 1)
            if (addresses.size > 0) {
                latitude = addresses[0].latitude
                longitude = addresses[0].longitude
            }
        } catch (e: IOException){
            Snackbar.make(layoutAddSupplier, "${e.message}", Snackbar.LENGTH_LONG).show()
        }
        return LatitudeLongitude(latitude, longitude)
    }

    private fun validateSupplier(supplier: InAddSupplier): Boolean {
        return validateData(supplier) && validateCoordinates(supplier.loc)

    }

    private fun validateCoordinates(loc: Loc): Boolean {
        if ((loc.coordinates[0] == 0f && loc.coordinates[1] == 0f)) {
            Snackbar.make(layoutAddSupplier, "Endereço não encontrado.", Snackbar.LENGTH_LONG).show()
            return false
        }

        return true //retorna false se os 2 forem 0 (n achou endereço ou algo assim), que é no meio do oceano e parece ser um local complicado de algum dia existir fornecedor lá
    }

    private fun validateData(supplier: InAddSupplier): Boolean {
        if (supplier.name.isBlank() || supplier.address.isBlank() || supplier.phone.isBlank()) {
            Snackbar.make(layoutAddSupplier, "Todos os dados são obrigatórios.", Snackbar.LENGTH_LONG).show()
            return false
        }
        if (supplier.priceP13 == 0.0 && supplier.priceP2 == 0.0 && supplier.priceP45 == 0.0) {
            Snackbar.make(layoutAddSupplier, "O Distribuidor deve possuir ao menos um tipo de gás.", Snackbar.LENGTH_LONG).show()
            return false
        }
        return true
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


}
