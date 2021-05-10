package com.justdance.passwordwaller.daoDb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.justdance.passwordwaller.daoDb.entities.Password
import com.justdance.passwordwaller.daoDb.entities.User
import com.justdance.passwordwaller.daoDb.queries.PasswordDAO
import com.justdance.passwordwaller.daoDb.queries.UserDAO

@Database(entities = [User::class, Password::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun userDao(): UserDAO
    abstract fun passwordDao(): PasswordDAO

    companion object {
        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also {instance = it}
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, "app-storage").build()
        }
    }
}