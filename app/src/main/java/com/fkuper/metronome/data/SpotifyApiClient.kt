package com.fkuper.metronome.data

import com.fkuper.metronome.BuildConfig
import com.fkuper.metronome.utils.ResultCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.Base64

object SpotifyApiClient {

    private const val AUTH_URL =
        BuildConfig.SPOTIFY_WEB_API_AUTH_URL
    private const val URL =
        "${BuildConfig.SPOTIFY_WEB_API_URL}/${BuildConfig.SPOTIFY_WEB_API_VERSION}/"

    private val interceptor = HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BASIC)
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    val auth : SpotifyApiAuth by lazy {
        Retrofit.Builder()
            .baseUrl(AUTH_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(ResultCallAdapterFactory())
            .client(client)
            .build()
            .create(SpotifyApiAuth::class.java)
    }

    val api : SpotifyWebApi by lazy {
        Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(ResultCallAdapterFactory())
            .client(client)
            .build()
            .create(SpotifyWebApi::class.java)
    }

}

interface SpotifyApiAuth {

    @FormUrlEncoded
    @POST("api/token")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    suspend fun getAccessToken(
        @Field("grant_type") grant: String = "client_credentials",
        @Header("Authorization") auth: String = authCode,
    ): Result<SpotifyWebApiAccessToken>

    private val authCode: String get() {
        val code = "$CLIENT_ID:$API_SECRET"
        return "Basic ${Base64.getEncoder().encodeToString(code.toByteArray())}"
    }

    companion object {
        private const val CLIENT_ID = BuildConfig.SPOTIFY_WEB_API_CLIENT_ID
        private const val API_SECRET = BuildConfig.SPOTIFY_WEB_API_SECRET
    }

}

interface SpotifyWebApi {

    @GET("search")
    suspend fun search(
        @Header("Authorization") auth: String,
        @Query("q") query: String,
        @Query("type") type: String = "track"
    ): Result<SpotifySearchResult>

    @GET("audio-features/{id}")
    suspend fun getTracksAudioFeatures(
        @Header("Authorization") auth: String,
        @Path("id") tracksSpotifyId: String
    ): Result<SpotifyTrackAudioFeatures>

}