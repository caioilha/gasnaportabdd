package com.gasnaporta.gasnaporta.Activity

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.gasnaporta.gasnaporta.Model.InLogin
import com.gasnaporta.gasnaporta.R
import com.gasnaporta.gasnaporta.Util.UserUtil
import com.gasnaporta.gasnaporta.WebClient.UserWebClient
import kotlinx.android.synthetic.main.activity_login.*


class Login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        layoutLogin.visibility = View.VISIBLE

        val actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)
        actionBar.hide()

        val token = UserUtil().getToken(this)

        if (token != null && token.isNotEmpty()) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        btncadastrar.setOnClickListener {

                val intent = Intent(this, Cadastro::class.java)
                startActivity(intent)
        }

        btnLogar.setOnClickListener {
            val userEmail = email.text.toString()
            val userPassword = password.text.toString()
            val user = InLogin(userEmail, userPassword)
            layoutLogin.visibility=View.INVISIBLE
            progressLayout2.visibility=View.VISIBLE

                if (!login(user)) { //login falhou, reseta os dados/
                    email.setText("")
                    password.setText("")
                    progressLayout2.visibility = View.INVISIBLE
                    layoutLogin.visibility = View.VISIBLE

            }

        }

    }

    private fun killActivity() {
        finish()
    }
    fun login(login: InLogin): Boolean {
        var loginSuccess = true
        UserWebClient().login(loginView, login, {
            if (it.user == null) {
                progressLayout2.visibility=View.INVISIBLE
                layoutLogin.visibility=View.VISIBLE
                loginSuccess = false
                Snackbar.make(loginView, "Dados incorretos ou usuário não cadastrado.", Snackbar.LENGTH_SHORT).show()
                password.setText("")
            } else {
                Log.e("Token User Atual", it.token)
                UserUtil().login(this, it)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                killActivity()
            }
        })
        return loginSuccess
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onResume() {
        super.onResume()
        val token = UserUtil().getToken(this)
        if (token.isNotEmpty()) {
            finish()
        }
    }

}
