package com.tezodevi.android.view.user.fragment.competition;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.tezodevi.android.R;
import com.tezodevi.android.databinding.FragmentStartedCompetitionBinding;
import com.tezodevi.android.model.ResponseRecipe;
import com.tezodevi.android.model.ScoreFragmentSingletonData;
import com.tezodevi.android.model.ScoreSingleton;
import com.tezodevi.android.service.worker_services.ScoreService;
import com.tezodevi.android.util.CompetitionHelper;
import com.tezodevi.android.util.ImageHelper;

import java.util.List;
import java.util.Random;

import io.reactivex.disposables.CompositeDisposable;

public class StartedCompetitionFragment extends Fragment {
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final Random random = new Random();
    private final int index = 0;
    private FragmentStartedCompetitionBinding binding;
    private ScoreService scoreService;
    private String answer = "";
    private List<ResponseRecipe> recipeList;
    private int score = 0;
    private final boolean isFirstStarting = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scoreService = new ScoreService(requireContext());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
        binding = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStartedCompetitionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        compositeDisposable.add(
                scoreService.getRecipeListForCompetition().subscribe(this::handleResponse, throwable -> {
                    Toast.makeText(requireContext(), getString(R.string.recipe_list_lower_than_five), Toast.LENGTH_SHORT).show();
                })
        );

        binding.startedCompetitionFragmentFinishCompetitionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScoreSingleton scoreSingleton = ScoreSingleton.getInstance();
                scoreSingleton.score = score;

                ScoreFragmentSingletonData data = ScoreFragmentSingletonData.getInstance();
                data.data = "new";

                Navigation.findNavController(v).popBackStack();
                Navigation.findNavController(v).navigate(R.id.scoreFragment);
            }
        });
    }

    private void handleResponse(List<ResponseRecipe> responseRecipes) {
        this.recipeList = responseRecipes;
        setCompetition();
    }

    private void setCompetition() {
        getCompetition();

        binding.button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (answer.equals(binding.button3.getText().toString())) {
                    score++;
                    binding.button3.setBackgroundColor(Color.GREEN);
                    binding.startedCompetitionFragmentQuestionCounter.setText(String.valueOf(score));
                } else {
                    binding.button3.setBackgroundColor(Color.RED);
                }

                getCompetition();
            }
        });

        binding.button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (answer.equals(binding.button4.getText().toString())) {
                    score++;
                    binding.startedCompetitionFragmentQuestionCounter.setText(String.valueOf(score));
                    binding.button4.setBackgroundColor(Color.GREEN);
                } else {
                    binding.button4.setBackgroundColor(Color.RED);
                }

                getCompetition();
            }
        });

        binding.button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (answer.equals(binding.button5.getText().toString())) {
                    score++;
                    binding.button5.setBackgroundColor(Color.GREEN);
                    binding.startedCompetitionFragmentQuestionCounter.setText(String.valueOf(score));
                } else {
                    binding.button5.setBackgroundColor(Color.RED);
                }

                getCompetition();
            }
        });
    }

    public void getCompetition() {
        binding.button3.setEnabled(false);
        binding.button4.setEnabled(false);
        binding.button5.setEnabled(false);

        new CountDownTimer(1200, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                binding.button3.setEnabled(true);
                binding.button4.setEnabled(true);
                binding.button5.setEnabled(true);

                binding.button3.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.teal_700));
                binding.button4.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.teal_700));
                binding.button5.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.teal_700));

                List<ResponseRecipe> recipeQuestions = CompetitionHelper.getCompetitionWithAnswers(recipeList);

                if (recipeQuestions == null || recipeQuestions.isEmpty()) {
                    return;
                }

                int answerIndex = random.nextInt(recipeQuestions.size());

                binding.startedCompetitionFragmentRecipePhoto.setImageBitmap(ImageHelper.toBitmap(recipeQuestions.get(answerIndex).recipePhoto));

                ResponseRecipe recipeAnswer = recipeQuestions.get(answerIndex);
                answer = recipeAnswer.recipeName;

                setButtonText(answerIndex, recipeAnswer);

                int otherIndex = 0;
                for (int i = 0; i < recipeQuestions.size(); i++) {
                    if (i != answerIndex) {
                        otherIndex = i;
                        setButtonText(i, recipeQuestions.get(i));
                        break;
                    }
                }

                for (int i = 0; i < recipeQuestions.size(); i++) {
                    if (i != answerIndex && i != otherIndex) {
                        setButtonText(i, recipeQuestions.get(i));
                        break;
                    }
                }
            }
        }.start();
    }

    private void setButtonText(int i, ResponseRecipe recipe) {
        if (i == 0) {
            binding.button3.setText(recipe.recipeName);
        } else if (i == 1) {
            binding.button4.setText(recipe.recipeName);
        } else {
            binding.button5.setText(recipe.recipeName);
        }
    }
}