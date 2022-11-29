package com.lectures.drinkstyle.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.lectures.drinkstyle.activites.DrinkActivity
import com.lectures.drinkstyle.activites.MainActivity
import com.lectures.drinkstyle.adapters.DrinksAdapter
import com.lectures.drinkstyle.adapters.MostPopularAdapter
import com.lectures.drinkstyle.databinding.FragmentFavoritesBinding
import com.lectures.drinkstyle.viewModel.HomeViewModel

class FavoritesFragment : Fragment() {
    private lateinit var binding:FragmentFavoritesBinding
    private lateinit var homeMvvm: HomeViewModel
    private lateinit var drinksAdapter: DrinksAdapter

    companion object{
        const val DRINK_ID = "com.lectures.drinkstyle.fragments.idDrink"
        const val DRINK_NAME = "com.lectures.drinkstyle.fragments.nameDrink"
        const val DRINK_THUMB = "com.lectures.drinkstyle.fragments.thumbDrink"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeMvvm = (activity as MainActivity).viewModel
        drinksAdapter = DrinksAdapter()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        observeFavorites()
        obtainFavorites()
        onFavoritesClickItem()

        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ) = true

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val deletedDrink = drinksAdapter.differ.currentList[position]
                homeMvvm.deleteDrink(deletedDrink)
                Snackbar.make(requireView(),"Meal Deleted",Snackbar.LENGTH_LONG).setAction(
                    "Undo",
                    View.OnClickListener {
                        homeMvvm.insertDrink(deletedDrink)
                    }).show()
            }
        }
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.rcvFavorites)
    }

    private fun obtainFavorites(){
        drinksAdapter = DrinksAdapter()
        binding.rcvFavorites.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = drinksAdapter
        }
    }

    private fun observeFavorites(){
        homeMvvm.observerFavoritesDrinkLiveData().observe(viewLifecycleOwner, Observer { drinks->
            drinksAdapter.differ.submitList(drinks)
        })
    }

    private fun onFavoritesClickItem(){
        drinksAdapter.setOnItemClickListener {drink->
            val intent = Intent(activity, DrinkActivity::class.java)
            intent.putExtra(DRINK_ID,drink.idDrink)
            intent.putExtra(DRINK_NAME,drink.strDrink)
            intent.putExtra(DRINK_THUMB,drink.strDrinkThumb)
            startActivity(intent)
        }

    }

}