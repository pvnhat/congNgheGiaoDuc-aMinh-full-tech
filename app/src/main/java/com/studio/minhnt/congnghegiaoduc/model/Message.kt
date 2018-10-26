package com.studio.minhnt.congnghegiaoduc.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull

@Entity(tableName = "message")
class Message(@PrimaryKey
              @ColumnInfo(name = "message_id")
              @NonNull
              var id: Long = System.currentTimeMillis(),
              @ColumnInfo(name = "message_content")
              var content: String,
              @ColumnInfo(name = "message_male")
              var maleVoice: String = "",
              @ColumnInfo(name = "message_female")
              var femaleVoice: String = "",
              @ColumnInfo(name = "message_shape")
              var shapes: ArrayList<String>)