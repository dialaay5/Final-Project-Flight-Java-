package com.example.project.controller;

import com.example.project.model.*;
import com.example.project.model.payload.response.MessageResponse;
import com.example.project.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UsersService usersService;
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    @GetMapping("/getAll")
    //get all users
    public ResponseEntity<?> getAllUsers() {
        try {
            List<User> list = usersService.getAllUsers();
            return ResponseEntity.ok(list);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }

    @GetMapping("/userById/{id}")
    //get user by id
    public ResponseEntity<?> getUserById(@PathVariable("id") Long id) {
        try {
            User user = usersService.getUserById(id);
            if (user != null) {
                return ResponseEntity.ok(user);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Not found user with Id " + id));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }

    @GetMapping("/userByUsername/{user_name}")
    //get user by username
    public ResponseEntity<?> getUserByUsername(@PathVariable String user_name){
        try {
            User user = usersService.get_user_by_username(user_name);
            if (user != null) {
                return ResponseEntity.ok(user);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Not found user with this username " + user_name));
        }
        catch (ClientFaultException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }

    @GetMapping("/userByEmail/{email}")
    //get user by email
    public ResponseEntity<?> getUserByEmail(@PathVariable String email){
        try {
            User user = usersService.get_user_by_email(email);
            if (user != null) {
                return ResponseEntity.ok(user);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Not found user with this email " + email));
        }
        catch (ClientFaultException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    @PostMapping("/addUsersList")
    //add all users
    public ResponseEntity<?> addAllUsers(@RequestBody List<User> users){
        try{
            usersService.addAllUsers(users);
            return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("New list of users has been added!"));
        }
        catch (ClientFaultException e){
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }

    @PostMapping("/addUser")
    //add user
    public ResponseEntity<?> addUser(@RequestBody User user){
        try{
            User addUser = usersService.addUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(addUser);
        }
        catch (ClientFaultException e){
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }

    @DeleteMapping("/removeUser/{id}")
    //remove user
    public ResponseEntity<?> removeUser(@PathVariable("id") Long id){
        try {
            usersService.removeUser(id);
            return ResponseEntity.ok(new MessageResponse("The user has been removed!"));
        }
        catch (UserIdNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    @PutMapping("/updateUser/{id}")
    //update user
    public ResponseEntity<?> updateUser(@PathVariable("id") Long id, @RequestBody User user) {
        try{
            usersService.updateUser(user, id);
            return ResponseEntity.ok(user);
        }
        catch (UserIdNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(e.getMessage()));
        }
        catch (ClientFaultException | DuplicateKeyException e){
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }
}
