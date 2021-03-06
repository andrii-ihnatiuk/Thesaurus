package com.opsu.thesaurus

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.opsu.thesaurus.databinding.ActivityMainBinding
import com.opsu.thesaurus.fragments.HomeFragment
import com.opsu.thesaurus.fragments.ProfileFragment


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var homeFragment: HomeFragment
    private var selectedMenuItemId: Int = 0

    private var newSetLauncher = registerForActivityResult(StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            Toast.makeText(this, "New set added", Toast.LENGTH_SHORT).show()
        }
        binding.bottomNav.menu.findItem(selectedMenuItemId).isChecked = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        homeFragment = HomeFragment()
        val profileFragment = ProfileFragment()

        setCurrentFragment(homeFragment) // initial fragment is home fragment

        binding.bottomNav.setOnItemSelectedListener {
            // we still can get an id of previously selected item
            selectedMenuItemId = binding.bottomNav.selectedItemId

            when (it.itemId) {
                R.id.ic_home -> setCurrentFragment(homeFragment)
                R.id.ic_add -> createNewSet()
                R.id.ic_user -> setCurrentFragment(profileFragment)
            }
            true
        }
    }

    private fun createNewSet() {
        val intent = Intent(this,  ManageSetActivity::class.java)
        newSetLauncher.launch(intent)
    }

    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, fragment)
            .setReorderingAllowed(true)
            .commit()
    }
}

