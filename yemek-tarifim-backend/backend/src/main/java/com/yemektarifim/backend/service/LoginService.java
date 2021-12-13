package com.yemektarifim.backend.service;

import com.yemektarifim.backend.model.*;
import com.yemektarifim.backend.repository.CountryRepository;
import com.yemektarifim.backend.repository.LoginRepository;
import com.yemektarifim.backend.repository.RoleRepository;
import com.yemektarifim.backend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LoginService {

    private PasswordEncoder passwordEncoder;
    private JwtUtil jwtUtil;
    private LoginRepository loginRepository;
    private RoleRepository roleRepository;
    private CountryRepository countryRepository;
    private UserService userService;
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setJwtUtil(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Autowired
    public void setLoginRepository(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Autowired
    public void setCountryRepository(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setMyUserDetailsService(MyUserDetailsService myUserDetailsService) {
        this.myUserDetailsService = myUserDetailsService;
    }

    /**
     * Custom oluşturulan rolleri authorization işlemi için gerekli nesneye çevirir. Bu rol tanımlamaları için gerekli.
     *
     * @param roleSet Security rolleri için dönüştürülecek roller.
     */
    public static Collection<? extends GrantedAuthority> convertRoleToAuthorityCollection(Set<Role> roleSet) {
        Collection<SimpleGrantedAuthority> authorities = new HashSet<>();

        for (Role role : roleSet) {
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.getRoleName());
            authorities.add(authority);
        }

        return authorities;
    }

    /**
     * Kullanıcı adı ve şifre bilgisindeki boşlukları kaldırarak login işlemini gerçekleştiren method.
     *
     * @throws RuntimeException nesnesi fırlatır
     */
    public String login(AuthRequest authRequest) throws RuntimeException {
        String username = authRequest.getUsername().trim();
        String password = authRequest.getPassword().trim();

        if (username.length() == 0 || password.length() == 0) {
            throw new RuntimeException("Username or password invalid! Length should be > 0");
        }

        final UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);
        final String jwtToken = jwtUtil.generateToken(username, userDetails.getAuthorities());

        final Login login = loginRepository.findByUsername(username).orElseThrow();
        userService.setUserIsOnline(login.getId(), true);

        return setTokenForLogin(jwtToken, login);
    }

    /**
     * Gerekli bilgileri doğrulayıp veritabanına kaydeden method.
     *
     * @param login Kullanıcı adı, şifresi ve email gibi bilgileri içeren nesne.
     * @param user  Kullanıcı hakkında bilgileri içeren nesne
     */
    public String register(Login login, User user) throws Exception {
        String username = login.getUsername().trim();
        String password = login.getPassword().trim();
        String email = login.getEmail().trim();

        String firstName = user.getName().trim();
        String lastName = user.getSurname().trim();
        Integer age = user.getAge();
        String countryId = user.getCountry().getId();

        // Girilen bilgiler geçersizse veya hiçbir veri girilmediyse hata fırlatır. Bu sayede veritabanına gereksiz veri kaydedilmez.
        if (username.length() == 0 || password.length() == 0 || email.length() == 0 || firstName.length() == 0 || lastName.length() == 0
                || age < 0 || countryId.length() == 0) {
            throw new Exception("Invalid inputs.");
        }

        // Eğer olmayan bir ülke id değeri giriliyorsa hata fırlatır.
        if (!countryRepository.existsById(countryId)) {
            throw new Exception("Invalid country.");
        }

        // Username ve email'in unique bir değer olmasını sağlar.
        if (loginRepository.existsByUsername(username) == null && loginRepository.existsByEmail(email) == null) {
            final Role role = roleRepository.findByRoleName("USER").orElseThrow();
            Set<Role> userRoleList = login.getUserRoleList();

            if (userRoleList == null) {
                userRoleList = new HashSet<>();
            }

            userRoleList.add(role);

            Login willSaveLogin = new Login();
            willSaveLogin.setUsername(username);
            willSaveLogin.setPassword(passwordEncoder.encode(password));
            willSaveLogin.setEmail(email);
            willSaveLogin.setUserTokenList(login.getUserTokenList());
            willSaveLogin.setUserRoleList(userRoleList);

            Login insertedLogin = loginRepository.insert(willSaveLogin);

            User willSaveUser = new User();
            willSaveUser.setLogin(insertedLogin);
            willSaveUser.setName(user.getName());
            willSaveUser.setSurname(user.getSurname());
            willSaveUser.setAge(user.getAge());
            willSaveUser.setProfilePhoto(user.getProfilePhoto());
            willSaveUser.setCountry(user.getCountry());
            willSaveUser.setIsOnline(user.getIsOnline());

            userService.add(willSaveUser);

            Collection<? extends GrantedAuthority> authorities = convertRoleToAuthorityCollection(insertedLogin.getUserRoleList());

            String jwtToken = jwtUtil.generateToken(insertedLogin.getUsername(), authorities);

            return setTokenForLogin(jwtToken, insertedLogin);
        } else {
            throw new RuntimeException("Username is already in use");
        }
    }

    /**
     * @return Şifre değiştirildikten sonraki yeni jwt token.
     */
    public String changePassword(String username, Login login) throws RuntimeException {
        final Login loginByUsername = loginRepository.findByUsername(username).orElseThrow();

        String password = login.getPassword().trim();

        if (password.length() == 0) {
            throw new RuntimeException("Invalid password! Password length should be >= 0");
        }

        loginByUsername.setPassword(password);
        final Login savedLogin = loginRepository.save(loginByUsername);

        String jwtToken = jwtUtil.generateToken(savedLogin.getUsername(), convertRoleToAuthorityCollection(savedLogin.getUserRoleList()));

        return setTokenForLogin(jwtToken, savedLogin);
    }

    /**
     * Kullanıcı logout işlemini yapan method. Kullanıcı çıkış yaptıktan sonra tüm token bilgileri silinir.
     */
    public void logout(String username) throws RuntimeException {
        final Login loginById = loginRepository.findByUsername(username).orElseThrow();

        if (loginById.getUserTokenList() == null) {
            throw new RuntimeException("Already logout.");
        }

        userService.setUserIsOnline(loginById.getId(), false);

        loginById.setUserTokenList(null);
        loginRepository.save(loginById);
    }

    public void delete(String username) throws RuntimeException {
        Login login = loginRepository.findByUsername(username).orElseThrow();
        userService.deleteByLoginId(login.getId());
        loginRepository.deleteById(login.getId());
    }

    public void deleteByCountryId(String countryId) {
        final Set<User> usersByCountryId = userService.findByCountryId(countryId);
        for (User user : usersByCountryId) {
            delete(user.getLogin().getId());
        }
    }

    /**
     * Login nesnesine token bilgilerini ekler. Yani kullanıcının login olabilmesi için token bilgileri veritabanına kaydedilir.
     */
    private String setTokenForLogin(String jwtToken, Login login) {
        Set<String> userTokenList = login.getUserTokenList();

        if (userTokenList == null) {
            userTokenList = new HashSet<>();
        }

        userTokenList.add(jwtToken);
        login.setUserTokenList(userTokenList);

        loginRepository.save(login);

        return jwtToken;
    }

    public User getByUsername(String username) {
        Login login = loginRepository.findByUsername(username).orElseThrow();
        return userService.getByLoginId(login.getId());
    }

    public boolean isBanned(String username) {
        Login login = loginRepository.findByUsername(username).orElseThrow();
        Set<Role> userRoleList = login.getUserRoleList();

        for (Role role : userRoleList) {
            String roleName = role.getRoleName();
            if (roleName.equals("BANNED")) {
                return true;
            }
        }

        return false;
    }

    public Login getLoginByUsername(String username) {
        return loginRepository.findByUsername(username).orElseThrow();
    }

    public Login updateLoginByUsername(String username, Login login) {
        Login database = loginRepository.findByUsername(username).orElseThrow();

        database.setUsername(login.getUsername());
        database.setEmail(login.getEmail());
        database.setPassword(login.getPassword());

        return loginRepository.save(database);
    }

    public String getToken(String username) {
        final UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);
        final String jwtToken = jwtUtil.generateToken(username, userDetails.getAuthorities());

        final Login login = loginRepository.findByUsername(username).orElseThrow();
        userService.setUserIsOnline(login.getId(), true);

        return setTokenForLogin(jwtToken, login);
    }

    public String getUsernameByLoginId(String loginId) {
        return loginRepository.findById(loginId).orElseThrow().getUsername();
    }

    public List<EditRoleModel> editRoleForAdmin() {
        List<UserList> all = userService.getAll();
        List<EditRoleModel> result = new ArrayList<>();
        for (UserList temp : all) {
            Login login = loginRepository.findByUsername(userService.getUsernameByUserId(temp.getId())).orElseThrow();
            Set<Role> userRoleList = login.getUserRoleList();

            for (Role role : userRoleList) {
                if (!role.getRoleName().equals("ADMIN")) {
                    result.add(new EditRoleModel(temp.getProfilePhoto(), temp.getUsername(), userRoleList));
                    break;
                }
            }
        }

        return result;
    }

    public List<Role> roleList() {
        return roleRepository.findAll();
    }

    public List<EditRoleModel> editRoleForModerator() {
        List<UserList> all = userService.getAll();
        List<EditRoleModel> result = new ArrayList<>();
        for (UserList temp : all) {
            Login login = loginRepository.findById(userService.getUsernameByUserId(temp.getId())).orElseThrow();
            Set<Role> userRoleList = login.getUserRoleList();

            for (Role role : userRoleList) {
                if (!role.getRoleName().equals("MODERATOR") && !role.getRoleName().equals("ADMIN")) {
                    result.add(new EditRoleModel(temp.getProfilePhoto(), temp.getUsername(), userRoleList));
                    break;
                }
            }
        }

        return result;
    }

    public EditRoleModel getMyRole(String username) {
        Login loginByUsername = getLoginByUsername(username);
        User byUsername = userService.getByUsername(username);
        return new EditRoleModel(byUsername.getProfilePhoto(), loginByUsername.getUsername(), loginByUsername.getUserRoleList());
    }

    public Login changeRole(String username, String roleName) {
        Login loginByUsername = getLoginByUsername(username);
        Set<Role> userRoleList = loginByUsername.getUserRoleList();
        Role insert = roleRepository.findByRoleName(roleName).orElseThrow();

        userRoleList.clear();

        userRoleList.add(insert);

        loginByUsername.setUserRoleList(userRoleList);

        return loginRepository.save(loginByUsername);
    }
}
