package com.trackademy.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.trackademy.entity.User;
import com.trackademy.repository.UserRepository;
import java.time.LocalDateTime;

@Service
@Transactional
public class UserService {
    
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public User upsertFromExternal(String provider, String externalId, String email, String name, String picture) {
        logger.info("Upserting user from external provider: {}, email: {}", provider, email);
        
        User user = userRepository.findByProviderAndExternalId(provider, externalId)
                .orElseGet(() -> {
                    logger.info("Creating new user: provider={}, email={}", provider, email);
                    User newUser = User.builder()
                            .provider(provider)
                            .externalId(externalId)
                            .email(email)
                            .name(name != null ? name : email)
                            .picture(picture)
                            .role("USER")
                            .active(true)
                            .createdAt(LocalDateTime.now())
                            .build();
                    return userRepository.save(newUser);
                });
        
        boolean updated = false;
        if (name != null && !name.equals(user.getName())) {
            user.setName(name);
            updated = true;
        }
        if (picture != null && !picture.equals(user.getPicture())) {
            user.setPicture(picture);
            updated = true;
        }
        
        if (updated) {
            user.setUpdatedAt(LocalDateTime.now());
            logger.debug("Updating user profile: email={}", email);
            return userRepository.save(user);
        }
        
        logger.debug("User already exists: email={}", email);
        return user;
    }
    
    public User findByEmail(String email) {
        logger.debug("Finding user by email: {}", email);
        return userRepository.findByEmail(email).orElse(null);
    }
}
