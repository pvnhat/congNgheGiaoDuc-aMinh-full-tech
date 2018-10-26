package com.studio.minhnt.congnghegiaoduc.ui

import android.support.v7.widget.RecyclerView
import android.text.Layout
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.nex3z.flowlayout.FlowLayout
import com.studio.minhnt.congnghegiaoduc.R
import com.studio.minhnt.congnghegiaoduc.base.DataBindingViewHolder
import com.studio.minhnt.congnghegiaoduc.databinding.ItemMainBinding
import com.studio.minhnt.congnghegiaoduc.model.Message

class MainAdapter(var list: MutableList<Message>) : RecyclerView.Adapter<MainAdapter.MainVH>() {
    lateinit var callBack: CallBack

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainVH {
        return MainVH(parent)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MainVH, position: Int) {
        holder.binding.tvMessage.text = list[position].content
        holder.binding.llShape.removeAllViews()
        for (icon in list[position].shapes) {
            Log.d("hehe", icon)
            if (icon == "\n") {
                val flow = FlowLayout(holder.itemView.context)
                flow.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                holder.binding.llShape.addView(flow)
            } else {
                val ic = icon.trim().split(" ")
                val flow = FlowLayout(holder.itemView.context)
                flow.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                for (i in ic) {
                    if (i.trim().isNotEmpty()) {
                        try {
                            val iv = ImageView(holder.itemView.context)
                            iv.setImageResource(when (i.toInt()) {
                                in 0..9 -> R.drawable.ic_signal_cellular_4_bar_black_24dp
                                in 10..19 -> R.drawable.ic_lens_black_24dp
                                else -> R.drawable.square
                            })
                            flow.addView(iv)
                        } catch (e: Exception) {
                        }
                    }
                }

                holder.binding.llShape.addView(flow)
            }
        }
    }

    inner class MainVH(viewGroup: ViewGroup) : DataBindingViewHolder<ItemMainBinding>(viewGroup, R.layout.item_main) {
        init {
            binding.tvMessage.setOnLongClickListener {
                callBack.onDelete(list[adapterPosition])
                return@setOnLongClickListener true
            }
            binding.ivSoundMale.setOnClickListener {
                callBack.onSoundMale(list[adapterPosition])
            }
            binding.ivSoundFemale.setOnClickListener {
                callBack.onSoundFemale(list[adapterPosition])
            }
            binding.tvMessage.setOnClickListener {
                if (binding.llShape.visibility == View.GONE)
                    binding.llShape.visibility = View.VISIBLE
                else
                    binding.llShape.visibility = View.GONE
            }
        }
    }

    interface CallBack {
        fun onSoundMale(message: Message)
        fun onSoundFemale(message: Message)
        fun onDelete(message: Message)
    }
}