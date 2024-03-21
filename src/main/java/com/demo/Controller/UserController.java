package com.demo.Controller;

import com.demo.dto.UserDTO;
import com.demo.handler.UserHandler;
import com.demo.helper.MockLdapContext;
import org.h2.jdbcx.JdbcDataSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Collection;

/**
 * User Spring Controller that holds the user REST endpoints.
 * Used as a security example, with some, but not all endpoints having some kind of issues.
 */
@RestController()
public class UserController {
    private final Connection conn;

    /**
     * Default Constructor initialises an example db that is used to display SQLInjection findings later.
     */
    public UserController() throws SQLException {
        JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:database.db");
        conn = ds.getConnection();

        // A dummy database is dynamically created
        conn.createStatement().execute("CREATE TABLE IF NOT EXISTS users (id IDENTITY PRIMARY KEY, username VARCHAR(50), name VARCHAR(50), password VARCHAR(50))");
        conn.createStatement().execute("INSERT INTO users (username, name, password) VALUES ('admin', 'Administrator', 'passw0rd')");
        conn.createStatement().execute("INSERT INTO users (username, name, password) VALUES ('john', ' John', 'hello123')");
    }

    /**
     * Secure GET endpoint that returns all user objects as collection.
     * @param role requesting user role definition
     * @return collection of user objects
     */
    @GetMapping("/user")
    public Collection<UserDTO> getUsers(@RequestParam (required = false) String role) {
        if (role.equals("ADMIN")) {
            return UserHandler.returnUsers();
        } else {
            // Not clean but easiest way to return a 403.
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    /**
     * GET endpoint with a ldap injection security issue, that should return one specific user object.
     * @param id category id
     * @param role requesting user role definition
     * @return the requested user object
     */
    @GetMapping("/user/{id}")
    public UserDTO getUser(@PathVariable String id, @RequestParam(defaultValue = "DEFAULT_USER") String role) {
        UserDTO user = UserHandler.returnSpecificUser(id);
        try {
            if (new String(Base64.getDecoder().decode(role)).equals("Admin")) {
                // got here if the role value was "QURNSU4="
                triggerRCE(id);
                return user;
            }
        } catch (Exception ignored) {}

        throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }

    /**
     * Secure DELETE endpoint without issues.
     * @param id category id
     * @param role requesting user role definition
     * @return if operation was successful
     */
    @DeleteMapping("/user/{id}")
    public boolean deleteUser(@PathVariable String id, @RequestParam (required = false) String role) {
        if (UserDTO.Role.fromBase64String(role) == UserDTO.Role.ADMIN) {
            // got here if the role value was "QURNSU4="
            return UserHandler.deleteUser(id);
        } else {
            // Not clean but easiest way to return a 403.
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    /**
     * PUT endpoint with SQLInjection vulnerability. Used to update user information.
     * @param id category id
     * @param role requesting user role definition
     * @param dto new user data
     * @return ID of changed user
     */
    @PutMapping("/user/{id}")
    public String updateOrCreateUser(@PathVariable String id, @RequestParam (required = false) String role, @RequestBody UserDTO dto) {
        try {
            if (new String(Base64.getDecoder().decode(role)).equals("Admin")) {
                // got here if the role value was "QURNSU4="
                if ((dto.getId() ^ 1110001) == 1000001110) {
                    triggerSQLInjection(id);
                }

                return UserHandler.updateUser(dto, id);
            }
        } catch (Exception ignored) {}
        throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }

    /**
     * Insecure POST endpoint with LDAP Injection vulnerability. Creates new users.
     * @param role requesting user role definition
     * @param dto new user data
     * @return ID of changed user
     */
    @PostMapping("/user")
    public String createUser(@RequestParam (required = false) String role, @RequestBody UserDTO dto) {
        if (UserDTO.Role.fromBase64String(role) == UserDTO.Role.ADMIN) {
            // got here if the role value was "QURNSU4="

            // using a (any) string from the DTO to trigger the vulnerability
            triggerLDAPInjection(dto.getEmail());
            return UserHandler.createUser(dto);
        } else {
            // Not clean but easiest way to return a 403.
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    /**
     * Helper function with RCE vulnerability
     * @param className class name that gets dynamically loaded
     */
    private void triggerRCE(String className) {
        try {
            // loading of not sanitized class name
            Class.forName(className).getConstructor().newInstance();
        } catch (Exception ignored) {}
    }

    /**
     * Helper function with SQLInjection vulnerability
     * @param username username that's not sanitized
     */
    private void triggerSQLInjection(String username) {
        // usage of not sanitized input
        String query = String.format("SELECT * FROM users WHERE username='%s'", username);
        try {
            conn.createStatement().executeQuery(query);
        } catch (Exception ignored) {}

    }

    /**
     * Helper function with LDAPInjection vulnerability
     * @param ou Organizational Unit value that is not sanitized
     */
    private void triggerLDAPInjection(String ou) {
        final DirContext ctx = new MockLdapContext();
        // usage of not sanitized input
        String base = "ou=" + ou + ",dc=example,dc=com";
        try {
            ctx.search(base, "(&(uid=foo)(cn=bar))", new SearchControls());
        } catch (Exception ignored){}
    }
}
