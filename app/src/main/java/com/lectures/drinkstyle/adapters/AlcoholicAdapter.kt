package com.lectures.drinkstyle.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lectures.drinkstyle.databinding.AlcoholicItemBinding
import com.lectures.drinkstyle.pojo.DrinkAlcoholic


class AlcoholicAdapter():RecyclerView.Adapter<AlcoholicAdapter.AlcoholicViewHolder>() {
    lateinit var onItemClick:((DrinkAlcoholic) -> Unit)
    private var alcoholicList = ArrayList<DrinkAlcoholic>()

    fun setAlcoholicList(alcoholicList: List<DrinkAlcoholic>){
        this.alcoholicList = alcoholicList as ArrayList<DrinkAlcoholic>
        notifyDataSetChanged()
    }

    inner class AlcoholicViewHolder(val binding: AlcoholicItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlcoholicViewHolder {
        return AlcoholicViewHolder(
            AlcoholicItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: AlcoholicViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(alcoholicList[position].strDrinkThumb)
            .into(holder.binding.imgAlcoholic)
        holder.binding.tvAlcoholicName.text = alcoholicList[position].strDrink

        holder.itemView.setOnClickListener {
            onItemClick.invoke(alcoholicList[position])
        }
    }

    override fun getItemCount(): Int {
        return alcoholicList.size
    }


}