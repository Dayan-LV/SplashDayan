package com.example.splashdayan

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.mysplash.json.MyInfo
import com.example.splashdayan.databinding.ActivityLoginBinding
import com.example.splashdayan.des.MyDesUtil
import com.example.splashdayan.json.Metodos
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException


class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    //DES
    val KEY = "+4xij6jQRSBdCymMxweza/uMYo+o0EUg"
    private val testClaro = "Hola mundo"
    lateinit var testDesCifrado: String

    //Atributos
    lateinit var correo: String
    lateinit var mensaje: String

    companion object {
        public lateinit var list: List<MyInfo>
    }

    private val TAG = "LoginActivity"
    private lateinit var json: String
    private lateinit var usuario: String
    private lateinit var password: String
    val archivo = "archivo.json"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)

        if (ReadFile()) {
            jsonToList(json)
        }

        binding.haveAccount.setOnClickListener {
            goRegister()
        }

        binding.tvForgotPswd.setOnClickListener {
            goForgotPswd()
        }

        binding.tvForgotPswd.setOnClickListener(View.OnClickListener { //DES
            var myDesUtil: MyDesUtil
            myDesUtil = MyDesUtil()
            myDesUtil.addStringKeyBase64(KEY)
            //DES
            usuario = binding.etUsername.text.toString()
            if (usuario == "" || usuario.isEmpty()) {
                Toast.makeText(applicationContext, "Llena el campo de Usuario", Toast.LENGTH_LONG)
                    .show()
            } else {
                var i = 0
                for (inf in list) {
                    if (inf.usuario == usuario) {
                        correo = inf.email
                        mensaje = "<html><h1>Registro para una app????</h1></html>"
                        correo = myDesUtil.cifrar(correo)
                        mensaje = myDesUtil.cifrar(mensaje)
                        i = 1
                    }
                }
                if (i == 1) {
                    Log.i(
                        TAG,
                        usuario
                    )
                    Log.i(TAG, correo)
                    Log.i(TAG, mensaje)
                    if (sendInfo(correo, mensaje)) {
                        Toast.makeText(baseContext, "Se envío el texto", Toast.LENGTH_LONG)
                        return@OnClickListener
                    }
                    Toast.makeText(baseContext, "Error en el envío", Toast.LENGTH_LONG)
                } else {
                    if (i == 0) {
                        Log.i(TAG, "no hay usuarios")
                        Toast.makeText(baseContext, "No existen usuarios", Toast.LENGTH_LONG)
                        return@OnClickListener
                    }
                }
            }
        })


    }

    fun signIn(v: View) {
        usuario = binding.etUsername.text.toString()
        password = binding.etPassword.text.toString()
        password = Metodos.bytesToHex(Metodos.createSha1(password))
        verificar(usuario, password)

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

    fun verificar(usr: String, pswd: String) {
        var i = 0
        if (usr == "" || pswd == "") {
            Toast.makeText(applicationContext, "Llena los campos", Toast.LENGTH_LONG).show()
        } else {
            for (myInfo in list) {
                if (myInfo.usuario == usr && myInfo.password == pswd) {
                    val intent = Intent(this@Login, Menu::class.java)
                    intent.putExtra("Objeto", myInfo)
                    startActivity(intent)
                    i = 1
                }
            }
            if (i == 0) {
                Toast.makeText(
                    applicationContext,
                    "El usuario o contraseña son incorrectos",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    fun ReadFile(): Boolean {
        if (!isFileExist()) {
            return false
        }
        val file = getFile()
        var fileInputStream: FileInputStream? = null
        var bytes: ByteArray? = null
        bytes = ByteArray(file.length().toInt())
        try {
            fileInputStream = FileInputStream(file)
            fileInputStream.read(bytes)
            json = String(bytes)
            Log.d(TAG, json)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return true
    }

    private fun isFileExist(): Boolean {
        val file: File = getFile() ?: return false
        return file.isFile && file.exists()
    }

    private fun getFile(): File {
        return File(dataDir, archivo)
    }

    fun jsonToList(json: String) {
        var gson: Gson? = null
        val mensaje: String? = null
        if (json == null || json.length == 0) {
            Toast.makeText(applicationContext, "Error json null or empty", Toast.LENGTH_LONG).show()
            return
        }
        gson = Gson()
        val listType = object : TypeToken<List<MyInfo>>() {}.type
        list = gson.fromJson(json, listType)
        Log.d(TAG, "HOLA BRO")
        if (list == null || list.isEmpty()) {
            Toast.makeText(applicationContext, "Error list is null or empty", Toast.LENGTH_LONG)
                .show()
            return
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
}