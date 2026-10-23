package com.example.lab10.data

import com.google.gson.annotations.SerializedName

data class DeviceModel(
    @SerializedName("id")
    var id: String = "",
    @SerializedName("name")
    var name: String = "",
    @SerializedName("data")
    var data: DeviceData? = null
)

data class DeviceData(
    @SerializedName("color")
    var color: String? = null,
    @SerializedName("price")
    var price: Double? = null
)