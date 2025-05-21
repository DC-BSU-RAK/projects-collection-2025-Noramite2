package com.example.music_player

import android.content.Context
import android.media.MediaPlayer

object MusicPlayerHolder {
    var mediaPlayer: MediaPlayer? = null
    var currentIndex: Int = 0
    var isPrepared: Boolean = false
    var songList = listOf(
        R.raw.bad_dream_baby,
        R.raw.dancing_with_my_phone,
        R.raw.infinity,
        R.raw.komm_susser_tod_directors,
        R.raw.why_do_with_miku
    )

    var lastSelectedSongName: String = ""

    var songNames = listOf(
        "Bad Dream Baby",
        "Dancing with my phone",
        "Infinity",
        "KOMM, SÜSSER TOD (Director's Edit)",
        "Why Do (with Hatsune Miku)"
    )

    fun prepare(context: Context, index: Int) {
        release()
        currentIndex = index
        mediaPlayer = MediaPlayer.create(context, songList[index])
        isPrepared = true
    }

    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
        isPrepared = false
    }
}
