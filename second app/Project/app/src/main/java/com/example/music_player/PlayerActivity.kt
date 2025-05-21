package com.example.music_player

import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PlayerActivity : AppCompatActivity() {

    private lateinit var playPause: Button
    private lateinit var backBtn: Button
    private lateinit var seekBar: SeekBar
    private lateinit var songTitle: TextView
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        playPause = findViewById(R.id.PlayPauseButton)
        backBtn = findViewById(R.id.BackToMainButton)
        seekBar = findViewById(R.id.seekBar)
        songTitle = findViewById(R.id.textView3)

        val mediaPlayer = MusicPlayerHolder.mediaPlayer
        val index = MusicPlayerHolder.currentIndex

        songTitle.text = MusicPlayerHolder.songNames[index]

        mediaPlayer?.let {
            seekBar.max = it.duration

            handler.post(object : Runnable {
                override fun run() {
                    if (it.isPlaying) {
                        seekBar.progress = it.currentPosition
                    }
                    handler.postDelayed(this, 1000)
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

        playPause.setOnClickListener {
            mediaPlayer?.let {
                if (it.isPlaying) {
                    it.pause()
                    playPause.text = "Play"
                } else {
                    it.start()
                    playPause.text = "Pause"
                }
            }
        }

        backBtn.setOnClickListener {
            finish()
        }

        val albumArt: ImageView = findViewById(R.id.AlbumArt)

        val retriever = MediaMetadataRetriever()
        val fd = resources.openRawResourceFd(MusicPlayerHolder.songList[MusicPlayerHolder.currentIndex])
        retriever.setDataSource(fd.fileDescriptor, fd.startOffset, fd.length)
        fd.close()

        val art = retriever.embeddedPicture
        if (art != null) {
            val bitmap = BitmapFactory.decodeByteArray(art, 0, art.size)
            albumArt.setImageBitmap(bitmap)
        } else {
            albumArt.setImageResource(R.drawable.default_album_art) // fallback image
        }
        retriever.release()

        val instructButton: Button = findViewById(R.id.downloadspotify)

        instructButton.setOnClickListener {
            val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val popupView = inflater.inflate(R.layout.activity_popupwindow, null)

            val songNameTextView: TextView = popupView.findViewById(R.id.DonwloadSpotifyText)
            songNameTextView.text = MusicPlayerHolder.songNames[MusicPlayerHolder.currentIndex]

            val instructWindow = PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
            )

            instructWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            instructWindow.showAtLocation(
                popupView,
                Gravity.BOTTOM,
                10,
                800
            )

            val closeButton: Button = popupView.findViewById(R.id.CloseButton)
            closeButton.setOnClickListener {
                instructWindow.dismiss()
            }
        }
    }

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }
}

