package com.yemektarifim.backend.controller;

import com.yemektarifim.backend.model.*;
import com.yemektarifim.backend.service.LoginService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final LoginService loginService;

    /**
     * Kullanıcının login olabilmesi için gerekli method.
     *
     * @return Kullanıcı login olabilirse String olarak bir json web token döndürür. Bu token ile yetkili olduğu işlemleri gerçekleştirebilir.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) throws Exception {
        if (loginService.isBanned(authRequest.getUsername())) {
            return ResponseEntity.badRequest().build();
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        final String jwt = loginService.login(authRequest);

        return ResponseEntity.ok(new AuthResponse(jwt));
    }

    /**
     * Kullanıcıyı veritabanına kaydeden method.
     *
     * @return Kullanıcı kaydedilebilirse bir token değeri döndürür.
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody LoginUser loginUser) {
        try {
            String jwtToken = loginService.register(loginUser.getLogin(), loginUser.getUser());
            return ResponseEntity.ok(new AuthResponse(jwtToken));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
    }

    /**
     * Kullanıcının login şifresini değiştirebildiğimiz method.
     *
     * @return Kullanıcının şifresi değiştirilebilirse yeni bir token oluşturulur.
     */
    @PutMapping("/{username}/change-password")
    public ResponseEntity<AuthResponse> changePassword(@PathVariable String username, @RequestBody Login login) {
        try {
            String jwtToken = loginService.changePassword(username, login);
            return ResponseEntity.ok(new AuthResponse(jwtToken));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
    }

    /**
     * Kullanıcının çıkış yapabildiği method.
     * Kullanıcı çıkış yapabilirse veritabanındaki token bilgileri silinir.
     */
    @GetMapping("/{username}/logout")
    public ResponseEntity<?> logout(@PathVariable String username) {
        try {
            loginService.logout(username);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).build();
        }
    }

    /**
     * Kullanıcının hesabını silebildiği method.
     */
    @GetMapping("/{username}/delete")
    public ResponseEntity<?> delete(@PathVariable String username) {
        try {
            loginService.delete(username);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
    }

    @GetMapping("/detail/{username}/me")
    public ResponseEntity<Login> getMe(@PathVariable String username) {
        Login loginByUsername = loginService.getLoginByUsername(username);
        return ResponseEntity.ok(loginByUsername);
    }

    @GetMapping("/detail/{username}/me/token")
    public ResponseEntity<AuthResponse> getToken(@PathVariable String username) {
        try {
            String jwtToken = loginService.getToken(username);
            return ResponseEntity.ok(new AuthResponse(jwtToken));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
    }

    @PutMapping("/detail/{username}/update")
    public ResponseEntity<Login> updateLogin(@PathVariable String username, @RequestBody Login login) {
        Login loginByUsername = loginService.updateLoginByUsername(username, login);
        return ResponseEntity.ok(loginByUsername);
    }

    @GetMapping("/role/{username}/my-role")
    public ResponseEntity<EditRoleModel> getMyRole(@PathVariable String username) {
        EditRoleModel users = loginService.getMyRole(username);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/role/admin")
    public ResponseEntity<List<EditRoleModel>> editRoleForAdmin() {
        List<EditRoleModel> users = loginService.editRoleForAdmin();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/role/moderator")
    public ResponseEntity<List<EditRoleModel>> editRoleForModerator() {
        List<EditRoleModel> users = loginService.editRoleForModerator();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/role/all")
    public ResponseEntity<List<Role>> getRoles() {
        return ResponseEntity.ok(loginService.roleList());
    }

    @PutMapping("/role/{username}/change-role")
    public ResponseEntity<Login> changeRole(@PathVariable String username, @RequestBody Role role) {
        return ResponseEntity.ok(loginService.changeRole(username, role.getRoleName()));
    }
}
