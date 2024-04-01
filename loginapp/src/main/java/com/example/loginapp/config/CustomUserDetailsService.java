package com.example.loginapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.loginapp.daorepo.DaoRepo;
import com.example.loginapp.model.UserMaster;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private DaoRepo repo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserMaster user = repo.getByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        } else {
            return new CustomUser(user);
        }
    }
}
