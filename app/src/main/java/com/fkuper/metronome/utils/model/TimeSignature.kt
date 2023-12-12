package com.fkuper.metronome.utils.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class TimeSignature(val upper: Int, val lower: Int) : Parcelable {
    ONE_FOUR(1, 4),
    TWO_FOUR(2, 4),
    THREE_FOUR(3, 4),
    FOUR_FOUR(4, 4),
    FIVE_FOUR(5, 4),
    SIX_FOUR(6, 4),
    SEVEN_FOUR(7, 4),
    EIGHT_FOUR(8, 4),
    NINE_FOUR(9, 4),
    TEN_FOUR(10, 4),
    ELEVEN_FOUR(11, 4),
    TWELVE_FOUR(12, 4),
    THIRTEEN_FOUR(13, 4);

    companion object {

        /**
         * Used for converting from Spotify's Web API time signature notation to ours.
         *
         * See here for more info:
         * https://developer.spotify.com/documentation/web-api/reference/get-several-audio-features
         */
        fun fromInt(input: Int): TimeSignature? {
            return when (input) {
                3 -> THREE_FOUR
                4 -> FOUR_FOUR
                5 -> FIVE_FOUR
                6 -> SIX_FOUR
                7 -> SEVEN_FOUR
                else -> null
            }
        }
    }
}
