package com.example.officemanagerapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.officemanagerapp.R
import com.example.officemanagerapp.database.UserPreferences
import com.example.officemanagerapp.databinding.ActivityMainBinding
import com.example.officemanagerapp.ui.auth.AuthActivity
import com.example.officemanagerapp.ui.profile.ProfileViewModel
import com.example.officemanagerapp.ui.tempModels.UserFirebase
import com.example.officemanagerapp.util.startNewActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    @Inject
    lateinit var userPreferences: UserPreferences

    private val viewModel by viewModels<ProfileViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginInFirebase()
        //loginSetUp()
        navigationConfigurationSetUp()
    }

    fun performLogout() = lifecycleScope.launch {
        viewModel.logout()
        userPreferences.clear()
        startNewActivity(AuthActivity::class.java)
    }

    /*private fun loginSetUp() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.nav_graph)

        if (!isLogged) {
            graph.startDestination = R.id.loginFragment
        } else {
            graph.startDestination = R.id.homeFragment
        }
        navHostFragment.navController.graph = graph
    }*/

    private fun navigationConfigurationSetUp() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.chatFragment,
                R.id.paymentFragment,
                R.id.profileFragment
            ))

        binding.bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> showBottomNav()
                R.id.chatFragment -> showBottomNav()
                R.id.paymentFragment -> showBottomNav()
                R.id.profileFragment -> showBottomNav()
                else -> hideBottomNav()
            }
        }

        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    private fun showBottomNav() {
        binding.bottomNavigationView.visibility = View.VISIBLE
    }

    private fun hideBottomNav() {
        binding.bottomNavigationView.visibility = View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
    private fun loginInFirebase(){
        val login = "Test1@gmail.com"
        val password = "Password"
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(login, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("AAA", "${it.result?.user?.uid}")
                    return@addOnCompleteListener
                }
            }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(login, password)
            .addOnSuccessListener {
                val uid = FirebaseAuth.getInstance().uid ?: ""
                val user = UserFirebase(
                    login,
                    uid,
                    "Алексей",
                    "Алексеев",
                    "Алексеевич",
                    "89999999999",
                    "*********",
                    "421",
                    "4")
                val my_rev = FirebaseDatabase.getInstance().getReference("users/$uid")
                my_rev.setValue(user)
                Log.d("AAA", "success")
                return@addOnSuccessListener
            }
    }
}