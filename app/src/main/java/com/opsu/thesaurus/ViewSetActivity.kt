package com.opsu.thesaurus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.opsu.thesaurus.adapters.SliderAdapter
import com.opsu.thesaurus.adapters.TermsAdapter
import com.opsu.thesaurus.database.entities.Entities
import com.opsu.thesaurus.databinding.ActivityViewSetBinding
import java.lang.ClassCastException
import kotlin.math.abs

class ViewSetActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityViewSetBinding
    private lateinit var vp2: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityViewSetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.toolbarBack.setOnClickListener {
            onBackPressed()
        }

        // Retrieving data from intent
        val set: Entities.Set
        var terms: List<Entities.Term> = emptyList()
        val extras = intent.extras
        if (extras != null)
        {
            try {
                set = intent.getSerializableExtra("set") as Entities.Set
                terms = intent.getSerializableExtra("terms") as List<Entities.Term>

                // Setting up set title and number of terms text fields
                binding.txtSetTitle.text = set.setTitle
                binding.txtTermsCount.text = getString(R.string.terms_count, set.numOfTerms)
            }
            catch (e: ClassCastException) {
                Log.d("ERROR", "Faced a class cast exception!")
                onBackPressed()
            }
        }

        // Setting up a ViewPager2
        vp2 = binding.viewPagerTermSlider
        vp2.adapter = SliderAdapter(layoutInflater, terms)

        vp2.clipToPadding = false
        vp2.clipChildren = false
        vp2.offscreenPageLimit = 3
        vp2.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        val pageTransformer = CompositePageTransformer()
        pageTransformer.addTransformer(MarginPageTransformer(15))
        pageTransformer.addTransformer(ViewPager2.PageTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.9f + r * 0.1f
        })

        vp2.setPageTransformer(pageTransformer)

        // Setting up bottom list of terms cards
        binding.termsList.adapter = TermsAdapter(layoutInflater, terms)
        binding.termsList.layoutManager = TermsAdapter.NoScrollLinearLayoutManager(this)

    }
}