package com.example.funciona

import android.os.Bundle
import android.util.Log
import android.widget.TextView // <-- IMPORTADO
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.button.MaterialButton // <-- IMPORTADO
import org.json.JSONArray
import org.json.JSONException

class MainActivity : AppCompatActivity() {

    // URL del servicio JSON
    private val url = "https://jsonplaceholder.typicode.com/posts"
    // Etiqueta para los logs
    private val TAG = "MainActivity"

    // Declarar las vistas
    private lateinit var button: MaterialButton
    private lateinit var titleTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // --- INICIO DE CAMBIOS ---

        // 1. Encontrar las vistas por su ID
        button = findViewById(R.id.btn_fetch_json)
        titleTextView = findViewById(R.id.tv_title)

        // 2. Configurar el listener del botón
        button.setOnClickListener {
            Log.d(TAG, "Botón presionado. Iniciando descarga...")
            titleTextView.text = "Cargando..." // Feedback visual
            downloadTask() // Llamar a la función al hacer clic
        }

        // 3. Ya no llamamos a downloadTask() automáticamente
        // downloadTask() // <-- Se elimina de aquí

        // --- FIN DE CAMBIOS ---
    }

    /**
     * Función para realizar una petición GET y procesar un JSONArray.
     */
    private fun downloadTask() {
        val queue = Volley.newRequestQueue(this)

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                try {
                    val jsonArray = JSONArray(response)

                    // ---- CÓDIGO MEJORADO ----
                    // Bucle para recorrer cada objeto en el array
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val id = jsonObject.getInt("id")
                        val title = jsonObject.getString("title")

                        // Imprimimos en Logcat
                        Log.d(TAG, "Post #$id: $title")

                        // Mostramos el TÍTULO DEL PRIMER POST en el TextView
                        if (i == 0) {
                            titleTextView.text = title
                        }
                    }
                    // ---- FIN DEL CÓDIGO MEJORADO ----

                } catch (e: JSONException) {
                    Log.e(TAG, "Error al procesar el JSON: ${e.message}")
                    titleTextView.text = "Error de JSON" // Mostrar error
                }
            },
            { error ->
                Log.e(TAG, "Error en Volley: ${error.message}")
                titleTextView.text = "Error de Red" // Mostrar error
            })

        queue.add(stringRequest)
    }
}