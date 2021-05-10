package com.justdance.passwordwaller.daoDb.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_id")
    val userId: Long = 0,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "token") val token: String
    )
