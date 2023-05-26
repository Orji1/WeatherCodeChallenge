package com.chimezie_interview.weather.ui.dashboard

import android.content.Context
import androidx.appcompat.app.AlertDialog


fun displayError(reportError : String,  context: Context, onOK: () -> Unit) {
    if (reportError.isNotEmpty()) {
        val alertDialog = AlertDialog.Builder(context)
            .setTitle("Alert")
            .setMessage(reportError)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        alertDialog.show()
        onOK()
    }
}