package com.justdance.passwordwaller.daoDb.queries

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.justdance.passwordwaller.daoDb.entities.User

@Dao
interface UserDAO {
    @Query("SELECT * FROM user")
    suspend fun getAll(): List<User>
    @Insert
    suspend fun insertAll(vararg users: User)
    @Delete
    suspend fun deleteUser(user: User)
}