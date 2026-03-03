package com.dcf2.orbita.model

import com.google.gson.annotations.SerializedName

data class DonkiModel(
    @SerializedName("startTime") val startTime: String?,
    @SerializedName("note") val note: String?
)