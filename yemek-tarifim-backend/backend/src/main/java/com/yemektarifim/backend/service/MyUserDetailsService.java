package com.yemektarifim.backend.service;

import com.yemektarifim.backend.model.Login;
import com.yemektarifim.backend.repository.LoginRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@AllArgsConstructor
@Service
public class MyUserDetailsService implements UserDetailsService {

    private final LoginRepository loginRepository;

    /**
     * Kullanıcı adını, authentication (login) yapmış kullanıcılara eklememizi sağlayan method.
     *
     * @param userName Login olacak kullanıcının userName bilgisi.
     */
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Login login = loginRepository.findByUsername(userName).orElseThrow();
        return new User(login.getUsername(), login.getPassword(), new ArrayList<>());
    }

}
