package com.tezodevi.android.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tezodevi.android.databinding.EditRoleLayoutBinding;
import com.tezodevi.android.model.EditRoleModel;
import com.tezodevi.android.model.Login;
import com.tezodevi.android.model.Role;
import com.tezodevi.android.service.worker_services.LoginService;
import com.tezodevi.android.util.ImageHelper;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class EditRoleAdapter extends RecyclerView.Adapter<EditRoleAdapter.EditRoleViewHolder> {

    private final LoginService loginService;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final List<EditRoleModel> roleList;
    private final String[] baseRoleList;
    private final List<Role> itemList;
    private String selectedRoleName;
    private int changedItemId;

    public EditRoleAdapter(List<EditRoleModel> roleList, LoginService loginService, List<Role> baseRoleList) {
        this.roleList = roleList;
        this.loginService = loginService;
        this.baseRoleList = new String[baseRoleList.size()];
        this.itemList = baseRoleList;

        for (int i = 0; i < this.baseRoleList.length; i++) {
            this.baseRoleList[i] = baseRoleList.get(i).roleName;
        }
    }

    @NonNull
    @Override
    public EditRoleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        EditRoleLayoutBinding binding = EditRoleLayoutBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );

        return new EditRoleViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull EditRoleViewHolder holder, int position) {
        EditRoleModel model = roleList.get(position);

        holder.binding.profilePhoto.setImageBitmap(ImageHelper.toBitmap(model.profilePhoto));
        holder.binding.usernameText.setText(model.username);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(holder.binding.getRoot().getContext(), android.R.layout.simple_spinner_dropdown_item, baseRoleList);
        holder.binding.userSpinnerLayout.setAdapter(adapter);

        for (String temp : baseRoleList) {
            for (Role tempRole : model.roles) {
                if (temp.equals(tempRole.roleName)) {
                    selectedRoleName = tempRole.roleName;
                    break;
                }
            }
        }

        for (int i = 0; i < baseRoleList.length; i++) {
            if (baseRoleList[i].equals(selectedRoleName)) {
                holder.binding.userSpinnerLayout.setSelection(i);
                break;
            }
        }

        holder.binding.userSpinnerLayout.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int itemPosition, long id) {
                selectedRoleName = baseRoleList[itemPosition];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedRoleName = baseRoleList[position];
            }
        });

        holder.binding.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeRole(selectedRoleName, model.username);
                changedItemId = position;
            }
        });
    }

    private void changeRole(String roleName, String username) {
        Role result = null;
        for (Role role : itemList) {
            if (role.roleName.equals(roleName)) {
                result = new Role(role.id, roleName);
                break;
            }
        }

        if (result == null) {
            return;
        }

        compositeDisposable.add(
                loginService.changeRole(result, username).subscribe(this::handleRole, Throwable::printStackTrace)
        );
    }

    private void handleRole(Login login) {
        notifyItemChanged(changedItemId + 1);
    }

    @Override
    public int getItemCount() {
        return roleList.size();
    }

    public static class EditRoleViewHolder extends RecyclerView.ViewHolder {
        private final EditRoleLayoutBinding binding;

        public EditRoleViewHolder(EditRoleLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}