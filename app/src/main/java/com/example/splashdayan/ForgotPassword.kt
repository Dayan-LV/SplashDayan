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
import com.example.mysplash.json.MyInfo
import com.example.splashdayan.Login
import com.example.splashdayan.Registro
import com.example.splashdayan.databinding.ActivityForgotPasswordBinding
import com.example.splashdayan.des.MyDesUtil
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.nio.charset.StandardCharsets


class ForgotPassword : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding

    var list: List<MyInfo>? = null
    var json: String? = null
    var TAG = "mensaje"
    var TOG = "error"
    var cadena: String? = null
    var myDesUtil = MyDesUtil().addStringKeyBase64(Registro.KEY)
    var usr: String? = null
    var correo: String? = null
    var mensaje:String? = null
    var usuario: EditText? = null
    var email:EditText? = null
    var button: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)

        setContentView(binding.root)

        usuario = binding.user
        email = binding.mail
        button = binding.recuperar

        list = Login.list

        button!!.setOnClickListener {
            usr = usuario!!.text.toString()
            correo = email!!.text.toString()
            if (usr == "" && correo == "") {
                Toast.makeText(applicationContext, "Complete algún campo", Toast.LENGTH_LONG).show()
            } else {
                var i = 0
                var j = 0
                for (inf in list!!) {
                    if (inf.usuario == usr || inf.email == correo) {
                        correo = inf.email
                        val contra = inf.password
                        val nueva = String.format("%d", (Math.random() * 1000).toInt())
                        mensaje =
                            "<html><body><h1>Su contraseña era $contra ahora es $nueva</h1></body></html>"
                        correo = myDesUtil.cifrar(correo)
                        mensaje = myDesUtil.cifrar(mensaje)
                        list!![j].password = nueva
                        Log.i(TAG, nueva)
                        Log.i(TAG, list!![j].password)
                        List2Json(list)
                        i = 1
                    }
                    j++
                }
                if (i == 1) {
                    Log.i(TAG, usr!!)
                    Log.i(TAG, correo!!)
                    Log.i(TAG, mensaje!!)
                    if (sendInfo(correo, mensaje)) {
                        Toast.makeText(baseContext, "Se envío el texto", Toast.LENGTH_LONG).show()
                        goLogin()
                    } else {
                        Toast.makeText(baseContext, "Error en el envío", Toast.LENGTH_LONG).show()
                    }
                } else {
                    if (i == 0) {
                        Log.i(TAG, "no hay usuarios")
                        Toast.makeText(baseContext, "No existen usuarios", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
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
        ) { error -> Log.e(TAG, error.toString()) }
        requestQueue = Volley.newRequestQueue(baseContext)
        requestQueue.add(jsonObjectRequest)
        return true
    }

    private fun getFile(): File {
        return File(dataDir, Registro.archivo)
    }

    fun List2Json(list: List<MyInfo?>?) {
        var gson: Gson? = null
        var json: String? = null
        gson = Gson()
        json = gson.toJson(list, ArrayList::class.java)
        if (json == null) {
            Log.d(TAG, "Error json")
        } else {
            Log.d(TAG, json)
            json = myDesUtil.cifrar(json)
            Log.d(TAG, json)
            writeFile(json)
        }
    }

    private fun writeFile(text: String): Boolean {
        var file: File? = null
        var fileOutputStream: FileOutputStream? = null
        try {
            file = getFile()
            fileOutputStream = FileOutputStream(file)
            fileOutputStream.write(text.toByteArray(StandardCharsets.UTF_8))
            fileOutputStream.close()
            Log.d(this.TAG, "Hola")
            return true
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return false
    }

    fun goLogin() {
        val intent = Intent(this@ForgotPassword, Login::class.java)
        startActivity(intent)
        finish()
    }
}