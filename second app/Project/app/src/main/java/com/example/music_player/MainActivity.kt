package com.example.music_player

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.PopupWindow
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var playPause: Button
    private lateinit var next: Button
    private lateinit var prev: Button
    private lateinit var goToPlayer: Button
    private lateinit var listView: ListView
    private lateinit var title: TextView
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var seekBar: SeekBar

    private fun setupSeekBar(mediaPlayer: MediaPlayer) {
        seekBar.max = mediaPlayer.duration

        handler.post(object : Runnable {
            override fun run() {
                try {
                    seekBar.progress = mediaPlayer.currentPosition
                    handler.postDelayed(this, 500)
                } catch (_: Exception) {}
            }
        })

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        playPause = findViewById(R.id.MainPlayPauseButton)
        next = findViewById(R.id.NextSong)
        prev = findViewById(R.id.PreviousSong)
        goToPlayer = findViewById(R.id.GotoSecondPage)
        listView = findViewById(R.id.Songlist)
        title = findViewById(R.id.SongTitleView)

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, MusicPlayerHolder.songNames)
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            MusicPlayerHolder.prepare(this, position)
            MusicPlayerHolder.mediaPlayer?.start()
            updateTitle()
            playPause.text = "Pause"
            MusicPlayerHolder.mediaPlayer?.let { setupSeekBar(it) }

            MusicPlayerHolder.lastSelectedSongName = MusicPlayerHolder.songNames[position]
        }


        playPause.setOnClickListener {
            MusicPlayerHolder.mediaPlayer?.let {
                if (it.isPlaying) {
                    it.pause()
                    playPause.text = "Play"
                } else {
                    it.start()
                    playPause.text = "Pause"
                }
            }
        }

        next.setOnClickListener {
            val nextIndex = (MusicPlayerHolder.currentIndex + 1) % MusicPlayerHolder.songList.size
            MusicPlayerHolder.prepare(this, nextIndex)
            MusicPlayerHolder.mediaPlayer?.start()
            updateTitle()
            playPause.text = "Pause"
            MusicPlayerHolder.mediaPlayer?.let { setupSeekBar(it) }
        }

        prev.setOnClickListener {
            val prevIndex = (MusicPlayerHolder.currentIndex - 1 + MusicPlayerHolder.songList.size) % MusicPlayerHolder.songList.size
            MusicPlayerHolder.prepare(this, prevIndex)
            MusicPlayerHolder.mediaPlayer?.start()
            updateTitle()
            playPause.text = "Pause"
            MusicPlayerHolder.mediaPlayer?.let { setupSeekBar(it) }
        }

        goToPlayer.setOnClickListener {
            startActivity(Intent(this, PlayerActivity::class.java))
        }

        updateTitle()

        seekBar = findViewById(R.id.SeekBar)

        val mediaPlayer = MusicPlayerHolder.mediaPlayer

        mediaPlayer?.let {
            seekBar.max = it.duration

            handler.post(object : Runnable {
                override fun run() {
                    try {
                        seekBar.progress = it.currentPosition
                        handler.postDelayed(this, 500)
                    } catch (_: Exception) {}
                }
            })

            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    if (fromUser) {
                        it.seekTo(progress)
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }
    }

    private fun updateTitle() {
        title.text = MusicPlayerHolder.songNames[MusicPlayerHolder.currentIndex]
    }

    override fun onResume() {
        super.onResume()
        MusicPlayerHolder.mediaPlayer?.let { setupSeekBar(it) }
    }
}

