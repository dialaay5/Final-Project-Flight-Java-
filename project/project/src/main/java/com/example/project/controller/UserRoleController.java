package com.example.project.controller;

import com.example.project.model.*;
import com.example.project.model.payload.response.MessageResponse;
import com.example.project.service.UserRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin
@RestController
@RequestMapping("/api/userRole")
public class UserRoleController {
    @Autowired
    UserRuleService userRuleService;

    @GetMapping("/getAll")
    //get all userRoles
    public ResponseEntity<?> getAllUserRules() {
        try {
            List<UserRole> list = userRuleService.getAllUserRoles();
            return ResponseEntity.ok(list);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }

    @GetMapping("/userRoleById/{id}")
    //get userRole by id
    public ResponseEntity<?> userRoleById(@PathVariable("id") Integer id) {
        try {
            UserRole userRole = userRuleService.getUserRoleById(id);
            if (userRole != null) {
                return ResponseEntity.ok(userRole);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Not found userRole with Id " + id));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }

    @PostMapping("/addUserRolesList")
    //add all userRoles
    public ResponseEntity<?> addAllUserRoles(@RequestBody List<UserRole> userRoles){
        try{
            userRuleService.addAllUserRoles(userRoles);
            return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("New list of userRoles has been added!"));
        }
        catch (ClientFaultException e){
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }

    @PostMapping("/addUserRole")
    //add userRule
    public ResponseEntity<?> addUserRole(@RequestBody UserRole userRole){
        try{
            UserRole addUserRole = userRuleService.addUserRole(userRole);
            return ResponseEntity.status(HttpStatus.CREATED).body(addUserRole);
        }
        catch (ClientFaultException e){
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }

    @PutMapping("/updateUserRule/{id}")
    //update userRule
    public ResponseEntity<?> updateUserRule(@PathVariable("id") Integer id, @RequestBody UserRole userRole) {
        try{
            userRuleService.updateUserRole(userRole, id);
            return ResponseEntity.ok(userRole);
        }
        catch (InvalidUserRoleException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(e.getMessage()));
        }
        catch (ClientFaultException e){
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }

    @DeleteMapping("/removeUserRole/{id}")
    //remove userRole
    public ResponseEntity<?> removeUserRole(@PathVariable("id") Integer id){
        try {
            userRuleService.removeUserRole(id);
            return ResponseEntity.ok(new MessageResponse("The userRole has been removed!"));
        }
        catch (InvalidUserRoleException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }
    @GetMapping("/userRoleByRoleName/{role_name}")
    //get userRole by RoleName
    public ResponseEntity<?> getUserRoleByRoleName(@PathVariable("role_name") UserRoleName role_name){
        try {
            UserRole userRole = userRuleService.getUserRoleByRoleName(role_name);
            if(userRole != null){
                return ResponseEntity.ok(userRole);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Not found userRole with RoleName " + role_name));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Internal server error: " + e.getMessage()));
        }
    }
}
