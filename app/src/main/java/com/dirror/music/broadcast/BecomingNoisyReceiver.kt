package com.dirror.music.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import com.dirror.music.MyApp

/**
 * 监听耳机拔出
 * 监听噪音
 */
class BecomingNoisyReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == AudioManager.ACTION_AUDIO_BECOMING_NOISY) {
            // Pause the playback
            MyApp.musicController.value?.pause()
        }
    }

}
