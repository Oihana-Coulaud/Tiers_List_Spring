package com.example.tp_spring.service;

import java.util.Collections;
import java.util.Set;

import com.example.tp_spring.entity.User;
import com.example.tp_spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserDetail implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    @CacheEvict(value = "userDetails", key = "#username")
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameOrEmail(username, username);
        if(user==null) {
            throw new UsernameNotFoundException("L'utilisateur n'existe pas");
        }
        Set<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(user.getRole().getName()));
        return new org.springframework.security.core.userdetails.User(username,user.getPassword(),authorities);
    }

    public boolean checkPassword(User user, String rawPassword) {
        // Use PasswordEncoder to check if rawPassword matches the encoded password
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

}