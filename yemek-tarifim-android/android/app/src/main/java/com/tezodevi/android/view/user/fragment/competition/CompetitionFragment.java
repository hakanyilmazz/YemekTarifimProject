package com.tezodevi.android.view.user.fragment.competition;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.tezodevi.android.R;
import com.tezodevi.android.databinding.CompetitionFragmentBinding;
import com.tezodevi.android.model.ScoreFragmentSingletonData;

public class CompetitionFragment extends Fragment {

    private CompetitionFragmentBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = CompetitionFragmentBinding.inflate(
                inflater, container, false
        );

        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.showScoresList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScoreFragmentSingletonData data = ScoreFragmentSingletonData.getInstance();
                data.data = "old";
                Navigation.findNavController(v).navigate(R.id.scoreFragment);
            }
        });

        binding.startCompetitionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.startedCompetitionFragment);
            }
        });
    }
}