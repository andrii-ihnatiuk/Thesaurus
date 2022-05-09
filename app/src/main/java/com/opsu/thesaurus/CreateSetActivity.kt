package com.opsu.thesaurus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.opsu.thesaurus.databinding.ActivityCreateSetBinding

class CreateSetActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateSetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateSetBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.toolbar.toolbarBack.setOnClickListener {
            onBackPressed()
        }
    }
}