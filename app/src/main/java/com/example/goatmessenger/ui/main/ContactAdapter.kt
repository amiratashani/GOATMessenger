package com.example.goatmessenger.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.goatmessenger.R
import com.example.goatmessenger.data.Contact
import com.example.goatmessenger.databinding.ChatItemBinding

class ContactAdapter(
    private val context: Context,
    private val onChatClicked: (id: Long) -> Unit
) : ListAdapter<Contact, ContactViewHolder>(DIFF_CALLBACK) {

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).id
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val holder = ContactViewHolder(parent)
        holder.itemView.setOnClickListener {
            onChatClicked(holder.itemId)
        }
        return holder
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact: Contact = getItem(position)
        holder.binding.icon.setImageDrawable( ContextCompat.getDrawable(context, contact.icon,))
        holder.binding.name.text = contact.name
    }
}

private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Contact>() {
    override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return oldItem == newItem
    }
}

class ContactViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.chat_item, parent, false)
) {
    val binding: ChatItemBinding = ChatItemBinding.bind(itemView)
}
