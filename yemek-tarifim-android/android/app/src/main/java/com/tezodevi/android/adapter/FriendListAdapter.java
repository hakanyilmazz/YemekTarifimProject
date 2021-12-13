package com.tezodevi.android.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tezodevi.android.databinding.FriendListLayoutBinding;
import com.tezodevi.android.model.RequestUser;
import com.tezodevi.android.model.ResponseUser;
import com.tezodevi.android.model.UserList;
import com.tezodevi.android.service.worker_services.UserService;
import com.tezodevi.android.util.ImageHelper;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.FriendListViewHolder> {

    private final List<UserList> userList;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private UserService userService;

    public FriendListAdapter(List<UserList> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public FriendListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FriendListLayoutBinding binding = FriendListLayoutBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );

        return new FriendListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendListViewHolder holder, int position) {
        UserList model = userList.get(position);

        holder.binding.friendListLayoutProfilePhoto.setImageBitmap(ImageHelper.toBitmap(model.profilePhoto));
        holder.binding.friendListLayoutFriendUsername.setText(model.username);
        holder.binding.friendListLayoutRemoveFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userService = new UserService(holder.binding.getRoot().getContext());

                compositeDisposable.add(
                        userService.removeFriend(new RequestUser(model.id)).subscribe(this::handleResponse, Throwable::printStackTrace)
                );
            }

            private void handleResponse(ResponseUser responseUser) {
                userList.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class FriendListViewHolder extends RecyclerView.ViewHolder {
        private final FriendListLayoutBinding binding;

        public FriendListViewHolder(FriendListLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}