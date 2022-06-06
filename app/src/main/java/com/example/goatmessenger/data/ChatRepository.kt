package com.example.goatmessenger.data

import android.content.Context
import android.net.Uri
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.concurrent.Executor
import java.util.concurrent.Executors

interface ChatRepository {
    fun getContacts(): LiveData<List<Contact>>
    fun findContact(id: Long): LiveData<Contact?>
    fun findMessages(id: Long): LiveData<List<Message>>
    fun sendMessage(id: Long, text: String)

    // Add your methods definition here
}

class DefaultChatRepository internal constructor() : ChatRepository {

    companion object {
        private var instance: DefaultChatRepository? = null

        fun getInstance(context: Context): DefaultChatRepository {
            return instance ?: synchronized(this) {
                instance ?: DefaultChatRepository().also {
                    instance = it
                }
            }
        }
    }

    private var currentContact: Contact = Contact.CONTACTS[0]

    private val messages = mutableListOf(
        Message(1L, currentContact.id, "Hey...!", System.currentTimeMillis()),
        Message(2L, currentContact.id, "What's Up...", System.currentTimeMillis()),
        Message(3L, currentContact.id, "Send me a message", System.currentTimeMillis()),
    )

    @MainThread
    override fun getContacts(): LiveData<List<Contact>> {
        return MutableLiveData<List<Contact>>().apply {
            postValue(Contact.CONTACTS)
        }
    }

    @MainThread
    override fun findContact(id: Long): LiveData<Contact?> {
        return MutableLiveData<Contact>().apply {
            postValue(Contact.CONTACTS.find { it.id == id })
        }
    }

    @MainThread
    override fun findMessages(id: Long): LiveData<List<Message>> {
        return MutableLiveData(messages)
    }

    @MainThread
    override fun sendMessage(id: Long, text: String) {
        addMessage(Message.Builder().apply {
            sender = 0L // User
            this.text = text
            timestamp = System.currentTimeMillis()
        })
        addMessage(currentContact.createSimpleMessage())
    }

    private fun addMessage(builder: Message.Builder) {
        builder.id = messages.last().id + 1
        messages.add(builder.build())
    }

}
