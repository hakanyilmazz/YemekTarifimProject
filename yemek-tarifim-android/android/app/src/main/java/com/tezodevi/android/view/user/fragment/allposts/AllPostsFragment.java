package com.tezodevi.android.view.user.fragment.allposts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.tezodevi.android.R;
import com.tezodevi.android.adapter.AllRecipesAdapter;
import com.tezodevi.android.databinding.AllPostsFragmentBinding;
import com.tezodevi.android.model.ResponseRecipe;
import com.tezodevi.android.service.worker_services.RecipeService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class AllPostsFragment extends Fragment {
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private AllPostsFragmentBinding binding;
    private RecipeService recipeService;
    private List<ResponseRecipe> responseRecipeList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recipeService = new RecipeService(requireContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = AllPostsFragmentBinding.inflate(
                inflater, container, false
        );

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setDefaultUI();

        binding.allPostsFragmentSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchRecipeName(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchRecipeName(newText);
                return true;
            }
        });

        compositeDisposable.add(
                recipeService.getAllRecipes().subscribe(this::handleResponse, throwable -> {
                    binding.allPostsFragmentProgressBar.setVisibility(View.GONE);
                    binding.allPostsFragmentNoRecipeHere.setVisibility(View.VISIBLE);
                    throwable.printStackTrace();
                })
        );

        binding.allPostsFragmentAddNewFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.addNewFriendFragment);
            }
        });
    }

    private void setDefaultUI() {
        binding.allPostsFragmentRecipeListRecyclerView.setVisibility(View.GONE);
        binding.allPostsFragmentNoRecipeHere.setVisibility(View.GONE);
        binding.allPostsFragmentProgressBar.setVisibility(View.VISIBLE);
    }

    private void searchRecipeName(String queryRecipeName) {
        binding.allPostsFragmentRecipeListRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));

        queryRecipeName = queryRecipeName.trim();
        AllRecipesAdapter allRecipesAdapter;

        if (queryRecipeName.isEmpty()) {
            allRecipesAdapter = new AllRecipesAdapter(responseRecipeList);
            binding.allPostsFragmentRecipeListRecyclerView.setAdapter(allRecipesAdapter);
            return;
        }

        final List<ResponseRecipe> list = new ArrayList<>();

        for (ResponseRecipe temp : responseRecipeList) {
            if (temp.recipeName.toLowerCase().contains(queryRecipeName.toLowerCase())) {
                list.add(temp);
            }
        }

        allRecipesAdapter = new AllRecipesAdapter(list);
        binding.allPostsFragmentRecipeListRecyclerView.setAdapter(allRecipesAdapter);
    }

    private void handleResponse(List<ResponseRecipe> responseRecipes) {
        if (responseRecipes == null || responseRecipes.isEmpty()) {
            binding.allPostsFragmentRecipeListRecyclerView.setVisibility(View.GONE);
            binding.allPostsFragmentNoRecipeHere.setVisibility(View.VISIBLE);
            binding.allPostsFragmentProgressBar.setVisibility(View.GONE);
        } else {
            binding.allPostsFragmentRecipeListRecyclerView.setVisibility(View.VISIBLE);
            binding.allPostsFragmentNoRecipeHere.setVisibility(View.GONE);
            binding.allPostsFragmentProgressBar.setVisibility(View.GONE);

            binding.allPostsFragmentRecipeListRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
            AllRecipesAdapter allRecipesAdapter = new AllRecipesAdapter(responseRecipes);
            binding.allPostsFragmentRecipeListRecyclerView.setAdapter(allRecipesAdapter);

            this.responseRecipeList = responseRecipes;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
        binding = null;
    }
}