package com.tezodevi.android.view.user.fragment.friendposts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.tezodevi.android.R;
import com.tezodevi.android.adapter.FriendPostListAdapter;
import com.tezodevi.android.databinding.FragmentFriendPostsBinding;
import com.tezodevi.android.model.FriendPostModel;
import com.tezodevi.android.service.worker_services.RecipeService;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class FriendPostsFragment extends Fragment {
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private FragmentFriendPostsBinding binding;
    private RecipeService recipeService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recipeService = new RecipeService(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFriendPostsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        compositeDisposable.add(
                recipeService.getFriendsRecipes().subscribe(this::handleResponse, Throwable::printStackTrace)
        );

        binding.friendPostsFragmentOpenChattingScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.messageFragment);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
        binding = null;
    }

    private void handleResponse(List<FriendPostModel> friendPostModelList) {
        if (friendPostModelList == null || friendPostModelList.isEmpty()) {
            binding.friendPostsFragmentNoFriendPostHere.setVisibility(View.VISIBLE);
        } else {
            binding.friendPostsFragmentNoFriendPostHere.setVisibility(View.GONE);
            binding.friendPostsFragmentPostListRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            FriendPostListAdapter adapter = new FriendPostListAdapter(friendPostModelList);
            binding.friendPostsFragmentPostListRecyclerView.setAdapter(adapter);
        }
    }
}