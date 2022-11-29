package com.lectures.drinkstyle.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.lectures.drinkstyle.R
import com.lectures.drinkstyle.activites.DrinkActivity
import com.lectures.drinkstyle.activites.MainActivity
import com.lectures.drinkstyle.adapters.AlcoholicAdapter
import com.lectures.drinkstyle.databinding.FragmentAlcoholicBinding
import com.lectures.drinkstyle.viewModel.HomeViewModel


class AlcoholicFragment : Fragment() {

    companion object{
        const val DRINK_ID = "com.lectures.drinkstyle.fragments.idDrink"
        const val DRINK_NAME = "com.lectures.drinkstyle.fragments.nameDrink"
        const val DRINK_THUMB = "com.lectures.drinkstyle.fragments.thumbDrink"
    }


    private lateinit var binding:FragmentAlcoholicBinding
    private lateinit var alcoholicAdapter:AlcoholicAdapter
    private lateinit var homeMvvm: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeMvvm = (activity as MainActivity).viewModel
        alcoholicAdapter = AlcoholicAdapter()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlcoholicBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        obtainRecyclerView()
        observeAlcoholic()
        onAlcoholicItemClick()
    }

    private fun observeAlcoholic() {
        homeMvvm.observeAlcoholicLiveData().observe(viewLifecycleOwner, Observer { alcoholic->
            alcoholicAdapter.setAlcoholicList(alcoholic)
        })
    }

    private fun obtainRecyclerView() {
        alcoholicAdapter = AlcoholicAdapter()
        binding.rcvAlcoholic.apply {
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
            adapter = alcoholicAdapter
        }
    }



    private fun onAlcoholicItemClick(){
        alcoholicAdapter.onItemClick = { drinks->
            val intent = Intent(activity, DrinkActivity::class.java)
            intent.putExtra(HomeFragment.DRINK_ID,drinks.idDrink)
            intent.putExtra(HomeFragment.DRINK_NAME,drinks.strDrink)
            intent.putExtra(HomeFragment.DRINK_THUMB,drinks.strDrinkThumb)
            startActivity(intent)
        }
    }

}