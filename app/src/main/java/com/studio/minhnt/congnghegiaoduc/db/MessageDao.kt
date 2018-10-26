package com.studio.minhnt.congnghegiaoduc.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.studio.minhnt.congnghegiaoduc.model.Message

@Dao
interface MessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMessage(notify: Message)

    @Query("select * from message order by message_id desc")
    fun loadMessages(): LiveData<List<Message>>

    @Query("select * from message where message_id =:id")
    fun getMessageById(id: Long): LiveData<Message>

    @Delete
    fun deleteMessage(message: Message)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateMessage(message: Message)
}