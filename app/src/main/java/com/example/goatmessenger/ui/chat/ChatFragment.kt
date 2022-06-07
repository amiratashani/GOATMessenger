package com.example.goatmessenger.ui.chat

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.transition.TransitionInflater
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.goatmessenger.R
import com.example.goatmessenger.data.Message
import com.example.goatmessenger.databinding.ChatFragmentBinding
import com.example.goatmessenger.databinding.PfmMoreOptionDialogBinding
import com.example.goatmessenger.getNavigationController
import com.example.goatmessenger.ui.viewBindings

/**
 * The chat screen.
 */
class ChatFragment : Fragment(R.layout.chat_fragment) {
    val handler = Handler()

    companion object {
        private const val ARG_ID = "id"

        fun newInstance(id: Long) =
            ChatFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_ID, id)
                }
            }
    }

    private val viewModel: ChatViewModel by viewModels()
    private val binding by viewBindings(ChatFragmentBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        enterTransition =
            TransitionInflater.from(context).inflateTransition(R.transition.slide_bottom)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val id = arguments?.getLong(ARG_ID)
        if (id == null) {
            parentFragmentManager.popBackStack()
            return
        }

        val navigationController = getNavigationController()

        viewModel.setChatId(id)

        val messageAdapter = MessageAdapter(view.context){
            showOptionDialog(it)
        }

        val linearLayoutManager = LinearLayoutManager(view.context).apply {
            stackFromEnd = true
        }
        binding.messages.run {
            layoutManager = linearLayoutManager
            adapter = messageAdapter
        }

        viewModel.contact.observe(viewLifecycleOwner) { contact ->
            if (contact == null) {
                Toast.makeText(view.context, "Contact not found", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack()
            } else {
                navigationController.updateAppBar { name, icon ->
                    name.text = contact.name
                    icon.setImageDrawable(ContextCompat.getDrawable(requireContext(), contact.icon))
                    startPostponedEnterTransition()
                }
            }
        }

        viewModel.messages.observe(viewLifecycleOwner) { messages ->
            Log.e("Amir","observe messages")
            val tmpMessages =ArrayList<Message>()
            tmpMessages.addAll(messages)
            messageAdapter.submitList(tmpMessages)

            handler.postDelayed({  binding.messages.smoothScrollToPosition(messages.size - 1)}, 200)

        }

        binding.send.setOnClickListener {
            send()
        }
        binding.input.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                send()
                true
            } else {
                false
            }
        }
    }

    private fun showOptionDialog(messageId:Long) {

        val binding = PfmMoreOptionDialogBinding.inflate(layoutInflater)

        var alertDialog = AlertDialog.Builder(requireContext()).apply {
            binding.root.setBackgroundColor(ContextCompat.getColor(requireContext(),android.R.color.transparent))
            setView(binding.root)
        }.create()

        binding.alertView.setOnClickListener {
            alertDialog.dismiss()
            viewModel.update(messageId,"This Message Edited")
        }

        binding.analysisReportConstraint.setOnClickListener {
            alertDialog.dismiss()
            viewModel.remove(messageId)
        }

        alertDialog.show()
    }

    private fun send() {
        binding.input.text?.let { text ->
            if (text.isNotEmpty()) {
                viewModel.send(text.toString())
                text.clear()
            }
        }
    }
}
