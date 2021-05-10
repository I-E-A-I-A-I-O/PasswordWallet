package com.justdance.passwordwaller

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.justdance.passwordwaller.daoDb.AppDatabase
import com.justdance.passwordwaller.redux.passwords.DeleteAllPasswords
import com.justdance.passwordwaller.redux.store
import com.justdance.passwordwaller.redux.user.RemoveUser
import com.justdance.passwordwaller.ui.passwordCreation.PasswordCreationFragment
import com.justdance.passwordwaller.ui.passwords.PasswordsFragment
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_NoActionBar)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val drawerNavView: NavigationView = findViewById(R.id.drawer_view)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0)
        drawerLayout.addDrawerListener(toggle)
        navView.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.nav_passwords -> {
                    openFragment(PasswordsFragment.newInstance())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_add_password -> {
                    openFragment(PasswordCreationFragment.newInstance())
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }
        drawerNavView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.nav_logout -> {
                    logout()
                    return@setNavigationItemSelectedListener true
                }
                R.id.nav_update_credentials -> {
                    return@setNavigationItemSelectedListener true
                }
            }
            false
        }
    }

    private fun logout() {
        lifecycleScope.launch {
            val database = AppDatabase.getInstance(applicationContext)
            store.dispatch(RemoveUser(null))
            database.userDao().deleteAllUsers()
            store.dispatch(DeleteAllPasswords(null))
            database.passwordDao().deleteAllPasswords()
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK.or(Intent.FLAG_ACTIVITY_CLEAR_TASK))
            startActivity(intent)
        }
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.nav_host_fragment, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}