package com.example.guardianpro

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

class Great {
    private val client = HttpClient()
    private val urlAuthorization = "https://c06f-80-76-43-249.eu.ngrok.io/api/auth/"
    private val urlGetApplic = "https://c06f-80-76-43-249.eu.ngrok.io/api/getapplication/"
    private val urlSetAccess = "https://c06f-80-76-43-249.eu.ngrok.io/api/grant/"
    private val urlSetTime = "https://c06f-80-76-43-249.eu.ngrok.io/api/settime/"

    suspend fun great(code: String, password: String): Int{
        val response: HttpResponse = client.post(urlAuthorization) {
            header("ngrok-skip-browser-warning", "1")
            setBody("{'code':${code},'password':'${password}'}")
        }

        return response.status.value
    }

    suspend fun greater(): String {
        val response: HttpResponse = client.get(urlGetApplic)

        return response.body()
    }

    suspend fun access(id: Int?, grants: Boolean): Int {
        val response: HttpResponse = client.post(urlSetAccess) {
            header("ngrok-skip-browser-warning", "1")
            setBody("{'id':${id},'grants':'${grants}'}")
        }

        return response.status.value
    }

    suspend fun setTimeOut(id: Int?, time: String?): Int{
        val response: HttpResponse = client.post(urlSetTime) {
            header("ngrok-skip-browser-warning", "1")
            setBody("{'id':${id},'time':'${time}'}")
        }

        return response.status.value
    }
}