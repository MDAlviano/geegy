package com.alvin.geegy.http

import java.net.HttpURLConnection
import java.net.URL

object HttpHandler {

    private const val BASE_URL = "http://10.0.2.2:5000/api"

    fun getRequest(endpoint: String): String {
        val url = URL("$BASE_URL/$endpoint")
        val connection = url.openConnection() as HttpURLConnection
        return try {
            connection.requestMethod = "GET"
            connection.setRequestProperty("Content-Type", "application/json")

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                connection.inputStream.bufferedReader().use { it.readText() }
            } else {
                connection.errorStream?.bufferedReader().use { it?.readText() } ?: "${connection.responseCode}, ${connection.responseMessage}"
            }
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }

}