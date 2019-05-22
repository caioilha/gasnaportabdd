package com.gasnaporta.gasnaporta.Adapter

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gasnaporta.gasnaporta.Activity.SupplierDetails
import com.gasnaporta.gasnaporta.Model.Supplier
import com.gasnaporta.gasnaporta.R
import com.gasnaporta.gasnaporta.Util.FuncoesUtil
import kotlinx.android.synthetic.main.supplier_cell.view.*

/**
 * Created by 15110310 on 11/04/18.
 */

class SuppliersAdapter(private val suppliers: List<Supplier>, val context: Context) : RecyclerView.Adapter<SuppliersAdapter.SupplierViewHolder>() {

    //number of item on recycler view
    override fun getItemCount(): Int {
        return suppliers.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SupplierViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.supplier_cell, parent, false)
        return SupplierViewHolder(view)
    }

    override fun onBindViewHolder(holder: SupplierViewHolder?, position: Int) {
        val supplier = suppliers[position]
        holder?.let {
            it.bindView(supplier)
        }

    }


    class SupplierViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bindView(supplier: Supplier) {

            view.setOnClickListener({
                val intent = Intent(view.context, SupplierDetails::class.java)
                val bundle = Bundle()
                bundle.putParcelable("supplier", supplier)
                intent.putExtras(bundle)
                view.context.startActivity(intent)


            })


            view.supplierNameTextView.text = supplier.name
            supplier.address.let {
                view.supplierAddressTextView.text = if (supplier.address.length > 50) supplier.address.substring(0..50) + "..." else "${supplier.address}"
            }
            var precoFormatado = "R$%.2f".format(supplier.priceP13)

            val str = "Kotlination.com = Be Kotlineer - Be Simple - Be Connective"

            val valores = precoFormatado.split(",".toRegex())


            view.lista_preco.text = valores.first()
            view.lista_precoCents.text = valores.last()
            view.lista_distancia.text = FuncoesUtil().getTempoEstimado(supplier.distance)

            if (supplier.isFavorite) {
                view.leftLine.setBackgroundColor(Color.parseColor("#d1144e"))
                view.favoritoheart.setBackgroundResource(R.drawable.favoritored)
                view.favoritoheart.layoutParams.width=(23 * (Resources.getSystem().displayMetrics.density).toInt())
                view.favoritoheart.layoutParams.height=(23 * (Resources.getSystem().displayMetrics.density).toInt())
                view.requestLayout()
            } else {
               view.leftLine.setBackgroundColor(Color.parseColor("#ffc801"))
                view.favoritoheart.setBackgroundResource(0)
                view.favoritoheart.layoutParams.width=0
                view.favoritoheart.layoutParams.height=0
                view.requestLayout()
            }

        }

    }
}