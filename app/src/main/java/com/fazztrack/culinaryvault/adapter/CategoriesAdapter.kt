package com.fazztrack.culinaryvault.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fazztrack.culinaryvault.databinding.CategoryItemBinding
import com.fazztrack.culinaryvault.pojo.Category
import com.fazztrack.culinaryvault.pojo.CategoryList

class CategoriesAdapter(): RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>() {
    private var categoriesList = ArrayList<Category>()
    var onMealsClick : ((Category) -> Unit)? = null

    fun setCategoryList(categoriesList: List<Category>) {
        this.categoriesList = categoriesList as ArrayList<Category>
        notifyDataSetChanged()
    }

    inner class CategoryViewHolder(val binding: CategoryItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            CategoryItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun getItemCount(): Int {
        return categoriesList.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        Glide
            .with(holder.itemView)
            .load(categoriesList[position].strCategoryThumb)
            .into(holder.binding.ivCategory)

        holder.binding.tvCategoryName.text = categoriesList[position].strCategory

        holder.itemView.setOnClickListener {
            onMealsClick!!.invoke(categoriesList[position])
        }
    }


}