package com.studio.minhnt.congnghegiaoduc.base

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

open class DataBindingViewHolder<Binding : ViewDataBinding>(var binding: Binding) : RecyclerView.ViewHolder(binding.root) {
    constructor(parent: ViewGroup, layout: Int) : this(LayoutInflater.from(parent.context), parent, layout)

    constructor(inflater: LayoutInflater, parent: ViewGroup, layout: Int) : this(DataBindingUtil.inflate<Binding>(inflater, layout, parent, false))
}