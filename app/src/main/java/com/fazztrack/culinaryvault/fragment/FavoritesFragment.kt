package com.fazztrack.culinaryvault.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.fazztrack.culinaryvault.activity.MainActivity
import com.fazztrack.culinaryvault.activity.MealActivity
import com.fazztrack.culinaryvault.adapter.FavoritesMealsAdapter
import com.fazztrack.culinaryvault.databinding.FragmentFavoritesBinding
import com.fazztrack.culinaryvault.pojo.Meal
import com.fazztrack.culinaryvault.viewmodel.HomeViewModel
import com.google.android.material.snackbar.Snackbar

class FavoritesFragment : Fragment() {
    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var favoritesMealsAdapter: FavoritesMealsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(inflater)
        return binding.root
    }

    private var deletedMeal: Meal? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerView()
        observeFavorites()
        onMealClick()

        val itemTouchHelper = object: ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = true

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
//                viewModel.deleteMeal(favoritesMealsAdapter.differ.currentList[position])
                deletedMeal = favoritesMealsAdapter.differ.currentList[position]

                viewModel.deleteMeal(deletedMeal!!)

                Snackbar.make(requireView(), "Favorites Meal Deleted", Snackbar.LENGTH_LONG)
                    .setAction("Undo",
                        View.OnClickListener {
                            if (!favoritesMealsAdapter.differ.currentList.contains(deletedMeal)) {
                                viewModel.insertMeal(deletedMeal!!)
                                deletedMeal = null
                            }
                    }
                ).show()
            }
        }

        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.rvFavorites)
    }

    private fun prepareRecyclerView() {
        favoritesMealsAdapter = FavoritesMealsAdapter()
        binding.rvFavorites.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = favoritesMealsAdapter
        }
    }

    private fun observeFavorites() {
        viewModel.observeFavoritesMealsLiveData().observe(viewLifecycleOwner, Observer { meals ->
            favoritesMealsAdapter.differ.submitList(meals)
        })
    }

    private fun onMealClick() {
        favoritesMealsAdapter.onMealClick = { meals ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(HomeFragment.MEAL_ID, meals.idMeal)
            intent.putExtra(HomeFragment.MEAL_NAME, meals.strMeal)
            intent.putExtra(HomeFragment.MEAL_THUMB, meals.strMealThumb)
            startActivity(intent)
        }
    }
}