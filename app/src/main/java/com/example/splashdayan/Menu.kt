package com.example.splashdayan

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.splashdayan.MyAdapter.MyAdapter
import com.example.splashdayan.databinding.ActivityMenuBinding
import com.example.splashdayan.db.Password
import com.example.splashdayan.db.PasswordsMethods


class Menu : AppCompatActivity(), AdapterView.OnItemClickListener {

    private lateinit var binding: ActivityMenuBinding

    private lateinit var passwordsMethods: PasswordsMethods

    private lateinit var passwords: List<Password>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMenuBinding.inflate(layoutInflater)

        setContentView(binding.root)

        passwordsMethods = PasswordsMethods(this)

        initPasswordDriver()
        initListView()

    }

    private fun initPasswordDriver() {

        binding.apply {
            btnAddPassword.setOnClickListener {
                if (validateFields()) addPassword()
            }
            btnDeletePassword.setOnClickListener {
                if (validateFields()) deletePassword()
            }
            btnUpdatePassword.setOnClickListener {
                if (validateFields()) updatePassword()
            }
        }
    }

    private fun validateFields(): Boolean {
        var isValid = true

        binding.apply {
            if (etSite.text.toString().isEmpty()) {
                etSite.error = "Campo requerido"
                isValid = false
            }
            if (etPassword.text.toString().isEmpty()) {
                etPassword.error = "Campo requerido"
                isValid = false
            }
        }

        return isValid
    }

    private fun addPassword() {

        passwords.forEach {
            if (it.site == binding.etSite.text.toString()) {
                Toast.makeText(this, "Ya tienes una contraseña para este sitio", Toast.LENGTH_SHORT).show()
                return
            }
        }

        val siteTv = binding.etSite.text.toString()
        val passwordTv = binding.etPassword.text.toString()

        val password = Password(
            null,
            Login.user.userId!!,
            siteTv,
            passwordTv
        )

        val result = passwordsMethods.insertPassword(password)

        if (result) {
            Toast.makeText(this, "Contraseña agregada", Toast.LENGTH_SHORT).show()
            addItemToListView(password)
        } else {
            Toast.makeText(this, "Error al agregar contraseña", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deletePassword() {

        var exists = false

        passwords.forEach {
            if (it.site == binding.etSite.text.toString()) {
                exists = true
            }
        }

        if (!exists) {
            Toast.makeText(this, "No tienes una contraseña para este sitio", Toast.LENGTH_SHORT).show()
            return
        }

        val siteTv = binding.etSite.text.toString()

        val result = passwordsMethods.deletePassword(siteTv)

        if (result) {
            Toast.makeText(this, "Contraseña eliminada", Toast.LENGTH_SHORT).show()
            deleteItemFromListView(siteTv)
            binding.apply {
                etSite.setText("")
                etPassword.setText("")
            }
        } else {
            Toast.makeText(this, "Error al eliminar contraseña", Toast.LENGTH_SHORT).show()
        }

    }

    private fun updatePassword() {

        var exists = false

        passwords.forEach {
            if (it.site == binding.etSite.text.toString()) {
                exists = true
            }
        }

        if (!exists) {
            Toast.makeText(this, "No tienes una contraseña para este sitio", Toast.LENGTH_SHORT).show()
            return
        }

        val siteTv = binding.etSite.text.toString()
        val passwordTv = binding.etPassword.text.toString()

        val password = Password(
            null,
            Login.user.userId!!,
            siteTv,
            passwordTv
        )

        val result = passwordsMethods.updatePassword(password)

        if (result) {
            Toast.makeText(this, "Contraseña actualizada", Toast.LENGTH_SHORT).show()
            deleteItemFromListView(siteTv)
            addItemToListView(password)
        } else {
            Toast.makeText(this, "Error al actualizar contraseña", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initListView() {
        passwords = passwordsMethods.getPasswords(Login.user.userId!!)

        val lvPasswords = binding.lvPassswords

        val passwordAdapter = MyAdapter(passwords, this)

        if (passwords.isEmpty()) {
            Toast.makeText(this, "No tienes contraseñas", Toast.LENGTH_SHORT).show()
        }

        lvPasswords.apply {

            adapter = passwordAdapter
            onItemClickListener = this@Menu

        }
    }

    private fun deleteItemFromListView(site: String) {
        val lvPasswords = binding.lvPassswords
        val passwordAdapter = lvPasswords.adapter as MyAdapter

        passwordAdapter.apply {
            removePassword(site)
            notifyDataSetChanged()
        }
    }

    private fun addItemToListView(password: Password) {
        val lvPasswords = binding.lvPassswords
        val passwordAdapter = lvPasswords.adapter as MyAdapter

        passwordAdapter.apply {
            addPassword(password)
            notifyDataSetChanged()
        }
    }

    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

        val password = passwords[p2]

        binding.etSite.setText(password.site)
        binding.etPassword.setText(password.password)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var flag = false
        flag = super.onCreateOptionsMenu(menu)
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.main_menu, menu)
        return flag
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.item_api) {
            val intent = Intent(this@Menu, MapActivity::class.java)
            startActivity(intent)
            return true
        }
        if (id == R.id.item_api2) {
            val intent = Intent(this@Menu, MusicActivity::class.java)
            startActivity(intent)
            return true
        }
        if (id == R.id.item_logout) {
            val intent = Intent(this@Menu, Login::class.java)
            startActivity(intent)
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}