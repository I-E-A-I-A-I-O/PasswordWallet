package com.justdance.passwordwaller

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.justdance.passwordwaller.ui.passwordCreation.PasswordCreationFragment
import com.justdance.passwordwaller.ui.passwords.PasswordsFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.nav_passwords -> {
                    supportActionBar!!.title = "Saved passwords"
                    openFragment(PasswordsFragment.newInstance())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_add_password -> {
                    supportActionBar!!.title = "Save a new password"
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