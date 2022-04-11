package com.example.store.util;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserSessionMessage implements Message {

    private String username;

    private String sessionId;


}
