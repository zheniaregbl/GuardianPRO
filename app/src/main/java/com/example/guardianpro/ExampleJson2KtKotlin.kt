package com.example.guardianpro

import com.google.gson.annotations.SerializedName

data class ExampleJson2KtKotlin(
    @SerializedName("applications" ) var applications : ArrayList<Applic> = arrayListOf()
)
