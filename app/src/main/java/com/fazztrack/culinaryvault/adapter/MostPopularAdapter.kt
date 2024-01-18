package com.fazztrack.culinaryvault.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fazztrack.culinaryvault.databinding.PopularFoodsBinding
import com.fazztrack.culinaryvault.pojo.CategoryMeals

class MostPopularAdapter(): RecyclerView.Adapter<MostPopularAdapter.PopularMealViewHolder>() {
    lateinit var onFoodClick: ((CategoryMeals) -> Unit)
    private var mealsList = ArrayList<CategoryMeals>()

    fun setMeals(mealsList: ArrayList<CategoryMeals>) {
        this.mealsList = mealsList
        notifyDataSetChanged()
    }

    class PopularMealViewHolder(var binding: PopularFoodsBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMealViewHolder {
        return PopularMealViewHolder(PopularFoodsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: PopularMealViewHolder, position: Int) {
        Glide
            .with(holder.itemView)
            .load(mealsList[position].strMealThumb)
            .into(holder.binding.ivPopularFoodItems)

        holder.itemView.setOnClickListener {
            onFoodClick.invoke(mealsList[position])
        }
    }

    override fun getItemCount(): Int {
        return mealsList.size
    }
}