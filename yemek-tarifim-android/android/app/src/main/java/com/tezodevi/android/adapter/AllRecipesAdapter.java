package com.tezodevi.android.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.tezodevi.android.R;
import com.tezodevi.android.databinding.AllPostsRowLayoutBinding;
import com.tezodevi.android.model.ResponseRecipe;
import com.tezodevi.android.util.ImageHelper;
import com.tezodevi.android.util.SingletonRecipe;

import java.util.List;

public class AllRecipesAdapter extends RecyclerView.Adapter<AllRecipesAdapter.AllRecipesViewHolder> {

    private final List<ResponseRecipe> recipeList;

    public AllRecipesAdapter(List<ResponseRecipe> recipeList) {
        this.recipeList = recipeList;
    }

    @NonNull
    @Override
    public AllRecipesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AllPostsRowLayoutBinding binding = AllPostsRowLayoutBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );

        return new AllRecipesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AllRecipesViewHolder holder, int position) {
        holder.binding.allPostsRowLayoutRecipeImage.setImageBitmap(
                ImageHelper.toBitmap(recipeList.get(position).recipePhoto)
        );

        holder.binding.allPostsRowLayoutCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SingletonRecipe singletonRecipe = SingletonRecipe.getInstance();
                singletonRecipe.recipe = recipeList.get(position);

                Navigation.findNavController(holder.itemView).navigate(R.id.recipeDetailFragment);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public static class AllRecipesViewHolder extends RecyclerView.ViewHolder {
        private final AllPostsRowLayoutBinding binding;

        public AllRecipesViewHolder(AllPostsRowLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
