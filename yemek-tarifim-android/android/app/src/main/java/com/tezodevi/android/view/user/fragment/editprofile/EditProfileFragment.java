package com.tezodevi.android.view.user.fragment.editprofile;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.tezodevi.android.databinding.FragmentEditProfileBinding;
import com.tezodevi.android.model.Country;
import com.tezodevi.android.model.Login;
import com.tezodevi.android.model.RequestUser;
import com.tezodevi.android.model.ResponseUser;
import com.tezodevi.android.service.worker_services.CountryService;
import com.tezodevi.android.service.worker_services.LoginService;
import com.tezodevi.android.service.worker_services.UserService;
import com.tezodevi.android.util.ImageHelper;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class EditProfileFragment extends Fragment {
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private FragmentEditProfileBinding binding;
    private UserService userService;
    private CountryService countryService;
    private LoginService loginService;
    private String selectedCountryId = "";
    private ActivityResultLauncher<Intent> intentLauncher;
    private ActivityResultLauncher<String> permissionLauncher;
    private Uri imageData;
    private Country myCountry;
    private ResponseUser responseUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginService = new LoginService(requireContext());
        userService = new UserService(requireContext());
        countryService = new CountryService();

        registerLaunchers();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.editProfileFragmentEditButton.setEnabled(false);

        compositeDisposable.add(
                userService.getMe().subscribe(responseUser -> {
                    handleUser(responseUser);
                    compositeDisposable.add(
                            countryService.getAll().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(this::handleCountries, Throwable::printStackTrace)
                    );

                    binding.editProfileFragmentEditButton.setEnabled(true);
                }, Throwable::printStackTrace)
        );

        compositeDisposable.add(
                loginService.getMe().subscribe(this::handleLogin, Throwable::printStackTrace)
        );

        binding.fragmentEditProfileProfilePhoto.setOnClickListener(new View.OnClickListener() {
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

        binding.fragmentEditProfileProfilePhoto.setOnClickListener(new View.OnClickListener() {
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

        binding.editProfileFragmentEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.editProfileFragmentEditButton.setEnabled(false);
                Toast.makeText(getContext(), getString(R.string.update_message), Toast.LENGTH_SHORT).show();

                compositeDisposable.add(
                        userService.updateUser(new RequestUser(
                                binding.fragmentEditProfileNameText.getText().toString(),
                                binding.fragmentEditProfileSurnameText.getText().toString(),
                                Integer.parseInt(binding.fragmentEditProfileAgeText.getText().toString()),
                                new Country(selectedCountryId),
                                true
                        )).subscribe(responseUser -> {
                            Single<Login> loginSingle = loginService.updateLogin(
                                    new Login(
                                            binding.fragmentEditProfileUsernameText.getText().toString(),
                                            binding.fragmentEditProfilePasswordText.getText().toString(),
                                            binding.fragmentEditProfileEmailText.getText().toString()
                                    )
                            );

                            if (loginSingle != null) {
                                compositeDisposable.add(
                                        loginSingle.subscribe(login -> {
                                            compositeDisposable.add(
                                                    loginService.getTokenFromDb(login.username, login.password).subscribe(jwt -> {
                                                        loginService.saveLoginInfo(login.username, login.password);
                                                        loginService.saveToken(jwt.jwt);

                                                        Navigation.findNavController(v).navigate(R.id.profileFragment);
                                                    }, Throwable::printStackTrace)
                                            );
                                        }, Throwable::printStackTrace)
                                );
                            } else {
                                Toast.makeText(requireContext(), getString(R.string.title_text_invalid_register_inputs), Toast.LENGTH_SHORT).show();
                            }
                        }, Throwable::printStackTrace)
                );

            }
        });
    }

    private void handleLogin(Login login) {
        binding.fragmentEditProfileEmailText.setText(login.email);
        binding.fragmentEditProfileUsernameText.setText(login.username);
        binding.fragmentEditProfilePasswordText.setText(login.password);
    }

    private void handleCountries(List<Country> countries) {
        String[] items = new String[countries.size()];

        int i = 0;
        for (Country country : countries) {
            items[i] = country.countryName;

            ++i;
        }

        if (myCountry == null) {
            myCountry = countries.get(0);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, items);
        binding.fragmentEditProfileCountrySpinnerLayout.setAdapter(adapter);
        binding.fragmentEditProfileCountrySpinnerLayout.setSelected(true);
        binding.fragmentEditProfileCountrySpinnerLayout.setSelection(adapter.getPosition(myCountry.countryName));
        binding.fragmentEditProfileCountrySpinnerLayout.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCountryId = countries.get(position).id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCountryId = countries.get(0).id;
            }
        });
    }

    private void handleUser(ResponseUser responseUser) {
        binding.fragmentEditProfileProfilePhoto.setImageBitmap(ImageHelper.toBitmap(responseUser.profilePhoto));
        binding.fragmentEditProfileNameText.setText(responseUser.name);
        binding.fragmentEditProfileSurnameText.setText(responseUser.surname);
        binding.fragmentEditProfileAgeText.setText(String.valueOf(responseUser.age));
        myCountry = responseUser.country;
        this.responseUser = responseUser;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
        binding = null;
    }

    private void registerLaunchers() {
        intentLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();

                    if (data != null) {
                        EditProfileFragment.this.imageData = data.getData();

                        if (imageData != null) {
                            Toast.makeText(requireContext(), getString(R.string.image_loading_please_wait), Toast.LENGTH_SHORT).show();

                            byte[] image = ImageHelper.toByteArray(requireContext(), imageData);
                            compositeDisposable.add(
                                    userService.changePhoto(new RequestUser(image))
                                            .subscribe(responseUser -> {
                                                Toast.makeText(requireContext(), getString(R.string.image_changing_successfully),
                                                        Toast.LENGTH_SHORT).show();

                                                binding.fragmentEditProfileProfilePhoto.setImageBitmap(ImageHelper.toBitmap(responseUser.profilePhoto));
                                            }, throwable -> {
                                                Toast.makeText(requireContext(), getString(R.string.image_changing_failed), Toast.LENGTH_SHORT).show();
                                                throwable.printStackTrace();
                                            })
                            );
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
}