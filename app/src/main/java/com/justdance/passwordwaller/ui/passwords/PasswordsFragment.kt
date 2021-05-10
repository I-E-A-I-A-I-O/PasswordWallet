package com.justdance.passwordwaller.ui.passwords

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.justdance.passwordwaller.databinding.FragmentPasswordsBinding
import com.justdance.passwordwaller.redux.store
import com.justdance.passwordwaller.ui.passwordsRecycler.Adapter
import com.justdance.passwordwaller.ui.passwordsRecycler.DataSource
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.passwordsRefresh.setOnRefreshListener {
            onRefresh(view)
        }
        binding.passwordsRecycler.adapter = Adapter(requireContext(), layoutInflater, lifecycleScope, view, activity?.applicationContext)
        unsubscribe = store.subscribe {
            if (binding.passwordsRefresh.isRefreshing) {
                binding.passwordsRefresh.isRefreshing = false
            }
            binding.passwordsRecycler.adapter = Adapter(requireContext(), layoutInflater, lifecycleScope, view, activity?.applicationContext)
        }
    }

    private fun onRefresh(view: View) {
        activity?.applicationContext?.let {
            DataSource().loadPasswords(view, lifecycleScope, it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unsubscribe()
    }
}