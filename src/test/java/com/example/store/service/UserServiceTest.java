package com.example.store.service;

import com.example.store.entity.User;
import com.example.store.repository.AuthenticatedUsersRepository;
import com.example.store.repository.UserRepository;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticatedUsersRepository authenticatedUsersRepository;

    @Autowired
    @InjectMocks
    private UserService userService;

    private List<User> userList;

    private BidiMap<String, String> userSessionIdMap;

    @BeforeEach
    private void prepareUserList() {
        User user1 = new User("User1@mail", "User1", "password1", true, new HashSet<>());
        User user2 = new User("User2@mail", "User2", "password2", true, new HashSet<>());
        userList = List.of(user1, user2);

        userSessionIdMap = new DualHashBidiMap<>();
        userSessionIdMap.put("User1", "EXSESSID");
    }

    @Test
    public void getUserByNameTest() {
        String name = "User1";
        Mockito.lenient()
               .when(userRepository.findByName(name))
               .thenReturn((userList.get(0)));
        User userByName = userService.getUserByName(name);
        assertEquals(userRepository.findByName(name), userByName);
    }

    @Test
    public void getUserBySessionIdTest() {
        String sessionId = "EXSESSID";
        String name = "User1";
        Mockito.lenient()
               .when(userRepository.findByName(name))
               .thenReturn((userList.get(0)));
        Mockito.lenient()
               .when(authenticatedUsersRepository.getUserBySessionId(sessionId))
               .thenReturn(userSessionIdMap.getKey(sessionId));
        User userBySessionId = (User) userService.getUserBySessionId(sessionId);
        assertEquals(authenticatedUsersRepository.getUserBySessionId(sessionId), userBySessionId.getName());
    }

    @Test
    public void getUserByEmailTest() {
        String email = "User1@mail";
        Mockito.lenient()
               .when(userRepository.findByEmail(email))
               .thenReturn((userList.get(0)));
        User userByEmail = userService.getUserByEmail(email);
        assertEquals(userRepository.findByEmail(email), userByEmail);
    }

    @Test
    public void saveUserTest() {
        User newUser = new User("new@mail", "new", "pass", true, new HashSet<>());
        Mockito.lenient()
               .when(userRepository.save(newUser))
               .thenReturn(newUser);
        User saveUser = userService.saveUser(newUser);
        assertEquals(newUser, saveUser);
    }

}