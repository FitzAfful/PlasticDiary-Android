package com.glivion.plasticdiary.view.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.glivion.plasticdiary.R
import com.glivion.plasticdiary.databinding.ActivityMainBinding
import com.glivion.plasticdiary.model.explore.Resource
import com.glivion.plasticdiary.model.explore.Tip
import com.glivion.plasticdiary.util.FCM_DATA
import com.glivion.plasticdiary.util.REMOTE_MSG
import com.glivion.plasticdiary.util.openViewMoreInfoDialog
import com.glivion.plasticdiary.util.showInWebView
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    var remoteMessage: RemoteMessage? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        initViews()
    }

    private fun initViews() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNav.setupWithNavController(navController)
    }

    private val messageReceiver = object: BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.extras?.let {
                remoteMessage = it.getParcelable(REMOTE_MSG)
            }
            val animSlideDown = AnimationUtils.loadAnimation(this@MainActivity, R.anim.slide_down)
            binding.notification.apply {
                parentLayout.visibility = View.VISIBLE
                title.text = remoteMessage?.notification?.title
                description.text = remoteMessage?.notification?.body
                close.setOnClickListener {
                    parentLayout.visibility = View.GONE
                }
                parentLayout.setOnClickListener {
                    parentLayout.visibility = View.GONE
                    navigateUser(remoteMessage, it)
                }
                parentLayout.startAnimation(animSlideDown)
            }
            Timber.e("message: ${remoteMessage?.notification?.title} body: ${remoteMessage?.notification?.body}")
        }

    }

    private fun navigateUser(remoteMessage: RemoteMessage?, view: View) {
        val type = remoteMessage?.data?.get("type")

        when (type) {
            "news", "video", "article", "research" -> {
                val data = Gson().fromJson(remoteMessage.data["data"], Resource::class.java)
                showInWebView(this, data.link, data.id, type)
            }
            "tips" -> {
                val tip = Gson().fromJson(remoteMessage.data["data"], Tip::class.java)
                openViewMoreInfoDialog(this, "Tip", tip.content!!)
            }
            "quiz" -> {
               navController.navigate(R.id.quiz_nav)
            }
            "leaderboard" -> {
                navController.navigate(R.id.homeLeaderboardFragment)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, IntentFilter(
            FCM_DATA))
    }

    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver)
    }
}