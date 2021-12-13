package com.yemektarifim.backend.controller;

import com.yemektarifim.backend.model.User;
import com.yemektarifim.backend.model.UserList;
import com.yemektarifim.backend.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    /**
     * @return Tüm kullanıcı listesini döndürür.
     */
    @GetMapping
    public ResponseEntity<List<UserList>> getAll() {
        List<UserList> users = userService.getAll();
        return ResponseEntity.ok(users);
    }

    /**
     * @return Belirli bir kullanıcıyı user id değerine göre döndürür.
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable String id) {
        User user = userService.getById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{username}/me")
    public ResponseEntity<User> getByUsername(@PathVariable String username) {
        User user = userService.getByUsername(username);
        return ResponseEntity.ok(user);
    }

    /**
     * Parametre ile verilen kullanıcıyı veritabanına kaydeder.
     */
    @PostMapping
    public ResponseEntity<User> add(@RequestBody User user) {
        userService.add(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Verilen kullanıcı nesnesini veritabanında günceller.
     */
    @PutMapping
    public ResponseEntity<User> update(@RequestBody User user) {
        userService.update(user);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{username}/change-photo")
    public ResponseEntity<User> update(@PathVariable String username, @RequestBody User user) {
        return ResponseEntity.ok(userService.changePhoto(username, user));
    }

    @PutMapping("/{username}/update")
    public ResponseEntity<User> updateUser(@PathVariable String username, @RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(username, user));
    }

    /**
     * User Id değeri verilen kullanıcıyı veritabanından login bilgileriyle ve diğer tüm bilgileriyle beraber siler.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteById(@PathVariable String id) {
        userService.deleteByUserId(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{username}/add-friend")
    public ResponseEntity<User> addFriend(@PathVariable String username, @RequestBody User friend) {
        return ResponseEntity.ok(userService.addFriend(username, friend));
    }

    @PutMapping("/{username}/remove-friend")
    public ResponseEntity<User> removeFriend(@PathVariable String username, @RequestBody User friend) {
        return ResponseEntity.ok(userService.removeFriend(username, friend));
    }

    @GetMapping("/{userId}/get-recipe-user")
    public ResponseEntity<String> getUsernameByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(userService.getUsernameByUserId(userId));
    }

    @GetMapping("/{username}/friend-list")
    public ResponseEntity<List<UserList>> getFriendList(@PathVariable String username) {
        List<UserList> users = userService.getFriendList(username);
        return ResponseEntity.ok(users);
    }
}
