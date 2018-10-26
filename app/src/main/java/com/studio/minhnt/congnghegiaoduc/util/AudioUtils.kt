package com.studio.minhnt.congnghegiaoduc.util

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import java.io.IOException

@Suppress("DEPRECATION")
class AudioUtils(private val context: Context) {
    private var mediaPlayer: MediaPlayer? = null
    private var ringtoneMediaPlayer: MediaPlayer? = null

    private var onPlayCompleteListener: OnPlayCompleteListener? = null
    private var onPlayErrorListener: OnPlayErrorListener? = null
    private var onPrepareCompleteListener: OnPrepareCompleteListener? = null
    private var audioManager: AudioManager? = null
    private var savedAudioMode = AudioManager.MODE_INVALID

    init {
        initMediaPlayer()
        initAudioManager()
    }

    private fun initConfigMediaPlayer(isNeedTracking: Boolean = true): MediaPlayer? {
        val player = MediaPlayer()
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            if (isNeedTracking) {
//                player.setAudioAttributes(AudioAttributes.Builder()
//                        .setUsage(AudioAttributes.USAGE_VOICE_COMMUNICATION)
//                        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
//                        .build())
//                player.setAudioStreamType(AudioManager.STREAM_VOICE_CALL)
//            } else {
//                player.setAudioAttributes(AudioAttributes.Builder()
//                        .setUsage(AudioAttributes.USAGE_MEDIA)
//                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
//                        .build())
//                player.setAudioStreamType(AudioManager.STREAM_MUSIC)
//            }
//        } else {
//            if (isNeedTracking) {
//                player.setAudioStreamType(AudioManager.STREAM_VOICE_CALL)
//            } else {
//                player.setAudioStreamType(AudioManager.STREAM_MUSIC)
//            }
//        }

        return player
    }

    private fun initRingTonePlayer(isIncoming: Boolean) {
        if (ringtoneMediaPlayer == null) {
            ringtoneMediaPlayer = initConfigMediaPlayer(!isIncoming)
        }

        if (ringtoneMediaPlayer!!.isPlaying) {
            ringtoneMediaPlayer?.reset()
        }

        ringtoneMediaPlayer?.setOnPreparedListener { ringtoneMediaPlayer ->
            ringtoneMediaPlayer.start()
        }

        ringtoneMediaPlayer?.setOnErrorListener { _, _, _ ->
            onPlayErrorListener?.onError()
            true
        }
    }

    private fun initAudioManager() {
        audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            audioManager!!.mode = AudioManager.MODE_IN_CALL
        } else {
            audioManager!!.mode = savedAudioMode
        }
    }

    private fun initMediaPlayer() {
        mediaPlayer = initConfigMediaPlayer()
        mediaPlayer?.setOnPreparedListener { mediaPlayer ->
            onPrepareCompleteListener?.onPrepareComplete()
            mediaPlayer.start()
        }
        mediaPlayer?.setOnCompletionListener { _ ->
            mediaPlayer?.release()
            mediaPlayer = null
            onPlayCompleteListener?.onPlayComplete()
        }

        mediaPlayer?.setOnErrorListener { _, _, _ ->
            onPlayErrorListener?.onError()
            true
        }
    }

    private fun resetMediaPlayer() {
        if (mediaPlayer == null) {
            initMediaPlayer()
        }
        if (mediaPlayer!!.isPlaying) {
            mediaPlayer?.reset()
        }
    }

    fun playRingtone(name: String, isIncoming: Boolean) {
        initRingTonePlayer(isIncoming)

        try {
            val alert = Uri.parse("android.resource://${context.packageName}/raw/$name")
            ringtoneMediaPlayer?.setDataSource(context, alert)
            ringtoneMediaPlayer?.isLooping = true
            ringtoneMediaPlayer?.prepare()
        } catch (e: IOException) {

        }
    }

    fun stopRingtone() {
        ringtoneMediaPlayer?.release()
        ringtoneMediaPlayer = null
    }

    fun playSoundRaw(name: String, loop: Boolean) {
        resetMediaPlayer()
        try {
            val alert = Uri.parse("android.resource://${context.packageName}/raw/$name")
            mediaPlayer?.setDataSource(context, alert)
            mediaPlayer?.isLooping = loop
            mediaPlayer?.prepare()
        } catch (e: IOException) {

        }
    }

    fun playSoundUri(uri: String) {
        resetMediaPlayer()
        try {
            mediaPlayer?.setDataSource(uri)
            mediaPlayer?.isLooping = false
            mediaPlayer?.prepareAsync()
        } catch (e: Exception) {
            onPlayErrorListener?.onError()
        }
    }

    fun clear() {
        if (mediaPlayer != null) {
            clear(mediaPlayer)
            mediaPlayer = null
        }

        if (ringtoneMediaPlayer != null) {
            clear(ringtoneMediaPlayer)
            ringtoneMediaPlayer = null
        }
    }

    private fun clear(mediaPlayer: MediaPlayer?) {
        if (mediaPlayer != null) {
            mediaPlayer.setOnPreparedListener(null)
            mediaPlayer.setOnErrorListener(null)
            if (mediaPlayer.isPlaying)
                mediaPlayer.stop()
            mediaPlayer.release()
        }
    }

    fun setOnPlayCompleteListener(onPlayCompleteListener: OnPlayCompleteListener?) {
        this.onPlayCompleteListener = onPlayCompleteListener
    }

    fun setOnPlayErrorListener(onPlayErrorListener: OnPlayErrorListener) {
        this.onPlayErrorListener = onPlayErrorListener
    }

    fun setOnPrepareCompleteListener(onPrepareCompleteListener: OnPrepareCompleteListener?) {
        this.onPrepareCompleteListener = onPrepareCompleteListener
    }

    @Suppress("DEPRECATION")
    fun setAudioFocus(setFocus: Boolean) {
        if (audioManager != null) {
            if (setFocus) {
                savedAudioMode = audioManager!!.mode
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val playbackAttributes = AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_VOICE_COMMUNICATION)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                            .build()
                    val focusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)
                            .setAudioAttributes(playbackAttributes)
                            .setAcceptsDelayedFocusGain(true)
                            .setOnAudioFocusChangeListener { }
                            .build()
                    audioManager!!.requestAudioFocus(focusRequest)
                } else {
                    audioManager!!.requestAudioFocus(null, AudioManager.STREAM_VOICE_CALL,
                            AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)
                }
                audioManager!!.mode = AudioManager.MODE_IN_CALL
            } else {
                audioManager!!.mode = savedAudioMode
                audioManager!!.abandonAudioFocus(null)
            }
        }
    }

    interface OnPlayCompleteListener {
        fun onPlayComplete()
    }

    interface OnPlayErrorListener {
        fun onError()
    }

    interface OnPrepareCompleteListener {
        fun onPrepareComplete()
    }
}
