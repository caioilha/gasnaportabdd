package com.gasnaporta.gasnaporta.Util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import com.gasnaporta.gasnaporta.Activity.MainActivity

class FuncoesUtil {

    var REQUEST_CODE = 101
    fun getTempoEstimado(d: Double): String {

          var distancia = d/1000


        val sd = distancia / 25
        val sdd = (sd * 60) + 8 // 5 minutos de tempo de logística

        return sdd.toInt().toString()+"m"

    }


    fun setupPermissions(context: Context) {
        val permGps = ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION)

        val permCall = ContextCompat.checkSelfPermission(context,
                Manifest.permission.CALL_PHONE)

        if (permGps != PackageManager.PERMISSION_GRANTED || permCall != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(MainActivity(),
                            Manifest.permission.CALL_PHONE)) {
                val builder = AlertDialog.Builder(context)
                builder.setMessage("Precisamos de algumas permissões para oferecer uma melhor experiência no app.")
                        .setTitle("Permissões")

                builder.setPositiveButton("Ok"
                ) { dialog, _ ->
                    ActivityCompat.requestPermissions(MainActivity(),
                            arrayOf(Manifest.permission.CALL_PHONE, Manifest.permission.ACCESS_FINE_LOCATION),REQUEST_CODE)
                }

                val dialog = builder.create()
                dialog.show()
            } else {
                ActivityCompat.requestPermissions(MainActivity(),
                        arrayOf(Manifest.permission.CALL_PHONE, Manifest.permission.ACCESS_FINE_LOCATION),REQUEST_CODE)
            }
        }
    }
}