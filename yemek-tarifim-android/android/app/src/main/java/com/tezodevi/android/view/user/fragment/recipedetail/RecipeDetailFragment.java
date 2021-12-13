package com.tezodevi.android.view.user.fragment.recipedetail;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tezodevi.android.R;
import com.tezodevi.android.databinding.FragmentRecipeDetailBinding;
import com.tezodevi.android.model.ResponseRecipe;
import com.tezodevi.android.model.ResponseRecipeContent;
import com.tezodevi.android.service.worker_services.RecipeContentService;
import com.tezodevi.android.service.worker_services.UserService;
import com.tezodevi.android.util.ImageHelper;
import com.tezodevi.android.util.SingletonRecipe;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class RecipeDetailFragment extends Fragment {
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private FragmentRecipeDetailBinding binding;
    private RecipeContentService recipeContentService;
    private UserService userService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRecipeDetailBinding.inflate(
                inflater, container, false
        );

        return binding.getRoot();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userService = new UserService(requireContext());
        recipeContentService = new RecipeContentService(requireContext());

        SingletonRecipe recipe = SingletonRecipe.getInstance();
        ResponseRecipe responseRecipe = recipe.recipe;

        String userId = responseRecipe.user.id;
        compositeDisposable.add(
                userService.getUsername(userId).subscribe(this::handleUsername, Throwable::printStackTrace)
        );

        if (responseRecipe != null) {
            binding.fragmentRecipeDetailRecipePhoto.setImageBitmap(ImageHelper.toBitmap(responseRecipe.recipePhoto));
            binding.fragmentRecipeDetailRecipeNameText.setText(responseRecipe.recipeName);
            binding.fragmentRecipeDetailRecipeDetailText.setText(responseRecipe.recipeDetail);
            getRecipeContents(responseRecipe.id);
        } else {
            Toast.makeText(requireContext(), getString(R.string.error_no_recipe_detail), Toast.LENGTH_SHORT).show();
        }
    }

    private void handleUsername(String username) {
        binding.fragmentRecipeDetailUsernameText.setText(getString(R.string.username_formatted_show, username));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
        binding = null;
    }

    private void getRecipeContents(String recipeId) {
        compositeDisposable.add(
                recipeContentService.getRecipeContentsById(recipeId).subscribe(this::handleResponse, Throwable::printStackTrace)
        );
    }

    @SuppressLint("SetTextI18n")
    private void handleResponse(List<ResponseRecipeContent> responseRecipeContents) {
        SingletonRecipe recipe = SingletonRecipe.getInstance();
        ResponseRecipe responseRecipe = recipe.recipe;
        recipe.recipeContentList = responseRecipeContents;

        List<String> list = new ArrayList<>();
        for (ResponseRecipeContent temp : responseRecipeContents) {
            list.add(temp.recipeContentName);
        }

        if (responseRecipe != null) {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, list);
            binding.fragmentRecipeDetailRecipeContentsListView.setAdapter(arrayAdapter);

            TextView textView = new TextView(requireContext());
            textView.setText(R.string.recipe_contents);

            binding.fragmentRecipeDetailRecipeContentsListView.addHeaderView(textView);
        }
    }
}