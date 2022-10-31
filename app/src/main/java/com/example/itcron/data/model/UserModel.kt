package com.example.itcron.data.model

import java.util.Date

data class UserModel(
    val avatar_url: String,
    val login: String,
    val id: Int,
    val name: String?,
    val email: String?,
    val organisation: String?,
    val following: Int,
    val followers: Int,
    val created_at: Date?
)