package com.example.store.service;

import com.example.store.entity.User;
import com.example.store.exception.InvalidSessionException;
import com.example.store.repository.AuthenticatedUsersRepository;
import com.example.store.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final AuthenticatedUsersRepository authenticatedUsersRepository;

    @Autowired
    public UserService(UserRepository userRepository,
                       AuthenticatedUsersRepository authenticatedUsersRepository) {
        this.userRepository = userRepository;
        this.authenticatedUsersRepository = authenticatedUsersRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getUserByName(username);
    }

    public User getUserByName(String username) {
        return userRepository.findByName(username);
    }

    public UserDetails getUserBySessionId(String sessionId) {
        return getUserByName(authenticatedUsersRepository.getUserBySessionId(sessionId));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

}
