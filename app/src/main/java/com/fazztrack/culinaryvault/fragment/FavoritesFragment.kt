package com.fazztrack.culinaryvault.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.fazztrack.culinaryvault.R
import com.fazztrack.culinaryvault.activity.MainActivity
import com.fazztrack.culinaryvault.adapter.FavoritesMealsAdapter
import com.fazztrack.culinaryvault.databinding.FragmentFavoritesBinding
import com.fazztrack.culinaryvault.viewmodel.HomeViewModel

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeFavorites()

        prepareRecyclerView()
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
}