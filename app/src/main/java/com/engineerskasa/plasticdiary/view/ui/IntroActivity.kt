package com.engineerskasa.plasticdiary.view.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.engineerskasa.plasticdiary.R
import com.engineerskasa.plasticdiary.databinding.ActivityIntroBinding
import com.engineerskasa.plasticdiary.util.setSystemBarColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IntroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIntroBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSystemBarColor(this, R.color.bg_white)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_intro)
        binding.lifecycleOwner = this
        initViews()
    }

    private fun initViews() {
        binding.getStartedBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}