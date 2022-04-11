package com.example.store.repository;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class InMemoryAuthenticatedUsersRepository
        implements AuthenticatedUsersRepository {

    private final BidiMap<String, String> userSessionIdMap = new DualHashBidiMap<>();

    @Override
    public void addUser(String username, String sessionId) {
        userSessionIdMap.put(username, sessionId);
    }

    @Override
    public void removeUser(String username) {
        userSessionIdMap.remove(username);
    }

    @Override
    public boolean containsUser(String username) {
        return userSessionIdMap.containsKey(username);
    }

    @Override
    public boolean containsSessionId(String sessionId) {
        return userSessionIdMap.containsValue(sessionId);
    }

    @Override
    public String getUserBySessionId(String sessionId) {
        return userSessionIdMap.getKey(sessionId);
    }
}
