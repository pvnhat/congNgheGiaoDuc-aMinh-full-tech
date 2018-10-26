package com.studio.minhnt.congnghegiaoduc.repo

import android.arch.lifecycle.LiveData
import com.studio.minhnt.congnghegiaoduc.api.*
import com.studio.minhnt.congnghegiaoduc.db.MessageDao
import com.studio.minhnt.congnghegiaoduc.model.Message
import com.studio.minhnt.congnghegiaoduc.model.Voice
import com.studio.minhnt.congnghegiaoduc.util.AppExecutors
import com.studio.minhnt.congnghegiaoduc.util.AppUtil
import javax.inject.Inject

class MessageRepository @Inject constructor(private val appExecutors: AppExecutors,
    private val messageDao: MessageDao, private val appService: AppService) {
  fun getMessages(): LiveData<List<Message>> {
    return messageDao.loadMessages()
  }

  fun insertMessage(message: Message) {
    appExecutors.diskIO().execute {
      message.shapes = AppUtil.convertTextToIcon(message.content)
      messageDao.insertMessage(message)
    }
    appExecutors.diskIO().execute {
      appService.getVoice("male", message.content.replace("\n", ". ")).observeForever {
        if (it is ApiSuccessResponse<Voice>) {
          message.maleVoice = it.body.async
          appExecutors.diskIO().execute { messageDao.insertMessage(message) }
        }
      }
    }

    appExecutors.diskIO().execute {
      appService.getVoice("female", message.content.replace("\n", ". ")).observeForever {
        if (it is ApiSuccessResponse<Voice>) {
          message.femaleVoice = it.body.async
          appExecutors.diskIO().execute { messageDao.insertMessage(message) }
        }
      }
    }
  }

  fun deleteMessage(message: Message) {
    appExecutors.diskIO().execute {
      messageDao.deleteMessage(message)
    }
  }

  fun getMaleVoice(message: Message): LiveData<Resource<Message>> {
    return object : NetworkBoundResource<Message, Voice>(appExecutors) {
      override fun loadFromDb(): LiveData<Message> {
        return messageDao.getMessageById(message.id)
      }

      override fun shouldFetch(data: Message?): Boolean {
        return true
      }

      override fun saveCallResult(item: Voice) {
        message.maleVoice = item.async
        messageDao.updateMessage(message)
      }

      override fun createCall(): LiveData<ApiResponse<Voice>> {
        return appService.getVoice("male", message.content)
      }

    }.asLiveData()
  }

  fun getFemaleVoice(message: Message): LiveData<Resource<Message>> {
    return object : NetworkBoundResource<Message, Voice>(appExecutors) {
      override fun loadFromDb(): LiveData<Message> {
        return messageDao.getMessageById(message.id)
      }

      override fun shouldFetch(data: Message?): Boolean {
        return true
      }

      override fun saveCallResult(item: Voice) {
        message.femaleVoice = item.async
        messageDao.updateMessage(message)
      }

      override fun createCall(): LiveData<ApiResponse<Voice>> {
        return appService.getVoice("female", message.content)
      }

    }.asLiveData()
  }
}