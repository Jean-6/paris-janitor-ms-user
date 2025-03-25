package com.example.parisjanitormsuser.controller;


import com.example.parisjanitormsuser.entity.User;
import com.example.parisjanitormsuser.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    
    private final UserService userService;

    static Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping(value="/",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> save(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.save(user));

    }

    @GetMapping(value="/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getById(@PathVariable("id") Long id) {
        return userService.findById(id)
                .map(role->{
                    logger.info("get user by id");
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(role);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(value="/",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> getAll(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.findAll());
    }

    @PutMapping(value="/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Optional<User>> updateById(@PathVariable Long id, @RequestBody User user) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.update(id, user));
    }

    @DeleteMapping(value="/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Optional<User>> deleteById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.delete(id));
    }


}
