package com.example.viagemapp.api

import android.util.Log
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

object GeminiService {
    private const val apiKey = "AIzaSyAMmCjAhFWTuEzkJ9WNjsBASqaAxY94cLU"
    private const val TAG = "GeminiService"

    fun sugerirRoteiro(
        destino: String,
        onResult: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        val prompt =
            "Sugira um roteiro de viagem para $destino, incluindo dicas locais, culinária e pontos turísticos."
        val json = """
            {
              "contents": [
                {
                  "parts": [
                    { "text": "$prompt" }
                  ]
                }
              ]
            }
        """.trimIndent()

        val requestBody = json.toRequestBody("application/json".toMediaType())

        val client = OkHttpClient.Builder()
            .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
            .writeTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
            .build()

        val request = Request.Builder()
            .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=$apiKey")
            .post(requestBody)
            .build()


        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {
                Log.e(TAG, "Erro na requisição: ${e.message}", e)
                onError("Erro: ${e.message}")
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                val body = response.body?.string()
                Log.d(TAG, "Código HTTP: ${response.code}")
                Log.d(TAG, "Corpo da resposta: $body")

                if (!response.isSuccessful || body == null) {
                    onError("Erro ao buscar sugestões de roteiro")
                    return
                }
                try {
                    val resultado = JSONObject(body)
                        .getJSONArray("candidates")
                        .getJSONObject(0)
                        .getJSONObject("content")
                        .getJSONArray("parts")
                        .getJSONObject(0)
                        .getString("text")

                    onResult(resultado)
                } catch (e: Exception) {
                    Log.e(TAG, "Erro ao interpretar JSON: ${e.message}", e)
                    onError("Erro ao interpretar resposta: ${e.message}")
                }
            }
        })
    }
}
