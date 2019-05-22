package com.gasnaporta.gasnaporta.Activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.gasnaporta.gasnaporta.Model.InOrder
import com.gasnaporta.gasnaporta.Model.Supplier
import com.gasnaporta.gasnaporta.Model.SupplierId
import com.gasnaporta.gasnaporta.R
import com.gasnaporta.gasnaporta.Util.FuncoesUtil
import com.gasnaporta.gasnaporta.Util.UserUtil
import com.gasnaporta.gasnaporta.WebClient.HistoricWebClient
import com.gasnaporta.gasnaporta.WebClient.UserWebClient
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_supplier_details.*

class SupplierDetails : AppCompatActivity() {

    private var userAgreePermissionCode = 1
    private var ligou = false

    var favoritobtn= false // Não funcionou pegando o bool supplier.isFavorite

    val colorNormal = Color.parseColor("#ffc801")
    val colorFavorito = Color.parseColor("#d1144e")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_supplier_details)
        val actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)

        val bundle = intent.extras
        val supplier = bundle?.getParcelable<Supplier>("supplier") as Supplier

        var latitude = supplier.loc.coordinates[0]
        var longitude = supplier.loc.coordinates[1]

        var mapa = "https://maps.googleapis.com/maps/api/staticmap?center=${latitude},${longitude}&zoom=15&size=400x250&maptype=roadmap%20&markers=color:red%7C${latitude},${longitude}"

        Picasso.get().load(mapa).into(backdrop)

        this.nome_fornecedor.text = "${supplier.name}"
        this.endereco_fornecedor.text = "${supplier.address}"

        var p2Format = "R$%.2f".format(supplier.priceP2)
        var p13Format = "R$%.2f".format(supplier.priceP13)
        var p45Format = "R$%.2f".format(supplier.priceP45)


        val p2valor = p2Format.split(",".toRegex())
        val p13valor = p13Format.split(",".toRegex())
        val p45valor = p45Format.split(",".toRegex())

        this.precoP2.text = p2valor.first()
        this.precoP2Cents.text = p2valor.last()

        this.precoP13.text = p13valor.first()
        this.precoP13Cents.text = p13valor.last()

        this.precoP45.text = p45valor.first()
        this.precoP45Cents.text = p45valor.last()

        this.btnligar.text = "${supplier.phone}" //provisorio -> para sempre
        this.avaliacao.text = "%.1f".format(supplier.scoresAvg)
        this.previsao.text = "${FuncoesUtil().getTempoEstimado(supplier.distance)}"

        if(supplier.isFavorite){
            favoritobtn=true
            hugeLine.setBackgroundColor(colorFavorito)
            hugeLine.requestLayout()

            smallerLine1.setBackgroundColor(colorFavorito)
            smallerLine1.requestLayout()

            smallerLine2.setBackgroundColor(colorFavorito)
            smallerLine2.requestLayout()
        }

        btnligar.setOnClickListener {
            val permissaoCall = android.Manifest.permission.CALL_PHONE

            if(ActivityCompat.checkSelfPermission(this, permissaoCall) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(permissaoCall), userAgreePermissionCode)
            }else{
                call()
            }

        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            userAgreePermissionCode -> {
                for( i in 0..(grantResults.size-1) ){
                    if ((grantResults.isNotEmpty() && grantResults[i] == PackageManager.PERMISSION_GRANTED)) {
                        call()
                    }
                }
                return
            }
        }
    }

    override fun onResume() {
        super.onResume()

        val sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val idFornecedor = sharedPreferences.getString("idFornecedor", "")

        if(idFornecedor.isNotBlank() && ligou) {
            val intent = Intent(this, Avaliacao::class.java)
            startActivity(intent)
        }
    }

    fun addFavorite(supplier: Supplier) {
        val token = UserUtil().getToken(this)
        UserWebClient().addFavorite(token, SupplierId(supplier._id), {
            supplier.isFavorite = true

            Snackbar.make(view, "Adicionado aos favoritos", Snackbar.LENGTH_LONG).show()
        })

        hugeLine.setBackgroundColor(colorFavorito)
        hugeLine.requestLayout()

        smallerLine1.setBackgroundColor(colorFavorito)
        smallerLine1.requestLayout()

        smallerLine2.setBackgroundColor(colorFavorito)
        smallerLine2.requestLayout()
    }

    fun deleteFavorite(supplier: Supplier) {
        val token = UserUtil().getToken(this)
        UserWebClient().deleteFavorite(token, SupplierId(supplier._id), {
            supplier.isFavorite = false
            Snackbar.make(view, "Removido dos favoritos", Snackbar.LENGTH_LONG).show()
        })

        hugeLine.setBackgroundColor(colorNormal)
        hugeLine.requestLayout()

        smallerLine1.setBackgroundColor(colorNormal)
        smallerLine1.requestLayout()

        smallerLine2.setBackgroundColor(colorNormal)
        smallerLine2.requestLayout()

    }

    @SuppressLint("MissingPermission")
    fun call() {
        val bundle = intent.extras
        val supplier = bundle?.getParcelable<Supplier>("supplier") as Supplier

        val inOrder = InOrder(UserUtil().getUserId(this), supplier._id)
        HistoricWebClient().save(UserUtil().getToken(this), inOrder, {})

        val editor = getSharedPreferences("myPrefs",0).edit()
        editor.clear().commit()
        editor.putString("idFornecedor", "${supplier._id}")
        editor.putString("nomeFornecedor", supplier.name)
        editor.commit()

        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:${supplier.phone}")
        startActivity(intent)
        ligou = true
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.favoritar, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.favoritar -> {
            val bundle = intent.extras
            val supplier = bundle?.getParcelable<Supplier>("supplier") as Supplier

            if (favoritobtn) {
                item.setIcon(R.drawable.favoritar)
                deleteFavorite(supplier)
                favoritobtn=false
            } else {
                item.setIcon(R.drawable.favorito)
                addFavorite(supplier)
                favoritobtn=true

            }
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        if (favoritobtn) {
            menu.findItem(R.id.favoritar).setIcon(R.drawable.favorito)
        } else {
            menu.findItem(R.id.favoritar).setIcon(R.drawable.favoritar)
        }
        return super.onPrepareOptionsMenu(menu)
    }

}