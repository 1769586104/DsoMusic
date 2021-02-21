package com.dirror.music.util

import org.jetbrains.annotations.TestOnly

/**
 * 配置
 */
object Config {

    const val UID = "long_uid"

    const val PAUSE_SONG_AFTER_UNPLUG_HEADSET = "pause_song_after_unplug_headset"

    const val LOCAL_PLAYLIST = "local_playlist"

    // 是否在移动数据下播放
    const val PLAY_ON_MOBILE = "boolean_play_on_mobile"

    // 播放历史
    const val PLAY_HISTORY = "list_play_history"

    // 播放模式
    const val PLAY_MODE = "int_play_mode"

    // 音乐搜索引擎
    const val SEARCH_ENGINE = "int_search_engine"

    // 网易云 Cookie
    const val CLOUD_MUSIC_COOKIE = "string_cloud_music_cookie"

    // 过滤录音
    const val FILTER_RECORD = "boolean_filter_record"

    // 为本地音乐自动匹配网络歌词
    const val PARSE_INTERNET_LYRIC_LOCAL_MUSIC = "boolean_parse_internet_lyric_local_music"

    // 跳过错误原因，自动播放下一首
    const val SKIP_ERROR_MUSIC = "boolean_skip_error_music"

    // 歌词翻译
    const val LYRIC_TRANSLATION = "boolean_lyric_translation"

    // 智能过滤
    const val SMART_FILTER = "smart_filter"

    // 首页导航栏适配
    const val PARSE_NAVIGATION = "parse_home_navigation"

    // 歌单滑动动画
    const val PLAYLIST_SCROLL_ANIMATION = "boolean_playlist_scroll_animation"

    // 深色主题
    const val DARK_THEME = "boolean_dark_theme"

    // 全局背景
    @Deprecated("2.7.0-alpha 废除字段")
    const val THEME_BACKGROUND = "string_theme_background"

    // 是否开启句子推荐
    const val SENTENCE_RECOMMEND = "boolean_sentence_recommend"

    // 双列歌单
    const val DOUBLE_ROW_MY_PLAYLIST = "boolean_double_row_my_playlist"

    // MainActivity 背景
    const val MAIN_ACTIVITY_BACKGROUND = "string_main_activity_background"

    // 自定义背景
    const val APP_THEME_BACKGROUND = "bitmap_app_theme_background"

    // 允许与其他应用同时播放
    const val ALLOW_AUDIO_FOCUS = "boolean_allow_audio_focus"
}