package com.fkuper.metronome.data


import com.google.gson.annotations.SerializedName
import java.util.Calendar
import java.util.Date

data class SpotifyWebApiAccessToken(
    @SerializedName("access_token")
    val token: String,
    @SerializedName("token_type")
    val tokenType: String,
    @SerializedName("expires_in")
    val expiresIn: Int
)

fun SpotifyWebApiAccessToken.toMyImpl(): MySpotifyWebApiAccessToken {
    return MySpotifyWebApiAccessToken(
        token = token,
        tokenType = tokenType,
        expiresIn = expiresIn
    )
}

data class MySpotifyWebApiAccessToken(
    val token: String,
    val tokenType: String,
    val expiresIn: Int,
    private val dateRequested: Date = Date()
) {
    val isValid: Boolean get() {
        val currentDate = Date()
        val validUntilDate = addSecondsTo(dateRequested, expiresIn)
        return !currentDate.after(validUntilDate)
    }

    private fun addSecondsTo(date: Date, seconds: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.SECOND, seconds)
        return calendar.time
    }
}