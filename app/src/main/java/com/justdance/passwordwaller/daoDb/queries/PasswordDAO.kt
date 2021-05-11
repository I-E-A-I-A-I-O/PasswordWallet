package com.justdance.passwordwaller.daoDb.queries

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.justdance.passwordwaller.daoDb.entities.Password

@Dao
interface PasswordDAO {
    @Query("SELECT * FROM password")
    suspend fun getAll(): List<Password>
    @Insert
    suspend fun insertAll(vararg passwords: Password)
    @Query("DELETE FROM password")
    suspend fun deleteAllPasswords()
    @Query("DELETE FROM password WHERE passwordId = :id")
    suspend fun deletePassword(id: String)
}