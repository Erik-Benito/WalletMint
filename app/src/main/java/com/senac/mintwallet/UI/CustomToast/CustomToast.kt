package com.senac.mintwallet.UI.CustomToast

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.senac.mintwallet.R

object CustomToast {

    fun showToast(context: Context, message: String, success: Boolean) {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val layout: View = inflater.inflate(R.layout.custom_toast, null)

        val toastIcon: ImageView = layout.findViewById(R.id.toast_icon)
        val toastMessage: TextView = layout.findViewById(R.id.toast_message)

        toastMessage.text = message

        if (success) {
            toastIcon.setImageResource(R.drawable.ic_success) // Icon for success
        } else {
            toastIcon.setImageResource(R.drawable.ic_error) // Icon for failure
        }

        val toast = Toast(context)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = layout
        toast.show()
    }
}
