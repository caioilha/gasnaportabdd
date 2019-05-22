package com.gasnaporta.gasnaporta.Activity

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.gasnaporta.gasnaporta.Model.Score
import com.gasnaporta.gasnaporta.R
import com.gasnaporta.gasnaporta.WebClient.SupplierWebClient
import kotlinx.android.synthetic.main.activity_avaliacao.*

/**
 * Created by 15110310 on 04/06/18.
 */

class Avaliacao: AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_avaliacao)



        val sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val supplier = sharedPreferences.getString("nomeFornecedor", "")
        nome_fornecedor.setText(supplier)

        var estrelas = 0
        val actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)
        actionBar.title = "Avaliação"

        view1.visibility= View.VISIBLE

        btnNao.setOnClickListener {
            val editor = getSharedPreferences("myPrefs",0).edit()
            editor.clear().commit()

            finish()
        }
        btnSim.setOnClickListener {
            view1.visibility= View.GONE
            view2.visibility= View.VISIBLE



        }


        btnAvaliar.setOnClickListener {


            val sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
            val idFornecedor = sharedPreferences.getString("idFornecedor", "")
            val scorenovo = Score(idFornecedor,estrelas)
            addScore(scorenovo)

            val editor = getSharedPreferences("myPrefs",0).edit()
            editor.clear().commit()

            finish()



        }

        star1.setOnClickListener {
            estrelas = 1
            star1.setImageResource(R.drawable.staron)
            star2.setImageResource(R.drawable.staroff)
            star3.setImageResource(R.drawable.staroff)
        }
        star2.setOnClickListener {
            estrelas = 2
            star1.setImageResource(R.drawable.staron)
            star2.setImageResource(R.drawable.staron)
            star3.setImageResource(R.drawable.staroff)
        }
        star3.setOnClickListener {
            estrelas = 3
            star1.setImageResource(R.drawable.staron)
            star2.setImageResource(R.drawable.staron)
            star3.setImageResource(R.drawable.staron)
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        val editor = getSharedPreferences("myPrefs",0).edit()
        editor.clear().commit()
        onBackPressed()
        return true
    }

    private fun addScore(score: Score) {

        SupplierWebClient().addScore(score, {


        })
    }
}