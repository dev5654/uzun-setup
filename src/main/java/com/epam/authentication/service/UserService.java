package com.epam.authentication.service;

import com.epam.authentication.config.encryption.PasswordEncoderConfigurer;
import com.epam.authentication.dto.UserCreateDTO;
import com.epam.authentication.model.UserEntity;
import com.epam.authentication.repo.UserRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(new ArrayList<>())
                .accountLocked(false)
                .accountExpired(false)
                .disabled(false)
                .credentialsExpired(false)
                .build();
    }

    public void register(UserCreateDTO dto) {
        UserEntity userEntity = new UserEntity();
        dto.setPassword(new PasswordEncoderConfigurer().encoder().encode(dto.getPassword()));
        BeanUtils.copyProperties(dto, userEntity);
        userRepo.save(userEntity);
    }
}
