package com.studio.minhnt.congnghegiaoduc.model

import com.google.gson.annotations.SerializedName

data class Voice(@SerializedName("async")
                 var async: String,
                 @SerializedName("error")
                 var error: Int)