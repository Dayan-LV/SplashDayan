package com.example.splashdayan

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.splashdayan.db.User
import com.example.splashdayan.db.UserMethods
import com.example.splashdayan.des.MyDesUtil
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class ForgotPassword : AppCompatActivity() {
    var list: List<User>? = null
    var json: String? = null
    var TAG = "mensaje"
    var TOG = "error"
    var cadena: String? = null
    val KEY = "+4xij6jQRSBdCymMxweza/uMYo+o0EUg"
    var myDesUtil: MyDesUtil = MyDesUtil().addStringKeyBase64(KEY)
    var usr: String? = null
    var correo: String? = null
    var mensaje: String? = null
    var usuario: EditText? = null
    var email: EditText? = null
    var button: Button? = null
    var button1: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        usuario = findViewById(R.id.user)
        email = findViewById<EditText>(R.id.mail)
        button = findViewById<Button>(R.id.recuperar)
        button1 = findViewById<Button>(R.id.login)
        val userMethods: UserMethods = UserMethods(this)
        button1!!.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@ForgotPassword, MainActivity::class.java)
            startActivity(intent)
        })
        button!!.setOnClickListener(View.OnClickListener {
            usr = usuario!!.text.toString()
            correo = email!!.text.toString()
            val user: User? = userMethods.getUser(usr!!)
            if (usr == "" && correo == "") {
                Toast.makeText(applicationContext, "Complete algún campo", Toast.LENGTH_LONG).show()
            } else {
                if (user == null) {
                    Toast.makeText(
                        applicationContext,
                        "El usuario o correo no existen",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    correo = user.email
                    val contra: String = user.password
                    var nueva = ""
                    for (i in 0..4) {
                        val random = Random()
                        val ch = random.nextInt(26) + 'a'.toInt()
                        nueva += ch.toString()
                    }
                    mensaje = """
                        <html>
                            <head>
                                <style>
                                    body {
                                        background-color: #f2f6fc;
                                        font-family: "Open Sans", sans-serif;
                                    }
                                    .container {
                                        background-color: #ffffff;
                                        box-shadow: 0px 0px 15px rgba(0, 0, 0, 0.1);
                                        border-radius: 10px;
                                        max-width: 600px;
                                        margin: 0 auto;
                                        padding: 40px;
                                        text-align: center;
                                    }
                                    h1 {
                                        font-size: 32px;
                                        font-weight: bold;
                                        color: #222222;
                                        margin-top: 0;
                                    }
                                    p {
                                        font-size: 18px;
                                        color: #555555;
                                        line-height: 1.5;
                                        margin-bottom: 30px;
                                    }
                                    .button {
                                        display: inline-block;
                                        padding: 10px 20px;
                                        background-color: #2d9cdb;
                                        color: #ffffff;
                                        font-size: 16px;
                                        font-weight: bold;
                                        text-decoration: none;
                                        border-radius: 5px;
                                        transition: all 0.3s ease-in-out;
                                    }
                                    .button:hover {
                                        background-color: #1e6fbf;
                                    }
                                </style>
                            </head>
                            <body>
                                <div class="container">
                                    <h1>Recupera tu contraseña</h1>
                                    <img src="https://www.razlee.com/wp-content/uploads/2017/08/Password-Reset-1-768x768-1-150x150.png" alt="icono de llave" width="150" height="150">
                                    <p>Tu contraseña antigua era <strong>${contra}</strong>, ahora es <strong>${nueva}</strong>.</p>
                                    <a href="#" class="button">Restablecer contraseña</a>
                                    <p>Si no solicitaste este correo electrónico, ignóralo.</p>
                                </div>
                            </body>
                        </html>

                    """.trimIndent()
                    correo = myDesUtil.cifrar(correo)
                    mensaje = myDesUtil.cifrar(mensaje)
                    val f: Boolean = userMethods.updateUser(usr!!, nueva)
                    if (f) {
                        if (sendInfo(correo, mensaje)) {
                            Toast.makeText(
                                applicationContext,
                                "Se ha enviado una contraseña a su correo",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "Error con sendinfo",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Error al enviar correo",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        })
    }

    fun sendInfo(correo: String?, mensaje: String?): Boolean {
        var jsonObjectRequest: JsonObjectRequest? = null
        var jsonObject: JSONObject? = null
        val url = "https://us-central1-nemidesarrollo.cloudfunctions.net/envio_correo"
        var requestQueue: RequestQueue? = null
        if (correo == null || correo.length == 0) {
            return false
        }
        jsonObject = JSONObject()
        try {
            jsonObject.put("correo", correo)
            jsonObject.put("mensaje", mensaje)
            val hola = jsonObject.toString()
            Log.i(TAG, hola)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, jsonObject,
            { response -> Log.i(TAG, response.toString()) }
        ) { error -> Log.e(TOG, error.toString()) }
        requestQueue = Volley.newRequestQueue(baseContext)
        requestQueue.add(jsonObjectRequest)
        return true
    }

    private fun goLogin() {
        val intent = Intent(this@ForgotPassword, Login::class.java)
        startActivity(intent)
        finish()
    }
}