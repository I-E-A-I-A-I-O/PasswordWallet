package com.justdance.passwordwaller.ui.passwords

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.justdance.passwordwaller.databinding.FragmentPasswordsBinding
import com.justdance.passwordwaller.redux.store
import org.reduxkotlin.StoreSubscriber

class PasswordsFragment : Fragment() {

    companion object {
        fun newInstance() = PasswordsFragment()
    }

    private val viewModel: PasswordsViewModel by viewModels()
    private lateinit var binding: FragmentPasswordsBinding
    private lateinit var unsubscribe: StoreSubscriber

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPasswordsBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        updateLayoutValues()
        unsubscribe = store.subscribe {
            updateLayoutValues()
        }
        return binding.root
    }

    private fun updateLayoutValues() {
        binding.emailDisplay.text = store.state.user?.email
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unsubscribe()
    }
}