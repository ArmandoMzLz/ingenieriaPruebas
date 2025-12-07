package com.example.animalcrossing

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainMenu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.main_menu)


        val userName = intent.getStringExtra("UserName") ?: "Usuario"
        val userEmail = intent.getStringExtra("UserEmail") ?: "Correo"
        val bottomNav: BottomNavigationView = findViewById(R.id.bottomNavigationView)

        if(savedInstanceState == null) {
            loadFragment(Home.newInstance(userName, userEmail))
        }

        bottomNav.setOnItemSelectedListener { item ->
            val fragment = when(item.itemId) {
                R.id.bottom_account -> Account()
                R.id.bottom_pet -> Pet.newInstance(userEmail)
                R.id.bottom_home -> Home.newInstance(userName, userEmail)
                R.id.bottom_route -> Route()
                R.id.bottom_message -> Message()
                else -> null
            }

            fragment?.let {
                loadFragment(it)
                true
            } ?: false
        }

        bottomNav.selectedItemId = R.id.bottom_home
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_contanier, fragment)
            .commit()
    }
}