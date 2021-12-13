package com.tezodevi.android.view.user.fragment.editroles;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.tezodevi.android.adapter.EditRoleAdapter;
import com.tezodevi.android.databinding.FragmentEditRolesBinding;
import com.tezodevi.android.model.EditRoleModel;
import com.tezodevi.android.model.Role;
import com.tezodevi.android.service.worker_services.LoginService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import io.reactivex.disposables.CompositeDisposable;

public class EditRolesFragment extends Fragment {
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private FragmentEditRolesBinding binding;
    private LoginService loginService;
    private List<Role> baseRoleList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginService = new LoginService(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditRolesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
        binding = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.fragmentEditRolesUserRolesList.setLayoutManager(new LinearLayoutManager(requireContext()));

        compositeDisposable.add(
                loginService.getRoles().subscribe(this::handleRoleList, Throwable::printStackTrace)
        );
    }

    private void handleRoleList(List<Role> roles) {
        this.baseRoleList = roles;

        compositeDisposable.add(
                loginService.getMyRole().subscribe(this::handleRole, Throwable::printStackTrace)
        );
    }

    private void handleRole(EditRoleModel editRoleModel) {
        Set<Role> roles = editRoleModel.roles;

        for (Role role : roles) {
            if (role.roleName.equals("ADMIN")) {
                compositeDisposable.add(
                        loginService.getRolesForAdmin().subscribe(this::handleRoles, Throwable::printStackTrace)
                );

                break;
            } else if (role.roleName.equals("MODERATOR")) {
                compositeDisposable.add(
                        loginService.getRolesForModerator().subscribe(this::handleRoles, Throwable::printStackTrace)
                );

                break;
            }
        }
    }

    private void handleRoles(List<EditRoleModel> roleList) {
        binding.fragmentEditRolesUserRolesList.setLayoutManager(new LinearLayoutManager(requireContext()));
        EditRoleAdapter adapter = new EditRoleAdapter(roleList, loginService, baseRoleList);
        binding.fragmentEditRolesUserRolesList.setAdapter(adapter);
    }
}