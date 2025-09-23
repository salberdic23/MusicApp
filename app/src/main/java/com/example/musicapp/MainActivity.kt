package com.example.musicapp

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.concurrent.TimeUnit
import android.media.AudioManager

class MainActivity : AppCompatActivity() {

    private lateinit var btnPlayPause: ImageButton
    private lateinit var seekBarProgress: SeekBar
    private lateinit var seekBarVolume: SeekBar
    private lateinit var tvElapsed: TextView
    private lateinit var tvRemaining: TextView
    private lateinit var lyricsView: TextView
    private var mediaPlayer: MediaPlayer? = null
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var runnable: Runnable
    private var wasPlayingBeforeRotation = false
    private var savedPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar vistas
        lyricsView = findViewById(R.id.Lyrics) ?: return
        btnPlayPause = findViewById(R.id.btnPlayPause)
        seekBarProgress = findViewById(R.id.seekBarProgress)
        seekBarVolume = findViewById(R.id.seekBarVolume)
        tvElapsed = findViewById(R.id.tvElapsed)
        tvRemaining = findViewById(R.id.tvRemaining)

        // Cargar letra desde assets
        lyricsView.text = loadLyricsFromAssets("lyrics.txt")

        // Volumen
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

        // MediaPlayer
        mediaPlayer = MediaPlayer.create(this, R.raw.music)
        mediaPlayer?.let { mp ->
            seekBarProgress.max = mp.duration
        }

        // Restaurar estado si hay
        savedPosition = savedInstanceState?.getInt("currentPosition") ?: 0
        wasPlayingBeforeRotation = savedInstanceState?.getBoolean("isPlaying") ?: false
        mediaPlayer?.seekTo(savedPosition)
        seekBarProgress.progress = savedPosition
        tvElapsed.text = formatTime(savedPosition)
        mediaPlayer?.let {
            val remaining = it.duration - savedPosition
            tvRemaining.text = "-${formatTime(remaining)}"
        }

        if (wasPlayingBeforeRotation) {
            mediaPlayer?.start()
            btnPlayPause.setImageResource(R.drawable.ic_pause)
            updateSeekBar()
        }

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
            seekBarProgress.progress = 0
            tvElapsed.text = "0:00"
            tvRemaining.text = "-${formatTime(it.duration)}"
        }

        // Control manual del SeekBar
        seekBarProgress.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayer?.seekTo(progress)
                    tvElapsed.text = formatTime(progress)
                    mediaPlayer?.let {
                        val remaining = it.duration - progress
                        tvRemaining.text = "-${formatTime(remaining)}"
                    }
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                handler.removeCallbacks(runnable)
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                updateSeekBar()
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mediaPlayer?.let {
            outState.putInt("currentPosition", it.currentPosition)
            outState.putBoolean("isPlaying", it.isPlaying)
        }
    }

    private fun updateSeekBar() {
        mediaPlayer?.let { mp ->
            val current = mp.currentPosition
            seekBarProgress.progress = current
            tvElapsed.text = formatTime(current)
            tvRemaining.text = "-${formatTime(mp.duration - current)}"

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

    private fun loadLyricsFromAssets(filename: String): String {
        return try {
            assets.open(filename).bufferedReader().use { it.readText() }
        } catch (e: Exception) {
            "No se pudo cargar la letra."
        }
    }
}
