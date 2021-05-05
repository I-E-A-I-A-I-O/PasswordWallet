package com.justdance.passwordwaller

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController

class LoginActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        val host: NavHostFragment = supportFragmentManager.
            findFragmentById(R.id.login_nav_fragment) as NavHostFragment? ?: return
        val navController = host.navController
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.loginFragment, R.id.registerFragment
        ))
        setupActionBarWithNavController(navController, appBarConfiguration)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.login_nav_fragment).navigateUp(appBarConfiguration)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(findNavController(R.id.login_nav_fragment))
                || super.onOptionsItemSelected(item)
    }
}