

package com.example.goatmessenger.data


import com.example.goatmessenger.R


class Contact(
    val id: Long,
    val name: String,
    val icon: Int
) {

    companion object {
        val CONTACTS = listOf(Contact(1L, "Amir", R.drawable.ic_avatar))
    }

    // you can create simple message from this contact with this method
    fun createSimpleMessage() = Message.Builder().apply {
        text = "Bale!"
        sender = this@Contact.id
        timestamp = System.currentTimeMillis()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Contact

        if (id != other.id) return false
        if (name != other.name) return false
        if (icon != other.icon) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + icon.hashCode()
        return result
    }
}
