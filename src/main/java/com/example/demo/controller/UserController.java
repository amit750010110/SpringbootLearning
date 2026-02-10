package com.example.demo.controller;


import com.example.demo.dto.PageResponse;
import com.example.demo.dto.UserDTO;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);  // HTTP 200 OK
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Integer id) {
        User user = userService.getById(id);
        return ResponseEntity.ok(user);  // HTTP 200 OK
    }


    @PostMapping("/add")
    public ResponseEntity<User> addUser(@RequestBody UserDTO userDTO) {
        User createdUser = userService.addUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);  // HTTP 201 CREATED
    }

    @PostMapping("/bulkUserCreationByJson")
    public ResponseEntity<List<User>> bulkUserCreationByJson(@RequestBody List<UserDTO> userDTOList) {
        List<User> createdUsers = userService.bulkUserCreationByJson(userDTOList);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUsers);  // HTTP 201 CREATED
    }


    @PostMapping(
            value = "/bulkUserCreationByCSV",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<List<User>> bulkUserCreationByCSV(@RequestParam("file") MultipartFile file) {
        List<User> createdUsers = userService.bulkUserCreationByCSV(file);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUsers);  // HTTP 201 CREATED
    }


    @GetMapping("/search")
    public ResponseEntity<PageResponse<User>> getAllPaginatedWithSearch(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String search) {
        Page<User> userPage = userService.getAllPaginatedWithSearch(page, size, search);
        PageResponse<User> response = new PageResponse<>(userPage);  // Convert to stable DTO
        return ResponseEntity.ok(response);  // HTTP 200 OK
    }

    @GetMapping("/email")
    public ResponseEntity<User> getUserByEmail(@RequestParam(required = true) String email) {
        User user = userService.getByEmail(email);
        return ResponseEntity.ok(user);  // HTTP 200 OK
    }


    @GetMapping("/paginatedSearch")
    public ResponseEntity<PageResponse<User>> getAllUsersUsingSpecification(
            @RequestParam(defaultValue = "0") int page,      // Optional: defaults to first page
            @RequestParam(defaultValue = "10") int size,     // Optional: defaults to 10 items
            @RequestParam(defaultValue = "") String search,   // Optional: defaults to empty (all users)
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String email
    ) {
        Page<User> userPage = userService.getAllUsersUsingSpecification(page, size, search, name, city, email);
        PageResponse<User> response = new PageResponse<>(userPage);  // Convert to stable DTO
        return ResponseEntity.ok(response);  // HTTP 200 OK
    }



}
