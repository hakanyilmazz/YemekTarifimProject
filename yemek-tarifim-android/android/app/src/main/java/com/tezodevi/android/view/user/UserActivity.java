package com.tezodevi.android.view.user;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tezodevi.android.R;
import com.tezodevi.android.databinding.ActivityUserBinding;

public class UserActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private ActivityUserBinding binding;
    private int lastFragmentId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        startBottomNavigationView();
    }

    private void startBottomNavigationView() {
        binding.userActivityBottomNavigationView.setOnNavigationItemSelectedListener(this);
        binding.userActivityBottomNavigationView.setSelectedItemId(R.id.friendPostsFragment);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        lastFragmentId = item.getItemId();
        Navigation.findNavController(binding.userActivityFragmentContainerView).popBackStack();

        switch (item.getItemId()) {
            case R.id.userAppBottomNav_friendPosts:
                Navigation.findNavController(binding.userActivityFragmentContainerView).navigate(R.id.friendPostsFragment);
                break;

            case R.id.userAppBottomNav_allPosts:
                Navigation.findNavController(binding.userActivityFragmentContainerView).navigate(R.id.allPostsFragment);
                break;

            case R.id.userAppBottomNav_competition:
                Navigation.findNavController(binding.userActivityFragmentContainerView).navigate(R.id.competitionFragment);
                break;

            case R.id.userAppBottomNav_profile:
                Navigation.findNavController(binding.userActivityFragmentContainerView).navigate(R.id.profileFragment);
                break;

            default:
                lastFragmentId = 0;
                break;
        }

        return lastFragmentId != 0;
    }

}