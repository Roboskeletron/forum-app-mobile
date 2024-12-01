package ru.vsu.forum.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import ru.vsu.forum.R
import ru.vsu.forum.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layout = binding.collapsingToolbarLayout

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        val host = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment? ?: return
        val navController = host.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)
        layout.setupWithNavController(toolbar, navController, appBarConfiguration)
    }

//    override fun onSupportNavigateUp(): Boolean {
//        return findNavController(R.id.nav_host_fragment_activity_main).navigateUp(appBarConfiguration)
//    }
}