package com.picpay.desafio.android.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.picpay.desafio.android.databinding.ListItemUserBinding
import com.picpay.desafio.android.domain.model.User

class UserListAdapter : RecyclerView.Adapter<UserListAdapter.UserListViewHolder>() {
    private var users = emptyList<User>()
        set(value) {
            val result = DiffUtil.calculateDiff(UserListDiffCallback(field, value))
            result.dispatchUpdatesTo(this)
            field = value
        }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): UserListViewHolder = UserListViewHolder(
        ListItemUserBinding.inflate(LayoutInflater.from(parent.context))
    )

    override fun onBindViewHolder(holder: UserListViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount(): Int = users.size

    fun update(data: List<User>) {
        users = data
        notifyItemChanged(0, data.size)
    }

    inner class UserListViewHolder(
        private val binding: ListItemUserBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.user = user
        }
    }
}