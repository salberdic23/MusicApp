package com.example.musicapp

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.concurrent.TimeUnit
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.widget.ImageView
import android.media.AudioManager
import android.widget.SeekBar

class MainActivity : AppCompatActivity() {

    private lateinit var btnPlayPause: ImageButton
    private lateinit var progressBar: ProgressBar
    private lateinit var tvElapsed: TextView
    private lateinit var tvRemaining: TextView

    private var mediaPlayer: MediaPlayer? = null
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var runnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bgImage = findViewById<ImageView?>(R.id.bgBlur)
        bgImage?.setImageResource(R.drawable.cover)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val blur = RenderEffect.createBlurEffect(25f, 25f, Shader.TileMode.CLAMP)
            bgImage?.setRenderEffect(blur)
        }
        val seekBarVolume = findViewById<SeekBar>(R.id.seekBarVolume)
        val audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        seekBarVolume.max = maxVolume
        seekBarVolume.progress = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        seekBarVolume.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        btnPlayPause = findViewById(R.id.btnPlayPause)
//        progressBar = findViewById(R.id.progressBar)
        tvElapsed = findViewById(R.id.tvElapsed)
        tvRemaining = findViewById(R.id.tvRemaining)

        mediaPlayer = MediaPlayer.create(this, R.raw.music)
//        progressBar.max = mediaPlayer?.duration ?: 100

        btnPlayPause.setOnClickListener {
            mediaPlayer?.let { mp ->
                if (mp.isPlaying) {
                    mp.pause()
                    btnPlayPause.setImageResource(R.drawable.ic_play)
                } else {
                    mp.start()
                    btnPlayPause.setImageResource(R.drawable.ic_pause)
                    updateSeekBar()
                }
            }
        }

        mediaPlayer?.setOnCompletionListener {
            btnPlayPause.setImageResource(R.drawable.ic_play)
            progressBar.progress = 0
            tvElapsed.text = "0:00"
            tvRemaining.text = "-${formatTime(it.duration)}"
        }

        // Inicializar vistas
        btnPlayPause = findViewById(R.id.btnPlayPause)
//        progressBar = findViewById(R.id.progressBar)
        tvElapsed = findViewById(R.id.tvElapsed)
        tvRemaining = findViewById(R.id.tvRemaining)

        // MediaPlayer
        mediaPlayer = MediaPlayer.create(this, R.raw.music)
//        mediaPlayer?.let { mp -> progressBar.max = mp.duration }

        // Botón Play/Pause
        btnPlayPause.setOnClickListener {
            mediaPlayer?.let { mp ->
                if (mp.isPlaying) {
                    mp.pause()
                    btnPlayPause.setImageResource(R.drawable.ic_play)
                } else {
                    mp.start()
                    btnPlayPause.setImageResource(R.drawable.ic_pause)
                    updateSeekBar()
                }
            }
        }

        // Fin de canción
        mediaPlayer?.setOnCompletionListener {
            btnPlayPause.setImageResource(R.drawable.ic_play)
            progressBar.progress = 0
            tvElapsed.text = "0:00"
            tvRemaining.text = "-${formatTime(it.duration)}"
        }
    }

    private fun updateSeekBar() {
        mediaPlayer?.let { mp ->
//            progressBar.progress = mp.currentPosition
            val elapsed = mp.currentPosition
            val remaining = mp.duration - elapsed
            tvElapsed.text = formatTime(elapsed)
            tvRemaining.text = "-${formatTime(remaining)}"

            if (mp.isPlaying) {
                runnable = Runnable { updateSeekBar() }
                handler.postDelayed(runnable, 1000)
            }
        }
    }

    private fun formatTime(millis: Int): String {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(millis.toLong())
        val seconds = TimeUnit.MILLISECONDS.toSeconds(millis.toLong()) % 60
        return String.format("%d:%02d", minutes, seconds)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
