package com.tezodevi.android.view.user.fragment.friendlist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.tezodevi.android.adapter.FriendListAdapter;
import com.tezodevi.android.databinding.FragmentFriendListBinding;
import com.tezodevi.android.model.UserList;
import com.tezodevi.android.service.worker_services.UserService;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class FriendListFragment extends Fragment {

    private FragmentFriendListBinding binding;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private UserService userService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userService = new UserService(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFriendListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        compositeDisposable.add(
                userService.getFriendList().subscribe(this::handleResponse, Throwable::printStackTrace)
        );
    }

    private void handleResponse(List<UserList> userLists) {
        binding.friendListFragmentFriendList.setLayoutManager(new LinearLayoutManager(requireContext()));
        FriendListAdapter adapter = new FriendListAdapter(userLists);
        binding.friendListFragmentFriendList.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
        binding = null;
    }
}