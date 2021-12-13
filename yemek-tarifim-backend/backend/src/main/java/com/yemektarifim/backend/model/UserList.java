package com.yemektarifim.backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserList {
    private String id;
    private String username;
    private byte[] profilePhoto;
}
