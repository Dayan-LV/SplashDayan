package com.example.splashdayan

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mysplash.json.MyInfo
import com.example.splashdayan.databinding.ActivityRegistroBinding
import com.example.splashdayan.des.MyDesUtil
import com.example.splashdayan.json.MyData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.*
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


class Registro : AppCompatActivity() {

    companion object {
        val archivo = "fila.json"
        val KEY = "+4xij6jQRSBdCymMxweza/uMYo+o0EUg"
        var myDesUtil = MyDesUtil().addStringKeyBase64(KEY)
    }

    private lateinit var binding: ActivityRegistroBinding
    private val TAG: String = "RegisterActivity"

    var list: List<MyInfo> = java.util.ArrayList()
    lateinit var json: String

    private lateinit var user: MyInfo
    private lateinit var usuario: String
    private lateinit var password: String
    private lateinit var email: String

    lateinit var lista: List<MyData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegistroBinding.inflate(layoutInflater)

        setContentView(binding.root)

        lista = ArrayList<MyData>()

        if (ReadFile()) {
            jsonToList(json)
        }

        binding.haveAccount.setOnClickListener {
            goLogin()
        }

        prepareCalendar()
        prepareNumberPicker()
    }

    fun signUp(v: View) {
        usuario = binding.etUsername.text.toString()
        password = binding.etPassword.text.toString()
        email = binding.etEmail.text.toString()

        if (validarCampos()) {
            Log.d("RegisterActivity", "Se han validado los campos")

            if (list.isNotEmpty() && usuarios(list, usuario)) {
                Toast.makeText(this, "El nombre de usuario está en uso", Toast.LENGTH_LONG).show()
            } else {
                //password = Metodos.bytesToHex(Metodos.createSha1(password))
                user = MyInfo(usuario, password, email, lista)
                listToJson(user, list as MutableList<MyInfo?>)
                Log.d( TAG,"Se ha registrado el usuario");
                Log.d(TAG, user.usuario + "\n" + user.password + "\n" + user.email)
                goLogin()
            }

        }
    }

    private fun goLogin() {
        val intent = Intent(this@Registro, Login::class.java)
        startActivity(intent)
        finish()
    }

    private fun validarCampos(): Boolean {
        if (usuario != null && password != null && email != null) {
            if (usuario.length in 6..10 && password.length in 6..10 && email.length > 0) {
                val pattern: Pattern = Pattern
                    .compile(
                        "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
                    )

                val mather: Matcher = pattern.matcher(email)

                if (mather.find()) {
                    return true
                } else {
                    Toast.makeText(this, "El email ingresado no es válido", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Asegura que el usuario y contraseña esten entre 6 y 10 caracteres", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
        }
        Log.d(TAG, "No se ha completado la validación")
        return false
    }

    private fun prepareCalendar() {
        var cvEjemplo = binding.cvEjemplo
        var tvFecha = binding.tvFecha

        cvEjemplo.setOnDateChangeListener { cv, year, month, day ->
            var date = "$day/${month+1}/$year"
            tvFecha.text = "Fecha seleccionada: $date"
        }

        var calendar = Calendar.getInstance()
        calendar.set(2005, 5-1, 8)

        cvEjemplo.date = calendar.timeInMillis

        var fecha = cvEjemplo.date
        var d = cvEjemplo.firstDayOfWeek
        cvEjemplo.firstDayOfWeek = (d+1) % 7
    }

    private fun prepareNumberPicker() {
        var np: NumberPicker

        for (i in 1 until 4) {
            np = findViewById(resources.getIdentifier("np${i}Ejemplo", "id", packageName))

            np.minValue = 0
            np.maxValue = 9
            np.value = 6
            np.wrapSelectorWheel = true
        }
    }

    fun listToJson(info: MyInfo?, list: MutableList<MyInfo?>) {
        var gson: Gson? = null
        var json: String? = null
        gson = Gson()
        list.add(info)
        json = gson.toJson(list, ArrayList::class.java)
        if (json == null) {
            Log.d(TAG, "Error json")
        } else {
            Log.d(TAG, json)
            json = myDesUtil.cifrar(json)
            Log.d(TAG, json)
            writeFile(json)
        }
        Toast.makeText(applicationContext, "Ok", Toast.LENGTH_LONG).show()
    }

    private fun writeFile(text: String?): Boolean {
        var file: File? = null
        var fileOutputStream: FileOutputStream? = null
        try {
            file = getFile()
            fileOutputStream = FileOutputStream(file)
            fileOutputStream.write(text!!.toByteArray(StandardCharsets.UTF_8))
            fileOutputStream.close()
            Log.d(TAG, "Hola")
            return true
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return false
    }

    private fun getFile(): File? {
        return File(dataDir, archivo)
    }

    private fun usuarios(list: List<MyInfo>, usr: String): Boolean {
        var bandera = false
        for (informacion in list) {
            if (informacion.usuario == usr) {
                bandera = true
            }
        }
        return bandera
    }

     private fun ReadFile(): Boolean {
        if (!isFileExist()) {
            return false
        }
        val file = getFile()
        var fileInputStream: FileInputStream? = null
        var bytes: ByteArray? = null
        bytes = ByteArray(file!!.length().toInt())
        try {
            fileInputStream = FileInputStream(file)
            fileInputStream.read(bytes)
            json = String(bytes)
            json = myDesUtil.desCifrar(json)
            Log.d(TAG, json)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                fileInputStream!!.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return true
    }

    private fun isFileExist(): Boolean {
        val file = getFile() ?: return false
        return file.isFile && file.exists()
    }

    private fun jsonToList(json: String?) {
        var gson: Gson? = null
        val mensaje: String? = null
        if (json == null || json.length == 0) {
            Toast.makeText(applicationContext, "Error json null or empty", Toast.LENGTH_LONG).show()
            return
        }
        gson = Gson()
        val listType = object : TypeToken<ArrayList<MyInfo?>?>() {}.type
        list = gson.fromJson<List<MyInfo>>(json, listType)
        if (list == null || list.size == 0) {
            Toast.makeText(applicationContext, "Error list is null or empty", Toast.LENGTH_LONG)
                .show()
            return
        }
    }
}