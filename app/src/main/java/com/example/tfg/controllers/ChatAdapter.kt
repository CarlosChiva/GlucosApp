package com.example.tfg.controllers

import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg.R
import com.example.tfg.models.Mensage

class ChatAdapter(private val messageList: List<Mensage>, private val currentUser: String) :
    RecyclerView.Adapter<ChatAdapter.MessageViewHolder>() {
    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
         val messageTextView: TextView = itemView.findViewById(R.id.messageTextView)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view: View = if (viewType == 0) {
            // Mensaje del usuario actual, alineado a la derecha
            LayoutInflater.from(parent.context).inflate(R.layout.own_message, parent, false)
        } else {
            // Mensaje de otro usuario, alineado a la izquierda
            LayoutInflater.from(parent.context).inflate(R.layout.item_message_left, parent, false)
        }
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message: Mensage = messageList[position]
        holder.messageTextView.text = message.message
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun getItemViewType(position: Int): Int {
        // Comprueba si el mensaje es del usuario actual o de otro usuario
        val message: Mensage = messageList[position]
        return if (message.sender == currentUser) {
            0 // Mensaje del usuario actual
        } else {
            1 // Mensaje de otro usuario
        }
    }


}