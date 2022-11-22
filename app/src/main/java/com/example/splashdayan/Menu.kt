package com.example.splashdayan

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.mysplash.json.MyInfo
import com.example.splashdayan.MyAdapter.MyAdapter
import com.example.splashdayan.json.MyData
import com.google.gson.Gson
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.nio.charset.StandardCharsets


class Menu : AppCompatActivity() {
    private lateinit var list: List<MyInfo>
    var TAG = "mensaje"
    var json: String? = null
    private lateinit var listView: ListView
    private lateinit var listo: MutableList<MyData>
    var aux: String? = null
    var pos = 0
    lateinit var myInfo: MyInfo
    lateinit var editText: EditText
    lateinit var editText1: EditText
    lateinit var button: Button
    lateinit var button1:android.widget.Button
    lateinit var button2:android.widget.Button

    override fun onCreate(savedInstanceState: Bundle?) {
        var `object`: Any? = null
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        val intent = intent
        if (intent != null) {
            if (intent.extras != null) {
                `object` = intent.extras!!["Objeto"]
                if (`object` != null) {
                    if (`object` is MyInfo) {
                        myInfo = `object`
                    }
                }
            }
        }
        list = ArrayList()
        list = Login.list
        editText = findViewById(R.id.editText1)
        editText1 = findViewById(R.id.editText2)
        button = findViewById(R.id.buttonE)
        button1 = findViewById(R.id.buttonM)
        button2 = findViewById(R.id.buttonA)
        listView = findViewById<View>(R.id.listViewId) as ListView
        listo = ArrayList()
        listo = myInfo.contras as MutableList<MyData>
        val myAdapter = MyAdapter(listo, baseContext)
        listView.adapter = myAdapter
        listView.onItemClickListener =
            OnItemClickListener { adapterView, view, i, l ->
                editText.setText(listo[i].usuario)
                editText1.setText(listo[i].contra)
                pos = i
            }
        button.setOnClickListener {
            listo.removeAt(pos)
            myInfo.contras = listo
            val myAdapter = MyAdapter(listo, baseContext)
            listView.adapter = myAdapter
            editText.setText("")
            editText1.setText("")
            Toast.makeText(applicationContext, "Se eliminó la contraseña", Toast.LENGTH_LONG).show()
        }
        button1.setOnClickListener {
            val usr = editText.text.toString()
            val contra = editText1.text.toString()
            listo[pos].usuario = usr
            listo[pos].contra = contra
            myInfo.contras = listo
            val myAdapter = MyAdapter(listo, baseContext)
            listView.adapter = myAdapter
            editText.setText("")
            editText1.setText("")
            Toast.makeText(applicationContext, "Se modificó la contraseña", Toast.LENGTH_LONG)
                .show()
        }
        button2.setOnClickListener {
            val usr = editText.text.toString()
            val contra = editText1.text.toString()
            if (usr == "" || contra == "") {
                Toast.makeText(applicationContext, "Llena los campos", Toast.LENGTH_LONG).show()
            } else {
                val myData = MyData()
                myData.contra = contra
                myData.usuario = usr
                listo.add(myData)
                val myAdapter = MyAdapter(listo, baseContext)
                listView.adapter = myAdapter
                editText.setText("")
                editText1.setText("")
                Toast.makeText(applicationContext, "Se agregó la contraseña", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: android.view.Menu?): Boolean {
        var flag = false
        flag = super.onCreateOptionsMenu(menu)
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.main_menu, menu)
        return flag
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.item1) {
            val usr = editText.text.toString()
            val contra = editText1.text.toString()
            if (usr == "" || contra == "") {
                Toast.makeText(applicationContext, "Llena los campos", Toast.LENGTH_LONG).show()
            } else {
                val myData = MyData()
                myData.contra = contra
                myData.usuario = usr
                listo.add(myData)
                val myAdapter = MyAdapter(listo, baseContext)
                listView.adapter = myAdapter
                editText.setText("")
                editText1.setText("")
                Toast.makeText(applicationContext, "Se agregó la contraseña", Toast.LENGTH_LONG)
                    .show()
            }
            return true
        }
        if (id == R.id.item2) {
            var i = 0
            for (inf in list) {
                if (myInfo.usuario == inf.usuario) {
                    list[i].contras = listo
                }
                i++
            }
            List2Json(myInfo, list)
            return true
        }
        if (id == R.id.item3) {
            val intent = Intent(this@Menu, Login::class.java)
            startActivity(intent)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun List2Json(info: MyInfo?, list: List<MyInfo?>?) {
        var gson: Gson? = null
        var json: String? = null
        gson = Gson()
        json = gson.toJson(list, ArrayList::class.java)
        if (json == null) {
            Log.d(TAG, "Error json")
        } else {
            Log.d(TAG, json)
            writeFile(json)
        }
        Toast.makeText(applicationContext, "Ok", Toast.LENGTH_LONG).show()
    }

    private fun writeFile(text: String): Boolean {
        var file: File? = null
        var fileOutputStream: FileOutputStream? = null
        try {
            file = getFile()
            fileOutputStream = FileOutputStream(file)
            fileOutputStream.write(text.toByteArray(StandardCharsets.UTF_8))
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
        return File(dataDir, Registro.archivo)
    }

}