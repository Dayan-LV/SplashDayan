package com.example.splashdayan

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.splashdayan.databinding.ActivityRegistroBinding
import com.example.splashdayan.db.User
import com.example.splashdayan.db.UserMethods
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

class Registro : AppCompatActivity() {

    companion object {
    }

    private lateinit var binding: ActivityRegistroBinding
    private val TAG: String = "RegisterActivity"

    private lateinit var user: User

    private lateinit var email: String
    private lateinit var username: String
    private lateinit var password: String
    private lateinit var birthDate: String
    private lateinit var gender: String
    private var weight: Double = 0.0

    private lateinit var userMethods: UserMethods

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegistroBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.haveAccount.setOnClickListener {
            goLogin()
        }

        prepareCalendar()
        prepareNumberPicker()
    }

    fun signUp(v: View) {
        username = binding.etUsername.text.toString()
        password = binding.etPassword.text.toString()
        email = binding.etEmail.text.toString()
        gender = if (binding.rgGender.checkedRadioButtonId == R.id.rbMasculino) "M" else "F"
        weight = (binding.np1Ejemplo.value.toString() + binding.np2Ejemplo.value.toString() + "." + binding.np3Ejemplo.value.toString()).toDouble()

        if (validarCampos()) {

            userMethods = UserMethods(this)

            user = User(
                userId = null,
                email = email,
                username = username,
                password = password,
                gender = gender,
                birthDate = birthDate,
                weight = weight
            )

            if (userMethods.insertUser(user)) {
                Toast.makeText(this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show()
                goLogin()
            } else {
                Toast.makeText(this, "No se ha podido registrar el usuario", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun goLogin() {
        val intent = Intent(this@Registro, Login::class.java)
        startActivity(intent)
        finish()
    }

    private fun validarCampos(): Boolean {
        if (username != null && password != null && email != null) {
            if (username.length in 6..10 && password.length in 6..10 && email.length > 0) {
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
        val cvEjemplo = binding.cvEjemplo
        val tvFecha = binding.tvFecha

        cvEjemplo.setOnDateChangeListener { cv, year, month, day ->
            val dateTv = "$day/${month+1}/$year"
            tvFecha.text = "Fecha seleccionada: $dateTv"

            val calendar = Calendar.getInstance().apply {
                set(year, month, day)
            }
            val date = calendar.time
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            birthDate = dateFormat.format(date)
        }

        val calendar = Calendar.getInstance()
        calendar.set(2005, 5-1, 8)

        cvEjemplo.date = calendar.timeInMillis

        val d = cvEjemplo.firstDayOfWeek
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
}