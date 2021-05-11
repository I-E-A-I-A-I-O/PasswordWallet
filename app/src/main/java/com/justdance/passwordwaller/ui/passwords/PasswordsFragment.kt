package com.justdance.passwordwaller.ui.passwords

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
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
    private lateinit var clipboardManager: ClipboardManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPasswordsBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        clipboardManager = activity?.getSystemService(Activity.CLIPBOARD_SERVICE) as ClipboardManager
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.passwordsRefresh.setOnRefreshListener {
            onRefresh(view)
        }
        binding.passwordsRecycler.adapter = Adapter(requireContext(), layoutInflater, lifecycleScope, view, activity?.applicationContext) {
            label, text -> onCopy(label, text)
        }
        unsubscribe = store.subscribe {
            if (binding.passwordsRefresh.isRefreshing) {
                binding.passwordsRefresh.isRefreshing = false
            }
            binding.passwordsRecycler.adapter = Adapter(requireContext(), layoutInflater, lifecycleScope, view, activity?.applicationContext) {
                label, text -> onCopy(label, text)
            }
        }
    }

    private fun onRefresh(view: View) {
        activity?.applicationContext?.let {
            DataSource().loadPasswords(view, lifecycleScope, it)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Log.i("nav", "BACK PRESSED")
            }

        })
    }

    private fun onCopy(label: String, text: String) {
        val myClipData = ClipData.newPlainText(label, text)
        clipboardManager.setPrimaryClip(myClipData)
        Toast.makeText(context, "Password copied.", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unsubscribe()
    }
}