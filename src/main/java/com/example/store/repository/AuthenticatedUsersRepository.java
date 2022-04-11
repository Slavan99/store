package com.example.store.repository;

public interface AuthenticatedUsersRepository {

    void addUser(String username, String sessionId);

    void removeUser(String username);

    boolean containsUser(String username);

    boolean containsSessionId(String sessionId);

    String getUserBySessionId(String sessionId);

}
