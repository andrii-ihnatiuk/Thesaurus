package com.opsu.thesaurus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.opsu.thesaurus.adapters.SliderAdapter
import com.opsu.thesaurus.database.entities.Entities
import com.opsu.thesaurus.databinding.ActivityViewSetBinding
import kotlin.math.abs

class ViewSetActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityViewSetBinding
    private val list: List<Entities.Term> = listOf(
        Entities.Term(0, "Term1", "def1"),
        Entities.Term(1, "Term2", "def2"),
        Entities.Term(2, "Term3", "def3")
    )
    private lateinit var vp2: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityViewSetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.toolbarBack.setOnClickListener {
            onBackPressed()
        }


        vp2 = binding.viewPagerTermSlider
        vp2.adapter = SliderAdapter(layoutInflater, list)

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
    }
}