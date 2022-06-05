package cz.mendelu.xpaseka.bodybeat

import android.content.res.Resources
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import cz.mendelu.xpaseka.bodybeat.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private lateinit var dataStore: DataStore<androidx.datastore.preferences.core.Preferences>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //setContentView(binding.root)
        dataStore = createDataStore("settings")
        setupAppLocale()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }

    private fun setupAppLocale() {
        var lang: String? = null
        lifecycleScope.launch {
            lang = readFromDataStore("language")
        }.invokeOnCompletion {
            if (lang != null && lang != "system") {
                val loc = Locale(lang!!)
                Locale.setDefault(loc)
                val resources: Resources = this.resources
                val config = resources.configuration
                config.setLocale(loc)
                resources.updateConfiguration(config, resources.getDisplayMetrics())
                setupNavigation()
            } else {
                setupNavigation()
            }
        }
    }

    private fun setupNavigation() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        //appBarConfiguration = AppBarConfiguration(navController.graph)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.PlansFragment,
                R.id.ScheduleFragment,
                R.id.ParksFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            manageMainScreen(destination.id)
        }

        setBottomNavListeners()
    }

    suspend fun saveToDataStore(key: String, value: String) {
        val dataStoreKey = preferencesKey<String>(key)
        dataStore.edit { settings ->
            settings[dataStoreKey] = value
        }
    }

    suspend fun readFromDataStore(key: String): String? {
        val dataStoreKey = preferencesKey<String>(key)
        val preferences = dataStore.data.first()
        return preferences[dataStoreKey]
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
                R.id.ic_parks -> replaceFragment(R.id.ParksFragment)
            }
            item.icon.setColorFilter(resources.getColor(R.color.orange), PorterDuff.Mode.MULTIPLY)
            DrawableCompat.setTint(item.icon, ContextCompat.getColor(this, android.R.color.holo_blue_light))
            true
        }
    }

    private fun replaceFragment(fragmentId: Int) {
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
            R.id.action_settings -> {
                replaceFragment(R.id.SettingsFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}