package com.example.goatmessenger.ui.chat

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.goatmessenger.ui.viewBindings
import com.example.goatmessenger.R
import com.example.goatmessenger.databinding.ChatFragmentBinding
import com.example.goatmessenger.getNavigationController

/**
 * The chat screen.
 */
class ChatFragment : Fragment() {

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

    //use this for setup view
    private var binding: ChatFragmentBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        enterTransition =
            TransitionInflater.from(context).inflateTransition(R.transition.slide_bottom)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ChatFragmentBinding.inflate(inflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        donNotChangeThisMethod()

        // write your code after this line here
    }

    override fun onDestroyView() {
        // Consider not storing the binding instance in a field, if not needed.
        binding = null
        super.onDestroyView()
    }

    private fun donNotChangeThisMethod() {
        val id = arguments?.getLong(ARG_ID)
        if (id == null) {
            parentFragmentManager.popBackStack()
            return
        }
        viewModel.setContactId(id)
        viewModel.contact.observe(viewLifecycleOwner) { contact ->
            if (contact == null) {
                Toast.makeText(context, "Contact not found", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack()
            } else {
                getNavigationController().updateAppBar { name, icon ->
                    name.text = contact.name
                    icon.setImageDrawable(ContextCompat.getDrawable(requireContext(), contact.icon))
                    startPostponedEnterTransition()
                }
            }
        }
    }




}
