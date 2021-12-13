package com.tezodevi.android.view.user.fragment.profile;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.tezodevi.android.R;
import com.tezodevi.android.adapter.AllRecipesAdapter;
import com.tezodevi.android.databinding.FragmentProfileBinding;
import com.tezodevi.android.model.ResponseRecipe;
import com.tezodevi.android.model.ResponseUser;
import com.tezodevi.android.service.worker_services.RecipeService;
import com.tezodevi.android.service.worker_services.UserService;
import com.tezodevi.android.util.ImageHelper;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class ProfileFragment extends Fragment {
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private FragmentProfileBinding binding;
    private RecipeService recipeService;
    private UserService userService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recipeService = new RecipeService(requireContext());
        userService = new UserService(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(
                inflater, container, false
        );

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.profileFragmentLoadingProgressBar.setVisibility(View.VISIBLE);
        binding.profileFragmentRecipeListRecyclerView.setVisibility(View.INVISIBLE);
        binding.profileFragmentNoRecipeHereText.setVisibility(View.GONE);

        compositeDisposable.add(
                userService.getMe().subscribe(this::handleUserResponse, Throwable::printStackTrace)
        );

        compositeDisposable.add(
                recipeService.getMyRecipes().subscribe(this::handleResponse, throwable -> {
                    binding.profileFragmentLoadingProgressBar.setVisibility(View.GONE);
                    binding.profileFragmentRecipeListRecyclerView.setVisibility(View.INVISIBLE);
                    binding.profileFragmentNoRecipeHereText.setVisibility(View.VISIBLE);

                    throwable.printStackTrace();
                })
        );

        binding.profileFragmentEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.editProfileFragment);
            }
        });

        binding.profileFragmentShareRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.postFragment);
            }
        });

        binding.profileFragmentSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.settingFragment);
            }
        });

        binding.profileFragmentFriendCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.friendListFragment);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
        binding = null;
    }

    private void handleResponse(List<ResponseRecipe> responseRecipes) {
        binding.profileFragmentLoadingProgressBar.setVisibility(View.GONE);

        if (responseRecipes == null || responseRecipes.size() == 0) {
            binding.profileFragmentNoRecipeHereText.setVisibility(View.VISIBLE);
            binding.profileFragmentRecipeListRecyclerView.setVisibility(View.INVISIBLE);

            return;
        }

        binding.profileFragmentNoRecipeHereText.setVisibility(View.GONE);
        binding.profileFragmentRecipeListRecyclerView.setVisibility(View.VISIBLE);

        binding.profileFragmentRecipeListRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        AllRecipesAdapter adapter = new AllRecipesAdapter(responseRecipes);
        binding.profileFragmentRecipeListRecyclerView.setAdapter(adapter);
    }

    private void handleUserResponse(ResponseUser responseUser) {
        Bitmap bitmap = ImageHelper.toBitmap(responseUser.profilePhoto);
        binding.profileFragmentUserImage.setImageBitmap(bitmap);

        if (responseUser.friends == null) {
            binding.profileFragmentFriendCount.setText(R.string.friends_n0);
        } else {
            binding.profileFragmentFriendCount.setText(getString(R.string.friends_formatted, String.valueOf(responseUser.friends.size())));
        }
    }
}