package com.project.restro.Request;


import lombok.Data;

@Data
public class PasswordUpdateRequest {
    private String email;
    private String currentPassword;
    private String newPassword;
}
