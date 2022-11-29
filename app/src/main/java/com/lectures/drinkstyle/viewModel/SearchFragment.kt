package com.lectures.drinkstyle.viewModel

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.clearFragmentResult
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lectures.drinkstyle.R
import com.lectures.drinkstyle.activites.DrinkActivity
import com.lectures.drinkstyle.activites.MainActivity
import com.lectures.drinkstyle.adapters.DrinksAdapter
import com.lectures.drinkstyle.databinding.FragmentSearchBinding
import com.lectures.drinkstyle.fragments.HomeFragment
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var homeMvvm: HomeViewModel
    private lateinit var searchRecyclerViewAdapter:DrinksAdapter

    companion object{
        const val DRINK_ID = "com.lectures.drinkstyle.fragments.idDrink"
        const val DRINK_NAME = "com.lectures.drinkstyle.fragments.nameDrink"
        const val DRINK_THUMB = "com.lectures.drinkstyle.fragments.thumbDrink"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        homeMvvm = (activity as MainActivity).viewModel

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        obtainRecyclerView()

        binding.imgSearchArrow.setOnClickListener { searchDrinks() }

        observeSearchDrinkLiveData()
        onSearchItemClick()

        var searchSomething: Job? = null
        binding.searchBox.addTextChangedListener { searchQuery->
            searchSomething?.cancel()
            searchSomething = lifecycleScope.launch {
                delay(500)
                homeMvvm.searchDrink(searchQuery.toString())
            }
        }
    }

    private fun observeSearchDrinkLiveData(){
        homeMvvm.observeSearchDrinkLiveData().observe(viewLifecycleOwner, Observer { drinksList->
            searchRecyclerViewAdapter.differ.submitList(drinksList)
        })
    }

    private fun searchDrinks(){
        val searchQuery = binding.searchBox.text.toString()
        if(searchQuery.isNotEmpty()){
            homeMvvm.searchDrink(searchQuery)
        }
    }

    private fun obtainRecyclerView(){
        searchRecyclerViewAdapter = DrinksAdapter()
        binding.rcvSearchDrinks.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = searchRecyclerViewAdapter
        }
    }

    private fun onSearchItemClick(){
        searchRecyclerViewAdapter.setOnItemClickListener { drinks->
            val intent = Intent(activity, DrinkActivity::class.java)
            intent.putExtra(HomeFragment.DRINK_ID,drinks.idDrink)
            intent.putExtra(HomeFragment.DRINK_NAME,drinks.strDrink)
            intent.putExtra(HomeFragment.DRINK_THUMB,drinks.strDrinkThumb)
            startActivity(intent)
        }
    }

}