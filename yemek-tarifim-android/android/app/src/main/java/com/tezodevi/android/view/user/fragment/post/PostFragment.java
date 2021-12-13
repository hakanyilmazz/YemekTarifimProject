package com.tezodevi.android.view.user.fragment.post;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.snackbar.Snackbar;
import com.tezodevi.android.R;
import com.tezodevi.android.databinding.PostFragmentBinding;
import com.tezodevi.android.model.RecipeContent;
import com.tezodevi.android.model.RequestRecipe;
import com.tezodevi.android.service.worker_services.RecipeContentService;
import com.tezodevi.android.service.worker_services.RecipeService;
import com.tezodevi.android.util.ImageHelper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;

public class PostFragment extends Fragment {
    private PostFragmentBinding binding;
    private RecipeService recipeService;
    private RecipeContentService recipeContentService;

    private ActivityResultLauncher<Intent> intentLauncher;
    private ActivityResultLauncher<String> permissionLauncher;

    private CompositeDisposable compositeDisposable;
    private Uri imageData;

    private List<EditText> recipeContentList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerLaunchers();
        compositeDisposable = new CompositeDisposable();
        recipeContentList = new ArrayList<>();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = PostFragmentBinding.inflate(
                inflater, container, false
        );

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recipeService = new RecipeService(requireContext());
        recipeContentService = new RecipeContentService(requireContext());

        binding.postFragmentAddRecipeContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText myEditText = new EditText(requireContext()); // Pass it an Activity or Context
                myEditText.setHint(R.string.add_recipe_content);
                myEditText.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.

                if (recipeContentList.size() >= 1 && !recipeContentList.get(recipeContentList.size() - 1).getText().toString().trim().isEmpty()) {
                    recipeContentList.add(myEditText);
                    binding.linearLayout.addView(myEditText);

                } else if (recipeContentList.size() == 0) {
                    recipeContentList.add(myEditText);
                    binding.linearLayout.addView(myEditText);

                } else {
                    Snackbar.make(view, getString(R.string.please_enter_recipe_content), Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        binding.postFragmentRecipeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        Snackbar.make(v, getString(R.string.permission_required_for_gallery), Snackbar.LENGTH_INDEFINITE)
                                .setAction(getString(R.string.give_permission), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                                    }
                                })
                                .show();
                    } else {
                        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                    }
                } else {
                    Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intentLauncher.launch(intentToGallery);
                }
            }
        });

        binding.postFragmentFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.postFragmentFloatingActionButton.setEnabled(false);
                binding.postFragmentProgressBar.setVisibility(View.VISIBLE);
                binding.postFragmentAddRecipeContent.setEnabled(false);

                if (imageData == null) {
                    Snackbar.make(v, R.string.please_select_a_photo, Snackbar.LENGTH_SHORT).show();
                    binding.postFragmentFloatingActionButton.setEnabled(true);
                    binding.postFragmentProgressBar.setVisibility(View.GONE);
                    binding.postFragmentAddRecipeContent.setEnabled(true);
                    return;
                }

                RequestRecipe recipe = new RequestRecipe(
                        ImageHelper.toByteArray(requireActivity(), imageData),
                        binding.postFragmentRecipeNameInput.getText().toString(),
                        binding.postFragmentRecipeDetail.getText().toString()
                );

                Single<String> responseRecipeSingle = recipeService.postRecipe(recipe);

                if (responseRecipeSingle != null) {
                    compositeDisposable.add(
                            responseRecipeSingle
                                    .subscribe(this::handleResponse, throwable -> {
                                        binding.postFragmentFloatingActionButton.setEnabled(true);
                                        binding.postFragmentProgressBar.setVisibility(View.GONE);
                                        binding.postFragmentAddRecipeContent.setEnabled(true);
                                        throwable.printStackTrace();
                                    }));
                } else {
                    Snackbar.make(v, R.string.post_fragment_fill_inputs_message, Snackbar.LENGTH_SHORT).show();
                    binding.postFragmentFloatingActionButton.setEnabled(true);
                    binding.postFragmentProgressBar.setVisibility(View.GONE);
                    binding.postFragmentAddRecipeContent.setEnabled(true);
                }

            }

            private void handleResponse(String recipeId) {
                List<RecipeContent> tempRecipeContentList = new ArrayList<>();

                for (EditText editText : recipeContentList) {
                    tempRecipeContentList.add(new RecipeContent(editText.getText().toString(), new RequestRecipe(recipeId)));
                }

                Single<Boolean> responseRecipeContentSingle = recipeContentService.postRecipeContent(tempRecipeContentList);

                if (responseRecipeContentSingle != null) {
                    compositeDisposable.add(
                            responseRecipeContentSingle
                                    .subscribe(this::handleRecipeContentResponse, throwable -> {
                                        binding.postFragmentFloatingActionButton.setEnabled(true);
                                        binding.postFragmentProgressBar.setVisibility(View.GONE);
                                        binding.postFragmentAddRecipeContent.setEnabled(true);
                                        throwable.printStackTrace();
                                    })
                    );
                } else {
                    Snackbar.make(binding.getRoot(), R.string.post_fragment_fill_inputs_message, Snackbar.LENGTH_SHORT).show();
                    binding.postFragmentFloatingActionButton.setEnabled(true);
                    binding.postFragmentProgressBar.setVisibility(View.GONE);
                    binding.postFragmentAddRecipeContent.setEnabled(true);
                }
            }

            private void handleRecipeContentResponse(Boolean nothing) {
                Navigation.findNavController(view).popBackStack();
                Navigation.findNavController(view).navigate(R.id.profileFragment);
            }
        });
    }

    private void registerLaunchers() {
        intentLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();

                    if (data != null) {
                        final Uri imageData = data.getData();

                        if (imageData != null) {
                            PostFragment.this.imageData = imageData;
                            binding.postFragmentRecipeImage.setImageURI(imageData);
                        }
                    }
                }
            }
        });

        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if (result) {
                    Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intentLauncher.launch(intentToGallery);
                } else {
                    Toast.makeText(requireContext(), getString(R.string.permission_required), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
        binding = null;
    }
}