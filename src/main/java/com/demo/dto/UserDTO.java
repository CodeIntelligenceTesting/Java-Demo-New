package com.demo.dto;

import java.util.Base64;

/**
 * DTO object with only secondary level of interest.
 * Only {@link Role#fromString(String)} and {@link Role#fromBase64String(String)} are interesting for their use
 * as condition guards for the vulnerabilities and issues.
 */
public class UserDTO {
    public enum Role {
        DEFAULT_USER,
        VIP_USER,
        ADMIN;

        /**
         * Function that maps input string to role object
         * @param enumString input to map to role object
         * @return role object
         */
        public static Role fromString(String enumString) {
            if (enumString.equalsIgnoreCase("ADMIN")) {
                return ADMIN;
            } else if (enumString.equalsIgnoreCase("VIP_USER")) {
                return VIP_USER;
            }
            return DEFAULT_USER;
        }

        /**
         * Function that maps base64 encoded input string to role object
         * @param encodedEnumString encoded input to map to role object
         * @return role object
         */
        public static Role fromBase64String(String encodedEnumString) {
            try {
                String enumString = new String(Base64.getDecoder().decode(encodedEnumString));
                if (enumString.equalsIgnoreCase("ADMIN")) {
                    // got here if the role value was "QURNSU4="
                    return ADMIN;
                } else if (enumString.equalsIgnoreCase("VIP_USER")) {
                    // got here if the role value was "VklQX1VTRVI="
                    return VIP_USER;
                }
            } catch (Exception ignored) {}
            return DEFAULT_USER;
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
