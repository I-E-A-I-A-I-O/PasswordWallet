package com.justdance.passwordwaller.daoDb.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Password (
    @PrimaryKey val passwordId: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "password") val password: String
    )