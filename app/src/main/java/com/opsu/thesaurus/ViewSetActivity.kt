package com.opsu.thesaurus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.opsu.thesaurus.adapters.SliderAdapter
import com.opsu.thesaurus.adapters.TermsAdapter
import com.opsu.thesaurus.database.entities.Entities
import com.opsu.thesaurus.database.viewmodels.HomeViewModel
import com.opsu.thesaurus.databinding.ActivityViewSetBinding
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import com.opsu.thesaurus.database.viewmodels.ViewSetViewModel
import com.opsu.thesaurus.database.viewmodels.ViewSetViewModelFactory
import java.lang.ClassCastException
import kotlin.math.abs

class ViewSetActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityViewSetBinding
    private lateinit var vp2: ViewPager2
    private lateinit var loadedSet: Entities.Set
    private lateinit var mViewSetViewModel: ViewSetViewModel

    private lateinit var termsAdapter: TermsAdapter
    private lateinit var termsSliderAdapter: SliderAdapter

    private var terms: List<Entities.Term> = emptyList()
    private var editSetLauncher = registerForActivityResult(StartActivityForResult()
    ) { result ->
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityViewSetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar.root)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.toolbar.toolbarBack.setOnClickListener {
            onBackPressed()
        }

        // List adapters
        termsAdapter = TermsAdapter(layoutInflater)
        termsSliderAdapter = SliderAdapter(layoutInflater)

        // Setting up a ViewPager2
        vp2 = binding.viewPagerTermSlider
        vp2.adapter = termsSliderAdapter

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
        binding.termsList.adapter = termsAdapter
        binding.termsList.layoutManager = TermsAdapter.NoScrollLinearLayoutManager(this)


        // Retrieving data from intent
        val extras = intent.extras
        if (extras != null)
        {
            try {
                val setId = intent.getLongExtra("setId", -1)
                mViewSetViewModel = ViewModelProvider(this, ViewSetViewModelFactory(application, setId))[ViewSetViewModel::class.java]

                mViewSetViewModel.readSetData.observe(this) {
                    if (it != null) {
                        loadedSet = it
                        binding.txtSetTitle.text = it.setTitle
                        binding.txtTermsCount.text = getString(R.string.terms_count, it.numOfTerms)
                    }
                }

                mViewSetViewModel.readAllTerms.observe(this) {
                    terms = it
                    termsAdapter.submitList(it)
                    termsSliderAdapter.submitList(it)
                }
            }
            catch (e: Exception) {
                Log.d("ERROR", "Faced an exception!")
                onBackPressed()
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.view_set_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val mSetViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        return when(item.itemId) {
            R.id.menuEdit -> {
                mViewSetViewModel.getAllTerms().observe(this) {
                    Log.d("allTerms", it.toString())
                }
                val intent = Intent(this, ManageSetActivity::class.java)
                intent.putExtra("setId", loadedSet.setId)
                editSetLauncher.launch(intent)
                true
            }
            R.id.menuDelete -> {
                mSetViewModel.deleteSet(loadedSet.setId)
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}