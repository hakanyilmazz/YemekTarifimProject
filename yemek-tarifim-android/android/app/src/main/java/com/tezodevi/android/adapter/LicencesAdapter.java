package com.tezodevi.android.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tezodevi.android.databinding.LicencesRowLayoutBinding;

import java.util.List;

public class LicencesAdapter extends RecyclerView.Adapter<LicencesAdapter.LicencesViewHolder> {

    private final List<String> licenceList;

    public LicencesAdapter(List<String> licenceList) {
        this.licenceList = licenceList;
    }

    @NonNull
    @Override
    public LicencesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LicencesRowLayoutBinding binding = LicencesRowLayoutBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );

        return new LicencesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LicencesViewHolder holder, int position) {
        holder.binding.licencesRowLayoutLicenceInformation.setText(licenceList.get(position));
    }

    @Override
    public int getItemCount() {
        return licenceList.size();
    }

    public static class LicencesViewHolder extends RecyclerView.ViewHolder {
        private final LicencesRowLayoutBinding binding;

        public LicencesViewHolder(LicencesRowLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
