package com.yemektarifim.backend.model;

import java.util.Set;

public class EditRoleModel {
    private final byte[] profilePhoto;
    private final String username;
    private final Set<Role> roles;

    public EditRoleModel(byte[] profilePhoto, String username, Set<Role> roles) {
        this.profilePhoto = profilePhoto;
        this.username = username;
        this.roles = roles;
    }

    public byte[] getProfilePhoto() {
        return profilePhoto;
    }

    public String getUsername() {
        return username;
    }

    public Set<Role> getRoles() {
        return roles;
    }
}
