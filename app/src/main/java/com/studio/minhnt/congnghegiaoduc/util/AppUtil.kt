package com.studio.minhnt.congnghegiaoduc.util

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import cn.pedant.SweetAlert.SweetAlertDialog
import java.util.*
import kotlin.collections.ArrayList

object AppUtil {
    fun showMessage(context: Context, type: Int, title: String, message: String) {
        val dialog = SweetAlertDialog(context, type)
                .setTitleText(title)
                .setContentText(message)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }

    fun showError(context: Context, title: String, message: String) {
        showMessage(context, SweetAlertDialog.ERROR_TYPE, title, message)
    }

    fun showWarning(context: Context, title: String, message: String) {
        showMessage(context, SweetAlertDialog.WARNING_TYPE, title, message)
    }

    fun showSuccess(context: Context, title: String, message: String) {
        showMessage(context, SweetAlertDialog.SUCCESS_TYPE, title, message)
    }

    fun showMessageWithCallback(context: Context, type: Int, title: String, message: String, buttonMessage: String, listener: SweetAlertDialog.OnSweetClickListener) {
        val dialog = SweetAlertDialog(context, type)
                .setTitleText(title)
                .setContentText(message)
                .setConfirmText(buttonMessage)
                .setConfirmClickListener(listener)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }

    fun showErrorWithCallBack(context: Context, title: String, message: String, buttonMessage: String, listener: SweetAlertDialog.OnSweetClickListener) {
        showMessageWithCallback(context, SweetAlertDialog.ERROR_TYPE, title, message, buttonMessage, listener)
    }

    fun showSuccessWithCallBack(context: Context, title: String, message: String, buttonMessage: String, listener: SweetAlertDialog.OnSweetClickListener) {
        showMessageWithCallback(context, SweetAlertDialog.SUCCESS_TYPE, title, message, buttonMessage, listener)
    }

    fun showMessageWithTwoOptions(context: Context, message: String, buttonMessage: String, listener: SweetAlertDialog.OnSweetClickListener) {
        val dialog = SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Cảnh báo")
                .setContentText(message)
                .setConfirmText(buttonMessage)
                .setConfirmClickListener(listener)
                .setCancelText("Lỡ tay thôi")
                .setCancelClickListener { sweetAlertDialog -> sweetAlertDialog.dismissWithAnimation() }
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val connManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        if (connManager != null) {
            val networkInfo = connManager.activeNetworkInfo
            if (networkInfo != null) {
                return networkInfo.isConnected
            }
        }

        return false
    }

    fun showErrorInternet(context: Context) {
        showError(context, "Lỗi kết nối Internet", "Bạn cần kết nối internet để thực hiện chức năng này.")
    }

    fun convertTextToIcon(text: String): ArrayList<String> {
        val st = StringTokenizer("$text\n".replace("\n", " \n "), "[`~!@#$%^&*()-_+=|{}:;'\"<>,./? ]+")
        val list = ArrayList<String>()
        var sb = StringBuilder("")
        while (st.hasMoreTokens()) {
            val s = st.nextToken()
            if (s.equals("\n")) {
                list.add(sb.toString().trim())
                sb = StringBuilder("")
                list.add("\n")
            } else {
                sb.append(Random().nextInt(30).toString()).append(" ")
            }
        }

        for (i in list) {
            if (i == "\n")
                Log.d("hehe", "xd")
            else
                Log.d("hehe", i)
        }
        list.removeAt(list.size - 1)
        Log.d("hehe", "size = ${list.size}")
        return list
    }
}