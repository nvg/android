package com.example.sqllitedemo

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sqllitedemo.database.DatabaseHandler
import com.example.sqllitedemo.model.User
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_update.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadUsers()

        btn_add.setOnClickListener {
            val name = et_add_name.text.toString()
            val email = et_add_email.text.toString()

            val db = DatabaseHandler(this)
            db.addUser(User(name, email))
            Toast.makeText(applicationContext, "Added user", Toast.LENGTH_LONG).show()

            et_add_name.text.clear()
            et_add_email.text.clear()

            loadUsers()
        }
    }

    fun delete(user: User) {
        var builder = AlertDialog.Builder(this)
        builder.setTitle("Delete user")
        builder.setMessage("Are you sure you want to delete user ${user.name}")
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton("Yes") { dialog, which ->
            val db = DatabaseHandler(this@MainActivity)
            db.deleteUser(user)
            loadUsers()
            dialog.dismiss()
        }
        builder.setNegativeButton("No") { dialog, which ->
            dialog.dismiss()
        }

        var alert = builder.create()
        alert.setCancelable(false)
        alert.show()
    }

    fun update(user: User) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_update)
        dialog.setCancelable(false)
        dialog.setTitle("Update user")

        dialog.et_edit_name.setText(user.name)
        dialog.et_edit_email.setText(user.email)

        dialog.btn_update.setOnClickListener {
            val name = dialog.et_edit_name.text.toString()
            val email = dialog.et_edit_email.text.toString()
            val db = DatabaseHandler(this@MainActivity)
            db.updateUser(User(name, email))
            loadUsers()
            dialog.dismiss()
        }

        dialog.btn_cancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun loadUsers() {
        val users = DatabaseHandler(this).getUsers()
        if (users.isEmpty()) {
            return
        }

        Log.e("UI", "Loaded $users")

        rv_users.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_users.adapter = UserAdapter(users, this)
    }
}
