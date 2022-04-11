package com.hust.smartparking.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.hust.smartparking.entity.User;
import com.hust.smartparking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Iterable<User> userIterable = userRepository.findByUsername(username);
        List<User> userList= new ArrayList<>();
        userIterable.forEach(userList::add);
        User user= null;
        List<SimpleGrantedAuthority> roles = null;
        if(userList.size()>0){
            user=userList.get(0);
            roles = Arrays.asList(new SimpleGrantedAuthority(user.getRole()));
        }
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                roles);
    }

    public User save(User user) {
        user.setPassword(bcryptEncoder.encode(user.getPassword()));
        user.setRole("ROLE_GUEST");
        user.setCreatedDate(new Timestamp(new Date().getTime()));
        return userRepository.save(user);
    }
}