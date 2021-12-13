package com.tezodevi.android.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.tezodevi.android.R;
import com.tezodevi.android.databinding.UserListRowBinding;
import com.tezodevi.android.model.RequestUser;
import com.tezodevi.android.model.ResponseUser;
import com.tezodevi.android.model.UserList;
import com.tezodevi.android.service.worker_services.UserService;
import com.tezodevi.android.util.ImageHelper;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class AddUserListAdapter extends RecyclerView.Adapter<AddUserListAdapter.AddUserListHolder> {
    private final List<UserList> mUserList;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final UserService mUserService;
    private ResponseUser me;

    public AddUserListAdapter(List<UserList> mUserList, UserService userService) {
        this.mUserList = mUserList;
        mUserService = userService;

        compositeDisposable.add(
                mUserService.getMe().subscribe(responseUser -> {
                    this.me = responseUser;
                }, Throwable::printStackTrace)
        );
    }

    @NonNull
    @Override
    public AddUserListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        UserListRowBinding binding = UserListRowBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );

        return new AddUserListHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AddUserListHolder holder, int position) {
        UserList userListItem = mUserList.get(position);

        holder.binding.userListRowUsernameText.setText(userListItem.username);
        holder.binding.userListRowUserPhotoImageView.setImageBitmap(ImageHelper.toBitmap(userListItem.profilePhoto));

        holder.binding.userListRowAddFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compositeDisposable.add(
                        mUserService.addFriend(new RequestUser(userListItem.id)).subscribe(responseUser -> {

                            if (!responseUser.id.equals(me.id)) {
                                Snackbar.make(holder.itemView,
                                        R.string.added_new_friend,
                                        Snackbar.LENGTH_LONG).show();
                            } else {
                                Snackbar.make(holder.itemView,
                                        R.string.cannot_add_yourself_or_sameuser,
                                        Snackbar.LENGTH_LONG).show();
                            }

                        }, Throwable::printStackTrace)
                );
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }

    public static class AddUserListHolder extends RecyclerView.ViewHolder {
        private final UserListRowBinding binding;

        public AddUserListHolder(@NonNull UserListRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
