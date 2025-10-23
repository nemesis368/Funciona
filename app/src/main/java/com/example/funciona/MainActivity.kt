package com.example.funciona


import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException

class MainActivity : AppCompatActivity() {

    // URL del servicio JSON
    private val url = "https://jsonplaceholder. typicode.com/posts"
    // Etiqueta para los logs
    private val TAG = "MainActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        downloadTask()
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

                    // ---- INICIO DEL CÓDIGO AGREGADO DE LA IMAGEN ----
                    // Bucle para recorrer cada objeto en el array
                    for (i in 0 until jsonArray.length()) {
                        // Obtenemos el objeto JSON en la posición 'i'
                        val jsonObject = jsonArray.getJSONObject(i)

                        // Sacamos los datos de cada objeto
                        val userId = jsonObject.getInt("userId")
                        val id = jsonObject.getInt("id")
                        val title = jsonObject.getString("title")
                        val body = jsonObject.getString("body")

                        // Imprimimos los datos en el Logcat para verificar
                        Log.d(TAG, "Post #$id (Usuario: $userId): $title")
                    }
                    // ---- FIN DEL CÓDIGO AGREGADO ----

                } catch (e: JSONException) {
                    Log.e(TAG, "Error al procesar el JSON: ${e.message}")
                }
            },
            { error ->
                Log.e(TAG, "Error en Volley: ${error.message}")
            })

        queue.add(stringRequest)
    }
}
