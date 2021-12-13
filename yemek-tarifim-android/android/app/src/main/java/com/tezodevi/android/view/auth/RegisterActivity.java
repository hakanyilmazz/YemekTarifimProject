package com.tezodevi.android.view.auth;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.tezodevi.android.R;
import com.tezodevi.android.databinding.ActivityRegisterBinding;
import com.tezodevi.android.model.Country;
import com.tezodevi.android.model.JWT;
import com.tezodevi.android.model.Login;
import com.tezodevi.android.model.LoginUser;
import com.tezodevi.android.model.RequestUser;
import com.tezodevi.android.service.worker_services.CountryService;
import com.tezodevi.android.service.worker_services.LoginService;
import com.tezodevi.android.util.ImageHelper;
import com.tezodevi.android.util.ViewHelper;
import com.tezodevi.android.view.user.UserActivity;

import java.util.List;
import java.util.Objects;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class RegisterActivity extends AppCompatActivity {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ActivityRegisterBinding binding;
    private String selectedCountryId = "";
    private LoginService loginService;
    private ActivityResultLauncher<Intent> intentLauncher;
    private ActivityResultLauncher<String> permissionLauncher;
    private Uri imageData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        registerLaunchers();

        CountryService countryService = new CountryService();
        loginService = new LoginService(this);

        compositeDisposable.add(
                countryService.getAll()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::handleResponse, Throwable::printStackTrace)
        );

        binding.registerActivityProfileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
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

        binding.registerActivityRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageData == null) {
                    Snackbar.make(v, R.string.please_select_a_photo, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                binding.registerActivityRegisterButton.setEnabled(false);

                binding.registerActivityProgressBar.setVisibility(View.VISIBLE);

                String age = Objects.requireNonNull(binding.registerActivityAgeEditText.getText()).toString();
                byte[] binaryImage = ImageHelper.toByteArray(RegisterActivity.this, imageData);

                if (age.isEmpty() || Integer.parseInt(age) > 150 || Integer.parseInt(age) <= 0) {
                    Snackbar.make(v, getString(R.string.title_text_invalid_register_inputs), Snackbar.LENGTH_SHORT).show();
                    binding.registerActivityProgressBar.setVisibility(View.GONE);
                    binding.registerActivityRegisterButton.setEnabled(true);
                    return;
                }

                Login login = new Login(
                        Objects.requireNonNull(binding.registerActivityUsernameEditText.getText()).toString(),
                        Objects.requireNonNull(binding.registerActivityPasswordEditText.getText()).toString(),
                        Objects.requireNonNull(binding.registerActivityEmailEditText.getText()).toString()
                );

                RequestUser user = new RequestUser(
                        Objects.requireNonNull(binding.registerActivityNameEditText.getText()).toString(),
                        Objects.requireNonNull(binding.registerActivitySurnameEditText.getText()).toString(),
                        Integer.parseInt(age),
                        new Country(selectedCountryId),
                        true,
                        binaryImage
                );

                Single<JWT> register = loginService.register(new LoginUser(login, user));

                if (register != null) {
                    compositeDisposable.add(
                            register.subscribe(this::handleToken, throwable -> {
                                throwable.printStackTrace();
                                binding.registerActivityProgressBar.setVisibility(View.GONE);
                                binding.registerActivityRegisterButton.setEnabled(true);
                            })
                    );
                } else {
                    Snackbar.make(v, getString(R.string.title_text_invalid_register_inputs), Snackbar.LENGTH_SHORT).show();
                    binding.registerActivityProgressBar.setVisibility(View.GONE);
                    binding.registerActivityRegisterButton.setEnabled(true);
                }
            }

            private void handleToken(JWT jwt) {
                String token = jwt.jwt;

                if (!token.isEmpty()) {
                    loginService.saveToken(token);

                    loginService.saveLoginInfo(
                            Objects.requireNonNull(binding.registerActivityUsernameEditText.getText())
                                    .toString().trim(),
                            Objects.requireNonNull(binding.registerActivityPasswordEditText.getText())
                                    .toString().trim()
                    );

                    ViewHelper.createIntentClear(RegisterActivity.this, UserActivity.class);
                    binding.registerActivityProgressBar.setVisibility(View.GONE);
                    binding.registerActivityRegisterButton.setEnabled(false);
                }
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
                            RegisterActivity.this.imageData = imageData;
                            binding.registerActivityProfileImageView.setImageURI(imageData);
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
                    Toast.makeText(RegisterActivity.this, getString(R.string.permission_required), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void handleResponse(List<Country> countries) {
        String[] items = new String[countries.size()];

        int i = 0;
        for (Country country : countries) {
            items[i] = country.countryName;
            ++i;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        binding.registerActivityCountrySpinnerLayout.setAdapter(adapter);
        binding.registerActivityCountrySpinnerLayout.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
        binding = null;
    }
}