package com.example.splashdayan

import android.R
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.mysplash.json.MyInfo
import com.example.splashdayan.databinding.ActivityForgotPasswordBinding
import com.example.splashdayan.databinding.ActivityLoginBinding
import com.example.splashdayan.des.MyDesUtil
import org.json.JSONException
import org.json.JSONObject


class ForgotPassword : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding

    lateinit var list: List<MyInfo>
    var TAG = "mensaje"
    var myDesUtil = MyDesUtil().addStringKeyBase64(Registro.KEY)
    lateinit var usr: String
    lateinit var correo: String
    lateinit var mensaje: String
    lateinit var usuario: EditText
    lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)

        setContentView(binding.root)

        var ivBack: ImageView = binding.ivBack

        ivBack.setOnClickListener {
            goLogin()
        }

        usuario = binding.user
        button = binding.recuperar
        list = Login.list
        button.setOnClickListener {
            fun onClick(view: View) {
                usr = usuario.getText().toString()
                if (usr == "") {
                    Toast.makeText(
                        applicationContext,
                        "Llena el campo de Usuario",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    var i = 0
                    for (inf in list!!) {
                        if (inf.usuario == usr) {
                            correo = inf.email
                            mensaje = "<html><h1>Registro para una app????</h1></html>"
                            correo = myDesUtil.cifrar(correo)
                            mensaje = myDesUtil.cifrar(mensaje)
                            i = 1
                        }
                    }
                    if (i == 1) {
                        Log.i(TAG, usr)
                        Log.i(TAG, correo)
                        Log.i(TAG, mensaje)
                        if (sendInfo(correo, mensaje)) {
                            Toast.makeText(baseContext, "Se envío el texto", Toast.LENGTH_LONG)
                            return
                        }
                        Toast.makeText(baseContext, "Error en el envío", Toast.LENGTH_LONG)
                    } else {
                        if (i == 0) {
                            Log.i(TAG, "no hay usuarios")
                            Toast.makeText(baseContext, "No existen usuarios", Toast.LENGTH_LONG)
                            return
                        }
                    }
                }
            }
        }
    }

    fun sendInfo(correo: String?, mensaje: String?): Boolean {
        var jsonObjectRequest: JsonObjectRequest? = null
        var jsonObject: JSONObject? = null
        val url = "https://us-central1-nemidesarrollo.cloudfunctions.net/function-test"
        var requestQueue: RequestQueue? = null
        if (correo == null || correo.length == 0) {
            return false
        }
        jsonObject = JSONObject()
        try {
            jsonObject.put("correo", correo)
            jsonObject.put("mensaje", mensaje)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, jsonObject,
            { response -> Log.i(TAG, response.toString()) }
        ) { error -> Log.e(TAG, error.toString()) }
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