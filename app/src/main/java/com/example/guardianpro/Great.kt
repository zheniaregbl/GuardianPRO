package com.example.guardianpro

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

class Great {
    private val client = HttpClient()
    private val urlAuthorization = "https://2ahcf.localtonet.com/api/auth/"
    private val urlGetApplic = "https://2ahcf.localtonet.com/api/getapplication/"
    private val urlSetAccess = "https://2ahcf.localtonet.com/api/grant/"
    private val urlSetTime = "https://2ahcf.localtonet.com/api/settime/"

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