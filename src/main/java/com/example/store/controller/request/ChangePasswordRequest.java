package com.example.store.controller.request;

import com.example.store.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class ChangePasswordRequest extends User {

    private String name;

    private String password;

    private String newPassword;

}
