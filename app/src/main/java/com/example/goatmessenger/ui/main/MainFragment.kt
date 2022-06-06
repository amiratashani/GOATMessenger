package com.example.goatmessenger.ui.main

import android.content.LocusId
import android.os.Build
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.goatmessenger.ui.viewBindings
import com.example.goatmessenger.R
import com.example.goatmessenger.databinding.MainFragmentBinding
import com.example.goatmessenger.getNavigationController

/**
 * The main chat list screen.
 */
class MainFragment : Fragment(R.layout.main_fragment) {

    private val binding by viewBindings(MainFragmentBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = TransitionInflater.from(context).inflateTransition(R.transition.slide_top)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val navigationController = getNavigationController()
        navigationController.updateAppBar(false)
        val viewModel: MainViewModel by viewModels()

        val contactAdapter = ContactAdapter(requireContext()) { id ->
            navigationController.openChat(id)
        }
        viewModel.contacts.observe(viewLifecycleOwner, Observer { contacts ->
            contactAdapter.submitList(contacts)
        })
        binding.contacts.run {
            layoutManager = LinearLayoutManager(view.context)
            setHasFixedSize(true)
            adapter = contactAdapter
        }
        // Differentiate the main view from a chat view (ChatFragment) for  content capture.
        // See https://developer.android.com/reference/androidx/core/content/LocusIdCompat
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requireActivity().setLocusContext(LocusId("mainFragment"), null)
        }
    }
}
