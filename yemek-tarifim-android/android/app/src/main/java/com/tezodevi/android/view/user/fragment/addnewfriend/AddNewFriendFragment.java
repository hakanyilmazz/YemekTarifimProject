package com.tezodevi.android.view.user.fragment.addnewfriend;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.tezodevi.android.adapter.AddUserListAdapter;
import com.tezodevi.android.databinding.FragmentAddNewFriendBinding;
import com.tezodevi.android.model.UserList;
import com.tezodevi.android.service.worker_services.UserService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class AddNewFriendFragment extends Fragment {
    private FragmentAddNewFriendBinding binding;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private UserService userService;
    private List<UserList> userListItems = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userService = new UserService(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddNewFriendBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        compositeDisposable.add(
                userService.getAll().subscribe(this::handleUsers, Throwable::printStackTrace)
        );

        binding.fragmentAddNewFriendUserSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchUsername(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchUsername(newText);
                return true;
            }
        });
    }

    private void searchUsername(String query) {
        binding.fragmentAddNewFriendUserListRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        query = query.trim();
        AddUserListAdapter adapter;

        if (query.isEmpty()) {
            adapter = new AddUserListAdapter(userListItems, userService);
            binding.fragmentAddNewFriendUserListRecyclerView.setAdapter(adapter);
            return;
        }

        final List<UserList> list = new ArrayList<>();

        for (UserList temp : userListItems) {
            if (temp.username.toLowerCase().contains(query.toLowerCase())) {
                list.add(temp);
            }
        }

        adapter = new AddUserListAdapter(list, userService);
        binding.fragmentAddNewFriendUserListRecyclerView.setAdapter(adapter);
    }

    private void handleUsers(List<UserList> responseUsers) {
        binding.fragmentAddNewFriendUserListRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        AddUserListAdapter adapter = new AddUserListAdapter(responseUsers, userService);
        binding.fragmentAddNewFriendUserListRecyclerView.setAdapter(adapter);
        this.userListItems = responseUsers;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
        binding = null;
    }
}