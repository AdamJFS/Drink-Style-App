package com.lectures.drinkstyle.adapters
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lectures.drinkstyle.databinding.AlcoholicItemBinding
import com.lectures.drinkstyle.databinding.FavoriteItemBinding
import com.lectures.drinkstyle.fragments.FavoritesFragment
import com.lectures.drinkstyle.pojo.AlcoholicList
import com.lectures.drinkstyle.pojo.Drink
import com.lectures.drinkstyle.pojo.DrinkAlcoholic


class DrinksAdapter() : RecyclerView.Adapter<DrinksAdapter.FavoritesAdapterViewHolder>(){



    inner class FavoritesAdapterViewHolder(val binding:FavoriteItemBinding) : RecyclerView.ViewHolder(binding.root){
        lateinit var onItemClick:((DrinkAlcoholic) -> Unit)

    }

    // diffUtil is improve the recyclerView only refresh the item that are new!!
    private val diffUtil = object : DiffUtil.ItemCallback<Drink>(){
        override fun areItemsTheSame(oldItem: Drink, newItem: Drink): Boolean {
            return oldItem.idDrink == newItem.idDrink
        }
        override fun areContentsTheSame(oldItem: Drink, newItem: Drink): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesAdapterViewHolder {
        return FavoritesAdapterViewHolder(
            FavoriteItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: FavoritesAdapterViewHolder, position: Int) {
        val drink = differ.currentList[position]
        Glide.with(holder.itemView)
            .load(drink.strDrinkThumb)
            .into(holder.binding.imgFavorite)
        holder.binding.tvFavoriteName.text = drink.strDrink

        holder.itemView.setOnClickListener {
            onItemOnClickListener.apply {
                onItemOnClickListener?.invoke(drink)
            }
        }


    }


    private var onItemOnClickListener: ((Drink) -> Unit)? = null

    fun setOnItemClickListener(listener: (Drink) -> Unit){
        onItemOnClickListener = listener
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }



}



