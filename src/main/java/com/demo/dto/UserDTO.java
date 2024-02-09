package com.demo.dto;

import java.util.Base64;

public class UserDTO {
    public enum Role {
        DEFAULT_USER,
        VIP_USER,
        ADMIN;

        public static Role fromString(String enumString) {
            switch (enumString.toUpperCase()) {
                case "ADMIN": return ADMIN;
                case "VIP_USER": return VIP_USER;
                case "DEFAULT_USER":
                default:          return DEFAULT_USER;
            }
        }

        public static Role fromBase64String(String encodedEnumString) {
            String enumString = Base64.getDecoder().decode(encodedEnumString).toString();
            switch (enumString.toUpperCase()) {
                case "ADMIN": return ADMIN;
                case "VIP_USER": return VIP_USER;
                case "DEFAULT_USER":
                default:          return DEFAULT_USER;
            }
        }
    }

    private Role role;
    private String username;
    private String email;
    private String password;
    private long id;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UserDTO(){}

    public UserDTO(Role role, String username, String email, String password, long id) {
        this.role = role;
        this.username = username;
        this.email = email;
        this.password = password;
        this.id = id;
    }
}
