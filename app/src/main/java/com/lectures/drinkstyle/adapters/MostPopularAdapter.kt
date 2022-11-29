package com.lectures.drinkstyle.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lectures.drinkstyle.databinding.PopularItemBinding
import com.lectures.drinkstyle.pojo.DrinksByPopular

class MostPopularAdapter():RecyclerView.Adapter<MostPopularAdapter.PopularDrinkViewHolder>() {
    lateinit var onItemClick:((DrinksByPopular) -> Unit)
    private var drinksList = ArrayList<DrinksByPopular>()

    fun setDrinks(drinksList:ArrayList<DrinksByPopular>){
        this.drinksList = drinksList
        notifyDataSetChanged()
    }

    class PopularDrinkViewHolder(val binding:PopularItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularDrinkViewHolder {
        return PopularDrinkViewHolder(PopularItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: PopularDrinkViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(drinksList[position].strDrinkThumb)
            .into(holder.binding.imgPopularDrinkItem)

        holder.itemView.setOnClickListener {
            onItemClick.invoke(drinksList[position])
        }
    }

    override fun getItemCount(): Int {
        return drinksList.size
    }


}