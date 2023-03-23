package com.example.guardianpro

import com.google.gson.annotations.SerializedName

/*
data class Applic(
    val id: Int,
    val lastName: String,
    val name: String,
    val secondName: String,
    val date: String,
    val division: String,
    val timeIn: String,
    val timeOut: String?,
    val access: Boolean
): java.io.Serializable
*/

data class Applic (

    @SerializedName("id"            ) var id            : Int?     = null,
    @SerializedName("lastName"      ) var lastName      : String?  = null,
    @SerializedName("firstName"     ) var name     : String?  = null,
    @SerializedName("fatherName"    ) var secondName    : String?  = null,
    @SerializedName("dateOfArrival" ) var date : String?  = null,
    @SerializedName("department"    ) var division    : String?  = null,
    @SerializedName("meetingTime"   ) var timeIn   : String?  = null,
    @SerializedName("departureTime" ) var timeOut : String?  = null,
    @SerializedName("grants"        ) var access        : Boolean? = null

)