package com.example.YAPO.service;

import com.example.YAPO.models.MyUserDetails;
import com.example.YAPO.models.User;
import com.example.YAPO.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        if (user == null) {
            System.out.println("User not found: " + username);
            throw new UsernameNotFoundException("User not found: " + username);
        }

        return new MyUserDetails(user);
    }
}
