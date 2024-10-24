package com.example.celestialconnect

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

class AccountActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var fragmentManager: FragmentManager
    private lateinit var toolbar: Toolbar
    private lateinit var fab: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        fab = findViewById(R.id.fab)//
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById<NavigationView>(R.id.navigation_drawer)
        navigationView.setNavigationItemSelectedListener(this)

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.background = null

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
    //TODO:*************************** \/ EDIT HERE ******************************************************

                R.id.bottom_home -> {
                    openFragment(HomeFragment())
                    true
                }
                R.id.bottom_Community -> {
                    openFragment(ShortsFragment())
                    true
                }
                R.id.bottom_subscriptions -> {
                    openFragment(SubscriptionsFragment())
                    true
                }
                R.id.bottom_profile -> {
                    openFragment(ProfileFragment())
                    true
                }


    //TODO:*************************** /\ EDIT HERE ******************************************************
                else -> false
            }
        }

        fragmentManager = supportFragmentManager
        openFragment(HomeFragment())

        fab.setOnClickListener {
            Toast.makeText(this@AccountActivity, "Upload Image", Toast.LENGTH_SHORT).show()
            openFragment(UploadFragment())
            true
        }
    }


    //TODO:*************************** \/  EDIT HERE ******************************************************
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> openFragment(HomeFragment())
            R.id.nav_chat -> {
                val intent = Intent(this, CHLoginActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_settings ->  {
                val intent = Intent(this, NatalChart::class.java)
                startActivity(intent)
            }
            R.id.nav_share -> openFragment(DailyReadingFragment())
            R.id.nav_about -> openFragment(AboutFragment())

            //do this to move to an Activity\/
            R.id.nav_logout -> {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }

    //TODO:*************************** /\ EDIT HERE ******************************************************
        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun openFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }
}
