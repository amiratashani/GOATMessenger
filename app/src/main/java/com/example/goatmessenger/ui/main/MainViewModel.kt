package com.example.goatmessenger.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.goatmessenger.data.ChatRepository
import com.example.goatmessenger.data.DefaultChatRepository

class MainViewModel @JvmOverloads constructor(
    application: Application,
    repository: ChatRepository = DefaultChatRepository.getInstance(application)
) : AndroidViewModel(application) {

    /**
     * All the contacts.
     */
    val contacts = repository.getContacts()
}
