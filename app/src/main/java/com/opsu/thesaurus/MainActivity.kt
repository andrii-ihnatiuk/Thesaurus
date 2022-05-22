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
import com.opsu.thesaurus.database.entities.Entities


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var homeFragment: HomeFragment

    private var newSetLauncher = registerForActivityResult(StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val set: Entities.Set = result.data?.getSerializableExtra("NewSet") as Entities.Set
            Toast.makeText(this, "New set added", Toast.LENGTH_SHORT).show()

            homeFragment.addNewSet(set)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        homeFragment = HomeFragment()
        val profileFragment = ProfileFragment()

        setCurrentFragment(homeFragment)

        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.ic_home -> setCurrentFragment(homeFragment)
                R.id.ic_add -> createNewSet()
                R.id.ic_profile -> setCurrentFragment(profileFragment)
            }
            true
        }
    }

    private fun createNewSet() {
        val intent = Intent(this,  CreateSetActivity::class.java)
        newSetLauncher.launch(intent)
    }

    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, fragment)
            .setReorderingAllowed(true)
            .commit()
    }
}

