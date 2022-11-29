package com.lectures.drinkstyle.fragments


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import com.lectures.drinkstyle.R
import com.lectures.drinkstyle.activites.DrinkActivity
import com.lectures.drinkstyle.activites.MainActivity
import com.lectures.drinkstyle.adapters.AlcoholicAdapter
import com.lectures.drinkstyle.adapters.MostPopularAdapter
import com.lectures.drinkstyle.pojo.Drink
import com.lectures.drinkstyle.pojo.DrinksByPopular
import com.lectures.drinkstyle.viewModel.HomeViewModel
import com.lectures.drinkstyle.databinding.FragmentHomeBinding
import com.lectures.drinkstyle.pojo.DrinksList
import com.lectures.drinkstyle.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment() {


    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeMvvm: HomeViewModel
    private lateinit var randomDrink: Drink
    private lateinit var popularItemAdapter: MostPopularAdapter
    private lateinit var alcoholicAdapter: AlcoholicAdapter

    companion object{
        const val DRINK_ID = "com.lectures.drinkstyle.fragments.idDrink"
        const val DRINK_NAME = "com.lectures.drinkstyle.fragments.nameDrink"
        const val DRINK_THUMB = "com.lectures.drinkstyle.fragments.thumbDrink"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeMvvm = (activity as MainActivity).viewModel
        popularItemAdapter = MostPopularAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        RetrofitInstance.api.getRandomDrink().enqueue(object : Callback<DrinksList>{
            override fun onResponse(call: Call<DrinksList>, response: Response<DrinksList>) {
                if(response.body() != null){
                    val randomDrink: Drink = response.body()!!.drinks[0]
                    Glide.with(this@HomeFragment)
                        .load(randomDrink.strDrinkThumb)
                        .into(binding.imgRandomDrink)
                }else
                    return
            }

            override fun onFailure(call: Call<DrinksList>, t: Throwable) {
                Log.d("HomeFragment", t.message.toString())
            }
        })

        obtainPopularItemRecyclerView()

        
        homeMvvm.getRandomDrink()
        observerRandomDrink()
        onRandomDrinkClick()
        onYouTubeImageClick()

        homeMvvm.getPopularItems()
        observePopularItemsLiveData()
        onPopularItemClick()

        obtainAlcoholicRecyclerView()
        homeMvvm.getAlcoholicItem()
        observeAlcoholicLiveData()
        onAlcoholicItemClick()
        onSearchIconClick()

    }

    private fun onSearchIconClick(){
        binding.imgSearch.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
    }

    private fun onYouTubeImageClick(){
        binding.imgYoutube.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=FrFSeuAJd9o&t=548s&ab_channel=5-MinuteCraftsVS"))
            startActivity(intent)
        }
    }

    private fun onAlcoholicItemClick(){
        alcoholicAdapter.onItemClick = { drinks->
            val intent = Intent(activity, DrinkActivity::class.java)
            intent.putExtra(DRINK_ID,drinks.idDrink)
            intent.putExtra(DRINK_NAME,drinks.strDrink)
            intent.putExtra(DRINK_THUMB,drinks.strDrinkThumb)
            startActivity(intent)
        }
    }

    private fun obtainAlcoholicRecyclerView(){
        alcoholicAdapter = AlcoholicAdapter()
        binding.recViewAlcoholic.apply {
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
            adapter = alcoholicAdapter
        }
    }

    private fun observeAlcoholicLiveData(){
        homeMvvm.observeAlcoholicLiveData().observe(viewLifecycleOwner, Observer { alcoholic->
                alcoholicAdapter.setAlcoholicList(alcoholic)

        })
    }

    private fun onPopularItemClick() {
        popularItemAdapter.onItemClick = { drinks->
            val intent = Intent(activity, DrinkActivity::class.java)
            intent.putExtra(DRINK_ID,drinks.idDrink)
            intent.putExtra(DRINK_NAME,drinks.strDrink)
            intent.putExtra(DRINK_THUMB,drinks.strDrinkThumb)
            startActivity(intent)
        }
    }

    private fun obtainPopularItemRecyclerView(){
        binding.recViewDrinksPopular.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularItemAdapter
        }
    }

    private fun observePopularItemsLiveData() {
        homeMvvm.observePopularItemsLiveData().observe(viewLifecycleOwner
        ) { drinksList ->
            popularItemAdapter.setDrinks(drinksList = drinksList as ArrayList<DrinksByPopular>)
        }
    }

    private fun onRandomDrinkClick() {
        binding.randomDrinkCard.setOnClickListener {
            val intent = Intent(activity,DrinkActivity::class.java)
            intent.putExtra(DRINK_ID, randomDrink.idDrink)
            intent.putExtra(DRINK_NAME, randomDrink.strDrink)
            intent.putExtra(DRINK_THUMB, randomDrink.strDrinkThumb)
            startActivity(intent)
        }

    }

    private fun observerRandomDrink() {
        homeMvvm.observeRandomDrinkLiveData().observe(viewLifecycleOwner
        ) { drink ->
            Glide.with(this@HomeFragment)
                .load(drink!!.strDrinkThumb)
                .into(binding.imgRandomDrink)

            this.randomDrink = drink
        }
    }

}