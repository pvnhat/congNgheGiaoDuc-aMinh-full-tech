package com.studio.minhnt.congnghegiaoduc.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.studio.minhnt.congnghegiaoduc.api.Resource
import com.studio.minhnt.congnghegiaoduc.model.Message
import com.studio.minhnt.congnghegiaoduc.repo.MessageRepository
import javax.inject.Inject

class MainViewModel @Inject constructor(val messageRepository: MessageRepository) : ViewModel() {
    fun getMessages(): LiveData<List<Message>> {
        return messageRepository.getMessages()
    }

    fun insertMessage(message: Message) {
        messageRepository.insertMessage(message)
    }

    fun deleteMessage(message: Message) {
        messageRepository.deleteMessage(message)
    }

    fun getMaleVoice(message: Message): LiveData<Resource<Message>> {
        return messageRepository.getMaleVoice(message)
    }

    fun getFemaleVoice(message: Message): LiveData<Resource<Message>> {
        return messageRepository.getFemaleVoice(message)
    }
}