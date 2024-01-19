package com.fazztrack.culinaryvault.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fazztrack.culinaryvault.databinding.MealItemsBinding
import com.fazztrack.culinaryvault.pojo.Meal

class FavoritesMealsAdapter: RecyclerView.Adapter<FavoritesMealsAdapter.FavoritesMealsViewHolder>() {
    inner class FavoritesMealsViewHolder(val binding: MealItemsBinding): RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object: DiffUtil.ItemCallback<Meal>() {
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem.idMeal == newItem.idMeal
        }

        override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesMealsViewHolder {
        return FavoritesMealsViewHolder(
            MealItemsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: FavoritesMealsViewHolder, position: Int) {
        val meal = differ.currentList[position]
        Glide
            .with(holder.itemView)
            .load(meal.strMealThumb)
            .into(holder.binding.ivMeal)

        holder.binding.tvMealName.text = meal.strMeal
    }


}