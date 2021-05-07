package com.justdance.passwordwaller.ui.passwordsRecycler

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import com.justdance.passwordwaller.R
import com.justdance.passwordwaller.redux.passwords.DeletePassword
import com.justdance.passwordwaller.redux.store

class Adapter(
    private val context: Context,
    private val layoutInflater: LayoutInflater
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
            val view = layoutInflater.inflate(R.layout.bottom_sheet, null)
            val editOption = view.findViewById<LinearLayout>(R.id.editOption)
            val deleteOption = view.findViewById<LinearLayout>(R.id.deleteOption)
            editOption.setOnClickListener {
                dialog.dismiss()
            }
            deleteOption.setOnClickListener {
                store.dispatch(DeletePassword(item.passwordId))
                dialog.dismiss()
            }
            dialog.setContentView(view)
            dialog.show()
        }
    }

    override fun getItemCount(): Int {
        return store.state.passwords.size
    }
}