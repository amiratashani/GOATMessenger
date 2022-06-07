package com.example.goatmessenger.ui.chat

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.*
import com.example.goatmessenger.data.ChatRepository
import com.example.goatmessenger.data.DefaultChatRepository
import com.example.goatmessenger.data.Message

class ChatViewModel @JvmOverloads constructor(
    application: Application,
    private val repository: ChatRepository = DefaultChatRepository.getInstance(application)
) : AndroidViewModel(application) {

    private val chatId = MutableLiveData<Long>()

    var messages :MutableLiveData<List<Message>> = repository.findMessages(0) as MutableLiveData<List<Message>>


    /**
     * We want to update the notification when the corresponding chat screen is open. Setting this
     * to `true` updates the current notification, removing the unread message(s) badge icon and
     * suppressing further notifications.
     *
     * We do want to keep on showing and updating the notification when the chat screen is opened
     * as an expanded bubble. [ChatFragment] should set this to false if it is launched in
     * BubbleActivity. Otherwise, the expanding a bubble would remove the notification and the
     * bubble.
     */


    /**
     * The contact of this chat.
     */
    val contact = chatId.switchMap { id -> repository.findContact(id) }

    /**
     * The list of all the messages in this chat.
     */

    fun setChatId(id: Long) {
        chatId.value = id
    }

    fun remove(id: Long) {
        repository.removeMessage(id)
        messages.postValue(repository.findMessages(0).value)
    }

    fun update(id: Long,text: String) {
        repository.updateMessage(id,text)
        messages.postValue(repository.findMessages(0).value)
    }

    fun send(text: String) {
        val id = chatId.value
        if (id != null && id != 0L) {
            repository.sendMessage(id, text)
        }
        messages.postValue(repository.findMessages(0).value)
    }
}
