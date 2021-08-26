package com.engineerskasa.plasticdiary.view.dialog

import android.app.AlertDialog
import android.content.Context
import dmax.dialog.SpotsDialog

class LoadingDialog(private val context: Context) {
    var progressDialog: AlertDialog? = null


    fun startProgressDialog() {
        progressDialog = SpotsDialog(context)
        progressDialog?.apply {
            setCancelable(false)
            show()
            setMessage("Please wait")
        }
    }


    fun dismissDialog() {
        progressDialog?.dismiss()
    }
}