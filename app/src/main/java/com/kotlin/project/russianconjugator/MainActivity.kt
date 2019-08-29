package com.kotlin.project.russianconjugator

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.fasterxml.jackson.databind.ObjectMapper
import java.net.HttpURLConnection
import java.net.URL
import com.fasterxml.jackson.module.kotlin.*

val aspectCB = "СВ"
val aspectNCB = "НСВ"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val boton = findViewById<Button>(R.id.searchButton)
        val textVerb = findViewById<EditText>(R.id.editText_verb)

        boton.setOnClickListener {
            verEnPantalla(textVerb.text.toString())
        }



        val url = "https://en.wikipedia.org/w/api.php?action=query&prop=extracts&explaintext&format=json&titles=Monster"

        AsyncTaskHandleJson().execute(url)
    }

    inner class AsyncTaskHandleJson : AsyncTask<String,String,String>(){
        override fun doInBackground(vararg url: String?): String {
            var text: String
            val connection = URL(url[0]).openConnection() as HttpURLConnection
            try{
                connection.connect()
                text = connection.inputStream.use { it.reader().use{reader -> reader.readText()} }
            }finally {
                connection.disconnect()
            }

            return text
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            handleJson(result)
        }

    }

    private fun handleJson(jsonString: String?) {

        print(jsonString)
//        verEnPantalla(jsonString)


        val mapper = jacksonObjectMapper()
        data class Person(val name: String, val age: Int, val messages: List<String>)

        val json = """{"name": "Kolineer", "age": "26", "messages" : ["message a","message b"]}"""

        println("=== JSON to Kotlin Object ===")
        println("1- read String")
        var person: Person = mapper.readValue<Person>(json)
        println(person)

        println("2- read URL")
//        person = mapper.readValue<Person>(URL("http://kotlination.com/wp-content/uploads/2017/05/person.json"))
        println(person)
        verEnPantalla(person.toString())



    }

    private fun verEnPantalla(s: String?){
        val txt = findViewById<TextView>(R.id.aspect)
        txt.text = s
    }
}
