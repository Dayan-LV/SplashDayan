package com.example.splashdayan

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.splashdayan.databinding.ActivityLoginBinding
import com.example.splashdayan.db.User
import com.example.splashdayan.db.UserMethods
import com.example.splashdayan.json.Metodos


class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    companion object {
        public lateinit var user: User
    }

    private val TAG = "LoginActivity"

    private lateinit var username: String
    private lateinit var password: String

    private lateinit var userMethods: UserMethods

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.haveAccount.setOnClickListener {
            goRegister()
        }

        binding.tvForgotPswd.setOnClickListener {
            goForgotPswd()
        }

    }

    fun signIn(v: View) {
        username = binding.etUsername.text.toString()
        password = binding.etPassword.text.toString()
        verificar()
    }

    private fun goRegister() {
        val intent = Intent(this@Login, Registro::class.java)
        startActivity(intent)
        finish()
    }

    private fun goForgotPswd(){
        val intent = Intent(this@Login, ForgotPassword::class.java)
        startActivity(intent)
        finish()
    }

    private fun verificar() {
        userMethods = UserMethods(this)

        if (userMethods.validateLogin(username, password)) {
            user = userMethods.getUser(username)!!
            val intent = Intent(this@Login, Menu::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
        }
    }

}