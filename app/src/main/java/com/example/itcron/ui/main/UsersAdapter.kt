package com.example.itcron.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.itcron.R
import com.example.itcron.data.model.UserModel

internal class UsersAdapter(
    val onUserClick: (UserModel)-> Unit
) : RecyclerView.Adapter<UsersAdapter.UsersViewHolder>() {

    var usersList: List<UserModel> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        return UsersViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        holder.setUser(usersList[position])
    }

    override fun getItemCount(): Int {
        return usersList.size
    }

    inner class UsersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val imgAvatar: ImageView
        private val txtLogin: TextView
        private val txtId: TextView

        init {
            imgAvatar = itemView.findViewById(R.id.imgAvatar)
            txtLogin = itemView.findViewById(R.id.txtLogin)
            txtId = itemView.findViewById(R.id.txtId)
        }

        fun setUser(user: UserModel){
            Glide.with(itemView.context).load(user.avatar_url).into(imgAvatar)
            txtLogin.text = user.login
            txtId.text = user.id.toString()
            itemView.setOnClickListener { onUserClick(user) }
        }
    }
}