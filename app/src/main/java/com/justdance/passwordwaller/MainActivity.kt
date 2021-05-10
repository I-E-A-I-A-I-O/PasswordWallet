package com.justdance.passwordwaller

import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.justdance.passwordwaller.ui.passwordCreation.PasswordCreationFragment
import com.justdance.passwordwaller.ui.passwords.PasswordsFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_NoActionBar)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
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
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.nav_host_fragment, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}