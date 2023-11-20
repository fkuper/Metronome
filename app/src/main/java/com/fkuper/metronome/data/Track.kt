package com.fkuper.metronome.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fkuper.metronome.utils.NoteValue
import com.fkuper.metronome.utils.TimeSignature
import com.google.gson.annotations.SerializedName

@Entity(tableName = "tracks")
data class Track(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val artist: String,
    val title: String,
    val bpm: Int = 120,
    val timeSignature: TimeSignature = TimeSignature.FOUR_FOUR,
    val noteValue: NoteValue? = NoteValue.QUARTER
)

data class SpotifySearchResult(
    @SerializedName("tracks")
    val tracks: SpotifyTrackSearchResult
)

data class SpotifyTrackSearchResult(
    @SerializedName("items")
    val items: List<SpotifyTrack>
)

data class SpotifyTrack(
    @SerializedName("id")
    val spotifyId: String,
    @SerializedName("name")
    val title: String,
    @SerializedName("artists")
    val artists: List<SpotifyArtist>,
    @SerializedName("album")
    val album: SpotifyAlbum
)

data class SpotifyArtist(
    @SerializedName("name")
    val name: String
)

data class SpotifyAlbum(
    @SerializedName("images")
    val images: List<SpotifyCoverArt>
)

data class SpotifyCoverArt(
    @SerializedName("url")
    val url: String,
    @SerializedName("height")
    val height: Int,
    @SerializedName("width")
    val width: Int
)

data class SpotifyTrackAudioFeatures(
    @SerializedName("tempo")
    val bpm: Float,
    @SerializedName("time_signature")
    val timeSignature: Int
)

fun SpotifyTrackAudioFeatures.toMetronomeTrack(spotifyTrack: SpotifyTrack): Track {
    return Track(
        artist = spotifyTrack.artists.first().name,
        title = spotifyTrack.title,
        bpm = bpm.toInt(),
        timeSignature = TimeSignature.fromInt(timeSignature) ?: TimeSignature.FOUR_FOUR
    )
}