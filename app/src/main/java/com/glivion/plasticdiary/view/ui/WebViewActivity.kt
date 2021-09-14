package com.glivion.plasticdiary.view.ui

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import com.glivion.plasticdiary.R
import com.glivion.plasticdiary.databinding.ActivityWebViewBinding
import com.glivion.plasticdiary.util.*
import com.glivion.plasticdiary.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class WebViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWebViewBinding
    private var url: String? = null
    private var id: Int = 0
    private var contentType: String? = null
    private  val viewModel:HomeViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        setSystemBarColor(this, android.R.color.transparent)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_web_view)
        binding.lifecycleOwner = this

        getIntentParams()
    }

    private fun initWebView(url: String?) {
        binding.webView.apply {
            settings.apply {
                loadsImagesAutomatically = true
                javaScriptEnabled = true
                useWideViewPort = true
                loadWithOverviewMode = true
                setSupportZoom(true)
                builtInZoomControls = true
                displayZoomControls = false
            }
            scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY

            webViewClient = object : WebViewClient(){
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                }

                override fun onPageCommitVisible(view: WebView?, url: String?) {
                    super.onPageCommitVisible(view, url)
                    binding.progressBar.visibility = View.GONE
                }

                override fun onReceivedError(
                    view: WebView?,
                    errorCode: Int,
                    description: String?,
                    failingUrl: String?
                ) {
                    super.onReceivedError(view, errorCode, description, failingUrl)
                    showSnackBarMessage(binding.parentLayout, "Failed to load page: $errorCode")
                }
            }
            loadUrl(url!!)
        }
    }

    private fun getIntentParams() {
        intent?.extras?.let {
            url = it.getString(WEB_VIEW_URL)
            id = it.getInt(WEB_VIEW_ID)
            contentType = it.getString(WEB_VIEW_TYPE)
        }
        Timber.e("url: $url")
        initWebView(url)
        initViewModel()
    }

    private fun initViewModel() {
        viewModel.singleContentAPI(id, contentType!!)
        viewModel.userErrors.observe(this, { isError ->
            if (this.lifecycle.currentState == Lifecycle.State.RESUMED) {
                isError.let { e ->
                    Timber.e("initViewModel: ${e.localizedMessage}")
                    showErrorMessage(binding.parentLayout, e)
                }
            }
        })


        viewModel.responses.observe(this, { response ->
            if (this.lifecycle.currentState == Lifecycle.State.RESUMED) {
                Timber.e("response: $response")
                showSnackBarMessage(binding.parentLayout, response)
            }
        })
    }

    override fun onBackPressed() {
        if (binding.webView.canGoBack()) {
            binding.webView.goBack()
        }else{
            super.onBackPressed()
        }
    }
}