package com.example.Test.Controller;

import com.example.Test.model.Board;
import com.example.Test.model.User;
import com.example.Test.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import java.util.List;

@RestController
@RequestMapping(value = "/api", produces = "application/json; charset=utf-8")
public class UserApiController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping(value = "/users", produces = "application/json; charset=utf8")
    List<User> all() {
        List<User> users = userRepository.findAll();
        System.out.println("----------------------------------------------------");
        System.out.println(users.get(0).getBoards().size());
        System.out.println(users.size());
        System.out.println("----------------------------------------------------");
        return users;
    }

    @PostMapping("/users")
    User newUser(@RequestBody User newUser) {
        return userRepository.save(newUser);
    }

    @GetMapping("/users/{id}")
    User one(@PathVariable Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @PutMapping("/users/{id}")
    User replaceUser(@RequestBody User newUser, @PathVariable Long id) {
        return userRepository.findById(id)
                .map(user -> {
//                    user.setTitle(newUser.getTitle());
//                    user.setContent(newUser.getContent());
//                    user.setBoards(newUser.getBoards());
                    user.getBoards().clear();
                    for (Board board : user.getBoards()) {
                        board.setUser(user);
                    }
                    return userRepository.save(user);
                })
                .orElseGet(() -> {
                    newUser.setId(id);
                    return userRepository.save(newUser);
                });
    }

    @DeleteMapping("/users/{id}")
    void deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
    }
}
