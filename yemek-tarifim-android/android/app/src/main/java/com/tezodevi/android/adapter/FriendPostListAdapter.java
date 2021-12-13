package com.tezodevi.android.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.tezodevi.android.R;
import com.tezodevi.android.databinding.FriendPostListLayoutBinding;
import com.tezodevi.android.model.FriendPostModel;
import com.tezodevi.android.util.ImageHelper;
import com.tezodevi.android.util.SingletonRecipe;

import java.util.List;

public class FriendPostListAdapter extends RecyclerView.Adapter<FriendPostListAdapter.FriendPostListViewHolder> {

    private final List<FriendPostModel> friendPostModelList;

    public FriendPostListAdapter(List<FriendPostModel> friendPostModelList) {
        this.friendPostModelList = friendPostModelList;
    }

    @NonNull
    @Override
    public FriendPostListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FriendPostListLayoutBinding binding = FriendPostListLayoutBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );

        return new FriendPostListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendPostListViewHolder holder, int position) {
        FriendPostModel friendPostModel = friendPostModelList.get(position);

        holder.binding.friendPostListLayoutUsernameText.setText(
                holder.itemView.getContext()
                        .getString(R.string.username_formatted_show, friendPostModel.username)
        );

        holder.binding.friendPostListLayoutRecipeNameText.setText(
                holder.itemView.getContext()
                        .getString(R.string.recipename_formatted_show, friendPostModel.recipe.recipeName)
        );

        holder.binding.friendPostListLayoutRecipePhoto.setImageBitmap(ImageHelper.toBitmap(friendPostModel.recipe.recipePhoto));

        holder.binding.friendPostListLayoutConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDetails(friendPostModel, holder);
            }
        });

    }

    private void showDetails(FriendPostModel friendPostModel, @NonNull FriendPostListViewHolder holder) {
        SingletonRecipe singletonRecipe = SingletonRecipe.getInstance();
        singletonRecipe.recipe = friendPostModel.recipe;

        Navigation.findNavController(holder.itemView).navigate(R.id.recipeDetailFragment);
    }

    @Override
    public int getItemCount() {
        return friendPostModelList.size();
    }

    public static class FriendPostListViewHolder extends RecyclerView.ViewHolder {
        private final FriendPostListLayoutBinding binding;

        public FriendPostListViewHolder(FriendPostListLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
