package com.example.goatmessenger.ui.chat

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.goatmessenger.R
import com.example.goatmessenger.databinding.ChatFragmentBinding
import com.example.goatmessenger.getNavigationController
import com.example.goatmessenger.ui.viewBindings

/**
 * The chat screen.
 */
class ChatFragment : Fragment(R.layout.chat_fragment) {

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

        val messageAdapter = MessageAdapter(view.context)
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
            messageAdapter.submitList(messages)
            linearLayoutManager.scrollToPosition(messages.size - 1)
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


    private fun send() {
        binding.input.text?.let { text ->
            if (text.isNotEmpty()) {
                viewModel.send(text.toString())
                text.clear()
            }
        }
    }
}
