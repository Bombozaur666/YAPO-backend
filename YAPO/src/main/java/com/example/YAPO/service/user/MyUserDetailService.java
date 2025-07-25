package com.example.YAPO.service.user;

import com.example.YAPO.models.User.MyUserDetails;
import com.example.YAPO.models.User.User;
import com.example.YAPO.models.enums.ErrorList;
import com.example.YAPO.repositories.user.UserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailService implements UserDetailsService {
    private final UserRepo userRepo;

    public MyUserDetailService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(ErrorList.USER_NOT_FOUND.toString());
        }
        return new MyUserDetails(user);
    }
}
