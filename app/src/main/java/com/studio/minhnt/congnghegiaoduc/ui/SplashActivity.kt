package com.studio.minhnt.congnghegiaoduc.ui

import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.studio.minhnt.congnghegiaoduc.R
import com.studio.minhnt.congnghegiaoduc.base.BaseActivity
import com.studio.minhnt.congnghegiaoduc.databinding.ActivitySplashBinding

class SplashActivity : BaseActivity() {
    lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        Glide.with(this).asGif().load(R.drawable.tamgiactronvuong).into(binding.ivGif)
        changeStatusBarColor()
        FirebaseFirestore.getInstance().collection("version").document("version").get().addOnSuccessListener {
            if (it.getString("version") != packageManager.getPackageInfo(packageName, 0).versionName)
                showDialogUpdate()
            else
                goToMain()
        }.addOnFailureListener {
            goToMain()
        }
    }

    private fun goToMain() {
        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }

    private fun showDialogUpdate() {
        val dialog = SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Yêu cầu cập nhật")
                .setContentText("Đã có phiên bản mới.\nBạn vui lòng cập nhật")
                .setConfirmText("Cập nhật")
                .setConfirmClickListener { goToGooglePlay() }
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }

    private fun changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    private fun goToGooglePlay() {
        var intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName"))
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName"))
            startActivity(intent)
        }
        finish()
    }
}