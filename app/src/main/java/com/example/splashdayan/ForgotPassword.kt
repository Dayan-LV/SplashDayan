package com.example.splashdayan

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class ForgotPassword : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        var ivBack: ImageView = findViewById(R.id.ivBack)

        ivBack.setOnClickListener {
            goLogin()
        }

    }

    private fun goLogin() {
        val intent = Intent(this@ForgotPassword, Login::class.java)
        startActivity(intent)
        finish()
    }
}