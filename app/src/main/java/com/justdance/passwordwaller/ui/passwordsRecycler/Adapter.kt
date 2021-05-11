package com.justdance.passwordwaller.ui.passwordsRecycler

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.Snackbar
import com.justdance.passwordwaller.R
import com.justdance.passwordwaller.daoDb.AppDatabase
import com.justdance.passwordwaller.network.ApiManager
import com.justdance.passwordwaller.redux.passwords.AddPassword
import com.justdance.passwordwaller.redux.passwords.DeletePassword
import com.justdance.passwordwaller.redux.store
import kotlinx.coroutines.launch

class Adapter(
    private val context: Context,
    private val layoutInflater: LayoutInflater,
    private val scope: LifecycleCoroutineScope,
    private val view: View,
    private val appContext: Context?,
    private val onCopy: (label: String, text: String) -> Unit
    ) : RecyclerView.Adapter<Adapter.ItemViewHolder>() {
    class ItemViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val descriptionView: TextView = view.findViewById(R.id.passwordDescription)
        val passwordView: TextView = view.findViewById(R.id.passwordContent)
        val cardView: MaterialCardView = view.findViewById(R.id.passwordCardView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context).
            inflate(R.layout.password_layout, parent, false)
        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = store.state.passwords[position]
        holder.descriptionView.text = item.description
        holder.passwordView.text = item.password
        holder.cardView.setOnClickListener {
            val dialog = BottomSheetDialog(context)
            val dialogView = layoutInflater.inflate(R.layout.bottom_sheet, null)
            val editOption = dialogView.findViewById<LinearLayout>(R.id.editOption)
            val deleteOption = dialogView.findViewById<LinearLayout>(R.id.deleteOption)
            val copyOption = dialogView.findViewById<LinearLayout>(R.id.CopyOption)
            copyOption.setOnClickListener {
                dialog.dismiss()
                onCopy(item.description, item.password)
            }
            editOption.setOnClickListener {
                dialog.dismiss()
            }
            deleteOption.setOnClickListener {
                store.dispatch(DeletePassword(item.passwordId))
                scope.launch {
                    try {
                        ApiManager.retrofitService.deletePassword(
                            store.state.user?.token,
                            "/passwords/password/${item.passwordId}"
                        )
                        appContext?.let {
                            scope.launch {
                                val database = AppDatabase.getInstance(it)
                                database.passwordDao().deletePassword(item.passwordId)
                            }
                        }
                        Snackbar.make(view, "Password deleted.", Snackbar.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        e.printStackTrace()
                        store.dispatch(AddPassword(item))
                        Snackbar.make(view, "Error deleting the password.", Snackbar.LENGTH_SHORT).show()
                    }
                }
                dialog.dismiss()
            }
            dialog.setContentView(dialogView)
            dialog.show()
        }
    }

    override fun getItemCount(): Int {
        return store.state.passwords.size
    }
}