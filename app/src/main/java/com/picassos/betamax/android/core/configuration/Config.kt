package com.picassos.betamax.android.core.configuration

import com.picassos.betamax.android.BuildConfig

object Config {
    // configuration
    const val LAUNCH_TIMEOUT = 200L
    const val SLIDER_INTERVAL = 5000L
    const val PLAYER_REPLAY_DURATION = 10000L
    const val PLAYER_FORWARD_DURATION = 10000L

    // exoplayer
    const val MIN_BUFFER_DURATION = 32 * 1024
    const val MAX_BUFFER_DURATION = 64 * 1024
    const val MIN_PLAYBACK_START_BUFFER = 1024
    const val MIN_PLAYBACK_RESUME_BUFFER = 1024

    // build preferences
    const val BUILD_TYPE = BuildConfig.BUILD_TYPE
}