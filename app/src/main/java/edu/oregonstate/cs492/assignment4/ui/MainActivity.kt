package edu.oregonstate.cs492.assignment4.ui

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.appbar.MaterialToolbar
import edu.oregonstate.cs492.assignment4.R

class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfig: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        //Setting up navigation
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        //setting up menu bar
        appBarConfig = AppBarConfiguration(navController.graph)

        val appBar: MaterialToolbar = findViewById(R.id.top_app_bar)

        setSupportActionBar(appBar)
        setupActionBarWithNavController(navController, appBarConfig)

        addMenuProvider(
            object :MenuProvider{
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.main_menu,menu)
                }
                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when(menuItem.itemId){
                        R.id.action_settings ->{
                            findNavController(R.id.nav_host_fragment).navigate(R.id.nav_to_settings)
                            true
                        }
                        else -> false
                    }
                }
            },
            this,
            Lifecycle.State.STARTED
        )


    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfig) || super.onSupportNavigateUp()
    }
}