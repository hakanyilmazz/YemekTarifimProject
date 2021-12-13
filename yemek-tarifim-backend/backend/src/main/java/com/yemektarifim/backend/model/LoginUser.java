package com.yemektarifim.backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoginUser {
    private Login login;
    private User user;
}
