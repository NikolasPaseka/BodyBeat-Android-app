package cz.mendelu.xpaseka.bodybeat

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import cz.mendelu.xpaseka.bodybeat.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.PlansFragment,
                R.id.ScheduleFragment
            )
        )
        setupActionBarWithNavController(navController!!, appBarConfiguration!!)

//        appBarConfiguration = AppBarConfiguration(navController.graph)
//        setupActionBarWithNavController(navController, appBarConfiguration)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            manageMainScreen(destination.id)
        }

        setBottomNavListeners()
    }

    private fun manageMainScreen(fragmentId: Int){
        if (fragmentId != R.id.PlanProgressFragment) {
            runOnUiThread {
                binding.bottomNav.visibility = View.VISIBLE
            }
        } else {
            runOnUiThread {
                binding.bottomNav.visibility = View.GONE
            }
        }
    }

    private fun setBottomNavListeners() {
        binding.bottomNav.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.ic_plans -> replaceFragment(R.id.PlansFragment)
                R.id.ic_schedule -> replaceFragment(R.id.ScheduleFragment)
            }
            true
        }
    }

    private fun replaceFragment(fragmentId: Int) {
//            val transaction = supportFragmentManager.beginTransaction()
//            transaction.replace(R.id.nav_host_fragment_content_main, fragment)
//            transaction.commit()
            findNavController(R.id.nav_host_fragment_content_main).navigate(fragmentId)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}