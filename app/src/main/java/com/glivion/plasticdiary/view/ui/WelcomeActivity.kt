package com.glivion.plasticdiary.view.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.glivion.plasticdiary.R
import com.glivion.plasticdiary.databinding.ActivityWelcomeBinding
import com.glivion.plasticdiary.services.TerminateQuizService
import com.glivion.plasticdiary.util.setSystemBarColor
import com.glivion.plasticdiary.util.showSnackBarMessage
import com.glivion.plasticdiary.view.dialog.LoadingDialog
import com.glivion.plasticdiary.viewmodel.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber




@AndroidEntryPoint
class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding
    private val viewModel: AuthViewModel by viewModels()
    private var googleSignInClient: GoogleSignInClient? = null
    private val loadingDialog = LoadingDialog(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSystemBarColor(this, R.color.bg_white)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_welcome)
        binding.lifecycleOwner = this
        Timber.e("token: ${viewModel.getCurrentUser()?.token}")
        Timber.e("uid: ${viewModel.getCurrentUser()?.uid}")

        initViews()
        initViewModel()
    }

    private fun initViewModel() {
        if (viewModel.getCurrentUser()?.token != null) {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        viewModel.userLoader.observe(this, { isLoading ->
            isLoading.let {
                if (it) {
                    loadingDialog.startProgressDialog()
                } else {
                    loadingDialog.dismissDialog()
                }
            }
        })

        viewModel.userErrors.observe(this, { isError ->
            isError.let { message ->
                if (message != null) {
                    Timber.e("initViewModel: $message")
                    showSnackBarMessage(binding.parentLayout, message)
                }
            }
        })
        viewModel.responses.observe(this, { isError ->
            isError.let { message ->
                if (message != null) {
                    Timber.e("initViewModel responses: $message")
                    showSnackBarMessage(binding.parentLayout, message)
                }
            }
        })
    }

    private fun initViews() {
        binding.apply {
            googleSignInBtn.setOnClickListener {
                val googleIntent = googleSignInClient?.signInIntent
                activityLauncher.launch(googleIntent)
            }
        }
    }

    private val activityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        Timber.e("result: ${it.resultCode}")
        if (it.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val account = task.result
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                viewModel.signInWithSocialAuthCredentials(credential, account)
            } catch (e: ApiException) {
                Timber.e("exception: status: ${e.status} code: ${e.statusCode} message: ${e.message}")
            }
        }
    }

    override fun onStart() {
        super.onStart()
        initGoogleSignIn()
    }

    private fun initGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .requestProfile()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }


}