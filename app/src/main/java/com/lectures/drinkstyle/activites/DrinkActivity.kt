package com.lectures.drinkstyle.activites
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.lectures.drinkstyle.R
import com.lectures.drinkstyle.dataBase.DrinkDataBase
import com.lectures.drinkstyle.databinding.ActivityDrinkBinding
import com.lectures.drinkstyle.fragments.HomeFragment
import com.lectures.drinkstyle.pojo.Drink
import com.lectures.drinkstyle.viewModel.DrinkViewModel
import com.lectures.drinkstyle.viewModel.DrinkViewModelFactory

class DrinkActivity : AppCompatActivity() {

    private lateinit var drinkId:String
    private lateinit var drinkName:String
    private lateinit var drinkThumb:String
    private lateinit var binding: ActivityDrinkBinding
    private lateinit var drinkMvvm: DrinkViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityDrinkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val drinkDataBase = DrinkDataBase.getInstance(context = MainActivity())
        val viewModelFactory = DrinkViewModelFactory(drinkDataBase)
        drinkMvvm = ViewModelProvider(this, viewModelFactory)[DrinkViewModel::class.java]

        getDrinkInfo()
        setInfoInView()
        loadingCase()
        drinkMvvm.getDrinkDetail(drinkId)
        observerDrinkDetailsLiveData()

        onDrinkWebImageClick()
        onFavoriteClick()

    }

    private fun onFavoriteClick(){
        binding.btnAddFav.setOnClickListener {
            drinkSave?.let {
                drinkMvvm.insertDrink(it)
                Toast.makeText(this, "Drink Save", Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun onDrinkWebImageClick(){
        binding.imgWebRecipes.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.acouplecooks.com/best-cocktail-recipes-to-make-at-home/"))
            startActivity(intent)
        }
    }

    private var drinkSave: Drink?=null
    private fun observerDrinkDetailsLiveData() {
        drinkMvvm.observerDrinkDetailsLiveData().observe(this, object : Observer<Drink>{
            override fun onChanged(t: Drink?) {
                onResponseCase()
                val drink = t
                drinkSave = drink

                binding.tvCategory.text = "Category : ${drink!!.strCategory}"
                binding.tvInstructionSteps.text = drink.strInstructions


            }
        })

    }

    private fun setInfoInView() {
        Glide.with(applicationContext)
            .load(drinkThumb)
            .into(binding.imgDrinkDetails)

        binding.collapsingToolbar.title = drinkName
        binding.collapsingToolbar.setCollapsedTitleTextColor(resources.getColor(R.color.white))
        binding.collapsingToolbar.setExpandedTitleColor(resources.getColor(R.color.white))
    }

    private fun getDrinkInfo() {
        val intent = intent
        drinkId = intent.getStringExtra(HomeFragment.DRINK_ID)!!
        drinkName = intent.getStringExtra(HomeFragment.DRINK_NAME)!!
        drinkThumb = intent.getStringExtra(HomeFragment.DRINK_THUMB)!!
    }

    private fun loadingCase(){
        binding.progressBar.visibility = View.VISIBLE
        binding.btnAddFav.visibility = View.INVISIBLE
        binding.tvInstruction.visibility = View.INVISIBLE
        binding.tvCategory.visibility = View.INVISIBLE
        binding.imgWebRecipes.visibility = View.INVISIBLE
    }

    private fun onResponseCase(){
        binding.progressBar.visibility = View.INVISIBLE
        binding.btnAddFav.visibility = View.VISIBLE
        binding.tvInstruction.visibility = View.VISIBLE
        binding.tvCategory.visibility = View.VISIBLE
        binding.imgWebRecipes.visibility = View.VISIBLE
    }
}