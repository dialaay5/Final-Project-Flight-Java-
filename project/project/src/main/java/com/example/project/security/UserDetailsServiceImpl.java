package com.example.project.security;

import com.example.project.model.User;
import com.example.project.model.UserRoleName;
import com.example.project.repository.UserRepository;
import com.example.project.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.get_user_by_username(username);
        if (user == null) {
            throw new UsernameNotFoundException("Not Found User with username: " + username);
        }
        UserRoleName roleName = userRoleRepository.getUserRoleById(user.getUser_role()).getRole_name();
        String defaultRole = "ANONYMOUS";

        if (roleName == UserRoleName.ADMINISTRATOR || roleName == UserRoleName.CUSTOMER || roleName == UserRoleName.AIRLINE_COMPANY) {
            return UserDetailsImpl.build(user, roleName.name());

        } else {
            return UserDetailsImpl.build(user, defaultRole);
        }
    }
}