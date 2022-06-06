

package com.example.goatmessenger.data

data class Message(
    val id: Long,
    val sender: Long,
    val text: String,
    val timestamp: Long
) {

    val isIncoming: Boolean
        get() = sender != 0L

    class Builder {
        var id: Long? = null
        var sender: Long? = null
        var text: String? = null
        var timestamp: Long? = null
        fun build() = Message(id!!, sender!!, text!!, timestamp!!)
    }
}
