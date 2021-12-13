package com.yemektarifim.backend.service;

import com.yemektarifim.backend.mapper.UserMapper;
import com.yemektarifim.backend.model.User;
import com.yemektarifim.backend.model.UserList;
import com.yemektarifim.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private UserRepository userRepository;
    private RecipeService recipeService;
    private ChattingService chattingService;
    private LikeService likeService;
    private CommentService commentService;
    private ScoreService scoreService;
    private LoginService loginService;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setRecipeService(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @Autowired
    public void setChattingService(ChattingService chattingService) {
        this.chattingService = chattingService;
    }

    @Autowired
    public void setLikeService(LikeService likeService) {
        this.likeService = likeService;
    }

    @Autowired
    public void setCommentService(CommentService commentService) {
        this.commentService = commentService;
    }

    @Autowired
    public void setScoreService(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    @Autowired
    public void setUserRepository(LoginService loginService) {
        this.loginService = loginService;
    }

    /**
     * @return Tüm kullanıcı listesi
     */
    public List<UserList> getAll() {
        List<User> all = userRepository.findAll();
        List<UserList> result = new ArrayList<>();
        all.forEach(o -> result.add(new UserList(o.getId(), loginService.getUsernameByLoginId(o.getLogin().getId()), o.getProfilePhoto())));
        return result;
    }

    /**
     * @param id Kullanıcının userId değeri
     * @return O id değerine sahip kullanıcı.
     */
    public User getById(String id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException(
                String.format("Cannot Find User by Id %s", id)
        ));
    }

    /**
     * Veritabanına kullanıcı kaydeder.
     */
    public void add(User user) {
        if (user.getFriends() == null) {
            user.setFriends(new HashSet<>());
        }

        userRepository.insert(user);
    }

    /**
     * Veritabanındaki bir kullanıcının bilgilerini günceller.
     */
    public void update(User user) {
        User database = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException(
                        String.format("Cannot find User by Id %s", user.getId())
                ));

        User result = UserMapper.updateUser(database, user);

        userRepository.save(result);
    }

    /**
     * User id değerine göre kullanıcının tüm bilgilerini, yemek tariflerini vs. siler.
     */
    private void deleteById(String userId) {
        scoreService.deleteByUserId(userId);
        commentService.deleteByUserId(userId);
        likeService.deleteByUserId(userId);
        chattingService.deleteByUserId(userId);
        recipeService.deleteByUserId(userId);
        userRepository.deleteById(userId);
    }

    /**
     * Kullanıcının login id değerine göre tüm bilgilerini, yemek tariflerini vs. siler.
     */
    public void deleteByLoginId(String loginId) {
        User user = userRepository.findByLoginId(loginId).orElseThrow();
        deleteById(user.getId());
    }

    /**
     * Kullanıcının user id değerine göre tüm bilgilerini, yemek tariflerini vs. siler.
     */
    public void deleteByUserId(String userId) {
        User user = userRepository.findById(userId).orElseThrow();
        deleteById(user.getId());
        deleteByLoginId(user.getLogin().getId());
    }

    /**
     * @return Parametre olarak girilen ülke id değerine sahip kullanıcılar
     */
    public Set<User> findByCountryId(String countryId) {
        return userRepository.findByCountryId(countryId).orElseThrow();
    }

    /**
     * Kullanıcı çevrim içi olduğunda bu bilgiyi veritabanında günceller.
     *
     * @param loginId  Kullanıcının login id değeri
     * @param isOnline Kullanıcının çevrim içi bilgisi. Çevrimiçiyse true, değilse false girilmelidir.
     */
    public void setUserIsOnline(String loginId, boolean isOnline) {
        final User user = userRepository.findByLoginId(loginId).orElseThrow();
        user.setIsOnline(isOnline);
        update(user);
    }

    public User getByLoginId(String id) {
        return userRepository.findByLoginId(id).orElseThrow();
    }

    public User getByUsername(String username) {
        return loginService.getByUsername(username);
    }

    public User changePhoto(String username, User user) {
        User database = loginService.getByUsername(username);
        database.setProfilePhoto(user.getProfilePhoto());
        return userRepository.save(database);
    }

    public User updateUser(String username, User user) {
        User database = loginService.getByUsername(username);
        database.setName(user.getName());
        database.setSurname(user.getSurname());
        database.setAge(user.getAge());
        database.setCountry(user.getCountry());
        database.setIsOnline(user.getIsOnline());
        return userRepository.save(database);
    }

    public User addFriend(String username, User friend) {
        User me = getByUsername(username);

        if (me.getId().equals(friend.getId())) {
            return me;
        }

        Set<User> friends = me.getFriends();

        for (User item : friends) {
            if (item.getId().equals(friend.getId())) {
                return me;
            }
        }

        friends.add(friend);
        userRepository.save(me);

        return friend;
    }

    public String getUsernameByUserId(String userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return loginService.getUsernameByLoginId(user.getLogin().getId());
    }

    public User removeFriend(String username, User friend) {
        User byUsername = getByUsername(username);
        byUsername.getFriends().removeIf(friendTemp -> friendTemp.getId().equals(friend.getId()));
        return userRepository.save(byUsername);
    }

    public List<UserList> getFriendList(String username) {
        User me = getByUsername(username);
        Set<User> friends = me.getFriends();

        List<UserList> result = new ArrayList<>();
        for (User user : friends) {
            result.add(new UserList(user.getId(), loginService.getUsernameByLoginId(user.getLogin().getId()), user.getProfilePhoto()));
        }

        return result;
    }
}
