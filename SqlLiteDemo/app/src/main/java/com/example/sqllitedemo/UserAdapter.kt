package com.example.sqllitedemo

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.sqllitedemo.model.User
import kotlinx.android.synthetic.main.items_row.view.*

class UserAdapter(val items: ArrayList<User>, val context: Context) :
    RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val view = view
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.ViewHolder {
        return UserAdapter.ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.items_row,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: UserAdapter.ViewHolder, position: Int) {
        val user = items[position]

        Log.e("UI", "Got $user in view")
        holder.view.tv_name.text = user.name
        holder.view.tv_email.text = user.email

        if (position % 2 == 0) {
            holder.view.ll_main.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.colorAccent
                )
            )
        }

        holder.view.btn_edit.setOnClickListener { view ->
            if (context is MainActivity) {
                context.update(user)
            }
        }

        holder.view.btn_delete.setOnClickListener { view ->
            if (context is MainActivity) {
                context.delete(user)
            }
        }

    }

}