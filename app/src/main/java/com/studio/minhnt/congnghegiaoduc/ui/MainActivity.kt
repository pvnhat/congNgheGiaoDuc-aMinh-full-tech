package com.studio.minhnt.congnghegiaoduc.ui

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.android.gms.ads.AdRequest
import com.studio.minhnt.congnghegiaoduc.R
import com.studio.minhnt.congnghegiaoduc.base.BaseActivity
import com.studio.minhnt.congnghegiaoduc.databinding.ActivityMainBinding
import com.studio.minhnt.congnghegiaoduc.di.ViewModelFactory
import com.studio.minhnt.congnghegiaoduc.model.Message
import com.studio.minhnt.congnghegiaoduc.util.AppUtil
import com.studio.minhnt.congnghegiaoduc.util.AudioUtils
import javax.inject.Inject

class MainActivity : BaseActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var viewModel: MainViewModel
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val audioUtils: AudioUtils  by lazy {
        AudioUtils(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val adRequest = AdRequest.Builder().build()
        val adRequest2 = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
        binding.adViewTop.loadAd(adRequest2)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
        binding.rvMain.layoutManager = LinearLayoutManager(this)
        binding.rvMain.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect!!.set(10, 10, 10, 10)
            }
        })
        val adapter = MainAdapter(mutableListOf())
        adapter.callBack = object : MainAdapter.CallBack {
            override fun onSoundFemale(message: Message) {
                playSound(message.femaleVoice)
            }

            override fun onSoundMale(message: Message) {
                playSound(message.maleVoice)
            }

            override fun onDelete(message: Message) {
                AppUtil.showMessageWithTwoOptions(this@MainActivity, "Bạn muốn xóa dòng này?", "Xóa ngay đi", SweetAlertDialog.OnSweetClickListener { sweetAlertDialog ->
                    sweetAlertDialog!!.dismissWithAnimation()
                    viewModel.deleteMessage(message)
                })
            }
        }
        binding.rvMain.adapter = adapter
        binding.ibSend.setOnClickListener {
            if (!AppUtil.isNetworkAvailable(this)) {
                AppUtil.showErrorInternet(this)
                return@setOnClickListener
            }
            if (binding.etInput.text.toString().trim().isNotEmpty()) {
                viewModel.insertMessage(Message(content = binding.etInput.text.toString().trim(), shapes = ArrayList()))
                binding.etInput.setText("")
            }
        }

        viewModel.getMessages().observe(this, android.arch.lifecycle.Observer {
            adapter.list = it as MutableList<Message>
            adapter.notifyDataSetChanged()
        })
    }

    override fun onDestroy() {
        audioUtils.clear()
        super.onDestroy()
    }

    private fun playSound(url: String) {
        audioUtils.playSoundUri(url)
        audioUtils.setOnPlayCompleteListener(object : AudioUtils.OnPlayCompleteListener {
            override fun onPlayComplete() {
                audioUtils.clear()
            }
        })
        audioUtils.setOnPlayErrorListener(object : AudioUtils.OnPlayErrorListener {
            override fun onError() {
                Toast.makeText(this@MainActivity, "Vui lòng không bấm liên tục vào loa", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
