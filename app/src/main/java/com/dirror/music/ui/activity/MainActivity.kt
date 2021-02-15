package com.dirror.music.ui.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dirror.music.MyApplication
import com.dirror.music.R
import com.dirror.music.broadcast.HeadsetChangeReceiver
import com.dirror.music.databinding.ActivityMainBinding
import com.dirror.music.music.standard.SongPicture
import com.dirror.music.ui.base.BaseActivity
import com.dirror.music.ui.dialog.PlaylistDialog
import com.dirror.music.ui.viewmodel.MainViewModel
import com.dirror.music.util.*
import com.dirror.music.util.GlideUtil
import com.google.android.material.tabs.TabLayoutMediator
import eightbitlab.com.blurview.RenderScriptBlur

/**
 * MainActivity
 */
class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var musicBroadcastReceiver: MusicBroadcastReceiver // 音乐广播接收
    private lateinit var headSetChangeReceiver: HeadsetChangeReceiver // 耳机广播接收
    private lateinit var loginReceiver: LoginReceiver // 登录广播接收

    // 不要写成 mainViewModel = MainViewModel()
    private val mainViewModel: MainViewModel by viewModels()

    override fun initBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initData() {
        // Intent 过滤器
        var intentFilter = IntentFilter()
        intentFilter.addAction("com.dirror.music.MUSIC_BROADCAST") // 只接收 "com.dirror.foyou.MUSIC_BROADCAST" 标识广播
        musicBroadcastReceiver = MusicBroadcastReceiver() //
        registerReceiver(musicBroadcastReceiver, intentFilter) // 注册接收器

        intentFilter = IntentFilter()
        intentFilter.addAction("android.intent.action.HEADSET_PLUG")
        headSetChangeReceiver = HeadsetChangeReceiver()
        registerReceiver(headSetChangeReceiver, intentFilter) // 注册接收器
        intentFilter = IntentFilter()

        intentFilter.addAction("com.dirror.music.LOGIN")
        loginReceiver = LoginReceiver()
        registerReceiver(loginReceiver, intentFilter) // 注册接收器
        // 检查新版本
        UpdateUtil.checkNewVersion(this, false)
    }

    override fun initView() {

        window.allowEnterTransitionOverlap = true
        window.allowReturnTransitionOverlap = true


        val radius = 20f
        val decorView: View = window.decorView
        val windowBackground: Drawable = decorView.background
        binding.blurViewPlay.setupWith(decorView.findViewById(R.id.viewPager2))
            .setFrameClearDrawable(windowBackground)
            .setBlurAlgorithm(RenderScriptBlur(this))
            .setBlurRadius(radius)
            .setHasFixedTransformationMatrix(true)
        binding.blurViewMenu.setupWith(decorView.findViewById(R.id.navigationView))
            .setFrameClearDrawable(windowBackground)
            .setBlurAlgorithm(RenderScriptBlur(this))
            .setBlurRadius(radius)
            .setHasFixedTransformationMatrix(true)

        // 适配状态栏
        val statusBarHeight = getStatusBarHeight(window, this) // px
        (binding.titleBar.layoutParams as ConstraintLayout.LayoutParams).apply{
            topMargin = statusBarHeight
        }
        // 侧滑状态栏适配
        (binding.menuMain.cvUser.layoutParams as LinearLayout.LayoutParams).apply{
            topMargin = statusBarHeight + 8.dp()
        }

        // 适配导航栏
        val navigationBarHeight = if (MyApplication.mmkv.decodeBool(Config.PARSE_NAVIGATION, true)) {
            getNavigationBarHeight(this)
        } else {
            0
        }

        (binding.clPlay.layoutParams as ConstraintLayout.LayoutParams).apply{
            bottomMargin = navigationBarHeight
        }
        (binding.blurViewPlay.layoutParams as ConstraintLayout.LayoutParams).apply{
            height = 56.dp() + navigationBarHeight
        }
        (binding.blurViewPlayBottom.layoutParams as ConstraintLayout.LayoutParams).apply {
            height = 56.dp() + navigationBarHeight
        }

        binding.viewPager2.offscreenPageLimit = 2
        binding.viewPager2.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int {
                return 2 // 2 个页面
            }

            override fun createFragment(position: Int): Fragment {
                return when (position) {
                    0 -> mainViewModel.myFragment
                    else -> mainViewModel.homeFragment
                }
            }
        }

        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.my)
                else -> getString(R.string.home)
            }
        }.attach()

        ViewPager2Util.changeToNeverMode(binding.viewPager2)

//        val url = MyApplication.mmkv.decodeString(Config.THEME_BACKGROUND, "") ?: ""
//        GlideUtil.load(url) {
//            binding.ivTheme.setImageBitmap(it)
//        }
    }

    override fun initListener() {
        //
        binding.apply {
            // 搜索按钮
            ivSearch.setOnClickListener {
                startActivity(Intent(this@MainActivity, SearchActivity::class.java))
            }
            // 设置按钮
            ivSettings.setOnClickListener {
                binding.drawerLayout.openDrawer(GravityCompat.START)
            }
        }


        // 侧滑
        binding.menuMain.apply {
            clUser.setOnClickListener {
                if (MyApplication.userManager.getCurrentUid() == 0L) {
                    MyApplication.activityManager.startLoginActivity(this@MainActivity)
                } else {
                    MyApplication.activityManager.startUserActivity(this@MainActivity, MyApplication.userManager.getCurrentUid())
                }
            }

            itemSwitchAccount.setOnClickListener {
                MyApplication.activityManager.startLoginActivity(this@MainActivity)
            }

            itemSettings.setOnClickListener {
                MyApplication.activityManager.startSettingsActivity(this@MainActivity)
            }

            itemPrivateLetter.setOnClickListener {
                val cookie = MyApplication.userManager.getCloudMusicCookie()
                if (cookie != "") {
                    MyApplication.activityManager.startPrivateLetterActivity(this@MainActivity)
                } else {
                    toast("当前为离线模式，请登录")
                }
            }
            // 反馈
            itemFeedback.setOnClickListener {
                startActivity(Intent(this@MainActivity, FeedbackActivity::class.java))
            }
        }

        binding.menuMain.itemAbout.setOnClickListener {
            startActivity(Intent(this, AboutActivity2::class.java))
        }

        // Mini Player
        binding.blurViewPlay.apply {
            setOnClickListener {
                MyApplication.activityManager.startPlayerActivity(this@MainActivity)
            }
            binding.includePlayer.ivPlay.setOnClickListener {
                MyApplication.musicController.value?.changePlayState()
                refreshPlayState()
            }
            binding.includePlayer.ivPlaylist.setOnClickListener { PlaylistDialog(this@MainActivity).show() }
        }


    }

    override fun initObserver() {
        mainViewModel.userId.observe(this, { userId ->
            // toast("侧栏收到变化：${userId}")
            if (userId == 0L) {
                binding.menuMain.tvUserName.text = "立即登录"
            } else {
                MyApplication.cloudMusicManager.getUserDetail(userId, {
                    runOnUiThread {
                        // toast("通知头像更新")
                        GlideUtil.loadCircle(it.profile.avatarUrl, binding.menuMain.ivCover)
                        binding.menuMain.tvUserName.text = it.profile.nickname
                    }
                }, {

                })
            }
        })
    }

    override fun onStart() {
        super.onStart()
        // 请求广播
        MyApplication.musicController.value?.sendBroadcast()
    }

    override fun onDestroy() {
        super.onDestroy()
        // 解绑广播接收
        unregisterReceiver(musicBroadcastReceiver)
        unregisterReceiver(headSetChangeReceiver)
        unregisterReceiver(loginReceiver)
    }

    inner class MusicBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val song = MyApplication.musicController.value?.getNowSongData()
            if (song != null) {
                binding.includePlayer.tvName.text = song.name
                binding.includePlayer.tvArtist.text = song.artists?.let { parseArtist(it) }
                // 这里应该用小的，等待修改
                SongPicture.getSongPicture(this@MainActivity, song, SongPicture.TYPE_LARGE) {
                    binding.includePlayer.ivCover.setImageBitmap(it)
                }
            }
            refreshPlayState()
        }
    }

    inner class LoginReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // 通知 viewModel
            mainViewModel.setUserId()
        }
    }

    /**
     * 刷新播放状态
     */
    private fun refreshPlayState() {
        if (MyApplication.musicController.value?.getPlayState()!!) {
            binding.includePlayer.ivPlay.setImageResource(R.drawable.ic_mini_player_pause)
        } else {
            binding.includePlayer.ivPlay.setImageResource(R.drawable.ic_mini_player_play)
        }
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerVisible(GravityCompat.START)) {
            binding.drawerLayout.close()
        } else {
            super.onBackPressed()
        }
    }

}