package com.tezodevi.android.view.user.fragment.setting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.tezodevi.android.R;
import com.tezodevi.android.databinding.SettingFragmentBinding;
import com.tezodevi.android.model.Login;
import com.tezodevi.android.model.Role;
import com.tezodevi.android.service.worker_services.LoginService;
import com.tezodevi.android.util.ViewHelper;
import com.tezodevi.android.view.LicencesActivity;

import java.util.Set;

import io.reactivex.disposables.CompositeDisposable;

public class SettingFragment extends Fragment {
    private SettingFragmentBinding binding;
    private LoginService loginService;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginService = new LoginService(requireContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = SettingFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        compositeDisposable.add(
                loginService.getMe().subscribe(this::handleResponse, Throwable::printStackTrace)
        );

        binding.settingFragmentSignOutButton.setOnClickListener(v -> {
            MaterialAlertDialogBuilder alert = new MaterialAlertDialogBuilder(requireContext());
            alert.setTitle(R.string.are_you_sure);
            alert.setMessage(R.string.signing_out);
            alert.setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                loginService.signOut();
                ViewHelper.triggerRebirth(requireContext());
            });

            alert.setNegativeButton(getString(R.string.no), (dialog, which) -> {
            });

            alert.show();
        });

        binding.settingFragmentLicenceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewHelper.createIntent(requireActivity(), LicencesActivity.class, false);
            }
        });
    }

    private void handleResponse(Login login) {
        Set<Role> myRoles = login.userRoleList;

        if (myRoles == null || myRoles.isEmpty()) {
            binding.settingFragmentEditRolesButton.setVisibility(View.GONE);
        } else {

            for (Role role : myRoles) {
                if (role.roleName.equals("ADMIN")) {
                    binding.settingFragmentEditRolesButton.setVisibility(View.VISIBLE);
                    break;
                } else if (role.roleName.equals("MODERATOR")) {
                    binding.settingFragmentEditRolesButton.setVisibility(View.VISIBLE);
                    break;
                } else {
                    binding.settingFragmentEditRolesButton.setVisibility(View.GONE);
                }
            }

            binding.settingFragmentEditRolesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Navigation.findNavController(v).navigate(R.id.editRolesFragment);
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
        binding = null;
    }
}