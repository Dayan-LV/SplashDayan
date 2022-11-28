package com.example.splashdayan

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mysplash.json.MyInfo
import com.example.splashdayan.MyAdapter.MyAdapter
import com.example.splashdayan.des.MyDesUtil
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

    var myDesUtil = MyDesUtil().addStringKeyBase64(Registro.KEY)
    var bandera = false

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

        button.setEnabled(false);
        button1.setEnabled(false);
        if(listo.isEmpty()){
            Toast.makeText(getApplicationContext(), "Para agregar una contraseña de clic en el menú o en el boton +", Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(), "Escriba en los campos", Toast.LENGTH_LONG).show();
        }
        Toast.makeText(getApplicationContext(), "Para modificar o eliminar una contraseña de click en ella", Toast.LENGTH_LONG).show();

        listView.onItemClickListener =
            OnItemClickListener { adapterView, view, i, l ->
                editText.setText(listo[i].usuario)
                editText1.setText(listo[i].contra)
                pos = i
                button.setEnabled(true);
                button1.setEnabled(true);
                Toast.makeText(getApplicationContext(), "Para guardar los cambios de click en guardar cambios", Toast.LENGTH_LONG).show();
            }
        button.setOnClickListener {
            listo.removeAt(pos)
            myInfo.contras = listo
            val myAdapter = MyAdapter(listo, baseContext)
            listView.adapter = myAdapter
            editText.setText("")
            editText1.setText("")
            Toast.makeText(applicationContext, "Se eliminó la contraseña", Toast.LENGTH_LONG).show()
            button.setEnabled(false);
            button1.setEnabled(false);
        }
        button1.setOnClickListener {
            val usr = editText.text.toString()
            val contra = editText1.text.toString()
            if(usr == "" || contra == ""){
                Toast.makeText(applicationContext, "Llene los campos", Toast.LENGTH_LONG).show();
            }else{
                listo.get(pos).usuario = usr
                listo.get(pos).contra = contra
                myInfo.contras = listo
                var myAdapter = MyAdapter(listo, baseContext)
                listView.adapter = myAdapter;
                editText.setText("");
                editText1.setText("");
                Toast.makeText(applicationContext, "Se modificó la contraseña", Toast.LENGTH_LONG).show();
                button.isEnabled = false;
                button1.isEnabled = false;
            }
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