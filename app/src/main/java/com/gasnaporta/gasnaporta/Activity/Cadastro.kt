package com.gasnaporta.gasnaporta.Activity

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.gasnaporta.gasnaporta.Model.InAddUser
import com.gasnaporta.gasnaporta.Model.InLogin
import com.gasnaporta.gasnaporta.R
import com.gasnaporta.gasnaporta.Util.UserUtil
import com.gasnaporta.gasnaporta.WebClient.UserWebClient
import kotlinx.android.synthetic.main.activity_cadastro.*
import java.util.regex.Pattern

class Cadastro : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        layoutCadastro.visibility = View.VISIBLE

        val actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)
        actionBar.hide()

        btnCadastrarUsuario.setOnClickListener {
            //fazer metodos p/ validaçao
            val password = senhaCadastro.text.toString()
            val passwordConfirm = senhaConfirmarCadastro.text.toString()
            val email = emailCadastro.text.toString()
            val name = nomeCadastro.text.toString()

            if (isUserDataValid(name, email, password, passwordConfirm)) {
                layoutCadastro.visibility = View.INVISIBLE
                progressLayout.visibility = View.VISIBLE
                addUser(name, email, password)
            }


        }
    }

    private fun isUserDataValid(name: String, email: String, password: String, passwordConfirm: String): Boolean {
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty()) {
            Snackbar.make(cadastroView, "Preencha todos os campos", Snackbar.LENGTH_SHORT).show()
            return false
        }
        if (!isEmailValid(email)) {
            Snackbar.make(cadastroView, "Email inválido.", Snackbar.LENGTH_SHORT).show()
            return false
        }
        if (password != passwordConfirm) {
            Snackbar.make(cadastroView, "As senhas digitadas não são iguais.", Snackbar.LENGTH_SHORT).show()
            resetPasswords()
            return false
        }
        if (password.length < 5) {
            Snackbar.make(cadastroView, "A senha deve ter no mínimo 5 caracteres.", Snackbar.LENGTH_SHORT).show()
            resetPasswords()
            return false
        }
        return true
    }


    private fun addUser(name: String, email: String, password: String) {
        val inAddUser = InAddUser(name, email, password)
        UserWebClient().addUser(inAddUser, {
            val success: Boolean = it
            if (success) {
                val login = InLogin(email, password)
                UserWebClient().loginCadastro(login, {
                    if (it.user == null) {
                        Snackbar.make(cadastroView, "Falha na tentativa de login após cadastro", Snackbar.LENGTH_INDEFINITE).show()
                        val intent = Intent(this, Login::class.java)
                        startActivity(intent)
                    } else {
                        UserUtil().login(this, it)
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        Snackbar.make(cadastroView, "Usuário cadastrado com sucesso.", Snackbar.LENGTH_INDEFINITE).show()
                        killActivity()
                    }
                })

            } else {

                progressLayout.visibility = View.INVISIBLE
                layoutCadastro.visibility = View.VISIBLE
                Snackbar.make(cadastroView, "O email ${email} já cadastrado", Snackbar.LENGTH_INDEFINITE).show()
            }
        })
    }

    private fun resetPasswords() {
        senhaCadastro.setText("")
        senhaConfirmarCadastro.setText("")
    }

    private fun killActivity() {
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun isEmailValid(email: String): Boolean {
        return Pattern.compile(
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
        ).matcher(email).matches()
    }
//    fun validaSenha(senha: String): Boolean {
//        return !Pattern.compile(
//                "[\\s\\W]+"
//        ).matcher(senha).matches()
//    }
}