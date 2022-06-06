package com.example.goatmessenger

import android.os.Bundle
import android.transition.Transition
import android.transition.TransitionInflater
import android.transition.TransitionManager
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.fragment.app.commitNow
import com.example.goatmessenger.ui.main.MainFragment
import com.example.goatmessenger.ui.viewBindings
import com.example.goatmessenger.databinding.MainActivityBinding
import com.example.goatmessenger.ui.chat.ChatFragment

class MainActivity : AppCompatActivity(R.layout.main_activity), NavigationController {

    companion object {
        private const val FRAGMENT_CHAT = "chat"
    }

    private val binding by viewBindings(MainActivityBinding::bind)

    private lateinit var transition: Transition

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar)
        transition = TransitionInflater.from(this).inflateTransition(R.transition.app_bar)
        if (savedInstanceState == null) {
            supportFragmentManager.commitNow {
                replace(R.id.container, MainFragment())
            }
        }
    }

    override fun updateAppBar(
        showContact: Boolean,
        hidden: Boolean,
        body: (name: TextView, icon: ImageView) -> Unit
    ) {
        if (hidden) {
            binding.appBar.visibility = View.GONE
        } else {
            binding.appBar.visibility = View.VISIBLE
            TransitionManager.beginDelayedTransition(binding.appBar, transition)
            if (showContact) {
                supportActionBar?.setDisplayShowTitleEnabled(false)
                binding.name.visibility = View.VISIBLE
                binding.icon.visibility = View.VISIBLE
            } else {
                supportActionBar?.setDisplayShowTitleEnabled(true)
                binding.name.visibility = View.GONE
                binding.icon.visibility = View.GONE
            }
        }
        body(binding.name, binding.icon)
    }

    override fun openChat(id: Long) {
        supportFragmentManager.popBackStack(FRAGMENT_CHAT, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        supportFragmentManager.commit {
            addToBackStack(FRAGMENT_CHAT)
            replace(R.id.container, ChatFragment.newInstance(id))
        }
    }
}