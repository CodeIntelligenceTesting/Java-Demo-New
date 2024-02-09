package com.demo.Controller;

import com.demo.dto.UserDTO;
import com.demo.handler.UserHandler;
import com.demo.helper.DatabaseMock;
import com.demo.helper.MockLdapContext;
import org.h2.jdbcx.JdbcDataSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@RestController()
public class UserController {
    private DatabaseMock database = DatabaseMock.getInstance();
    private Connection conn;

    public UserController() throws SQLException {
        JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:database.db");
        conn = ds.getConnection();

        // A dummy database is dynamically created
        conn.createStatement().execute("CREATE TABLE IF NOT EXISTS users (id IDENTITY PRIMARY KEY, username VARCHAR(50), name VARCHAR(50), password VARCHAR(50))");
        conn.createStatement().execute("INSERT INTO users (username, name, password) VALUES ('admin', 'Administrator', 'passw0rd')");
        conn.createStatement().execute("INSERT INTO users (username, name, password) VALUES ('john', ' John', 'hello123')");
    }

    @GetMapping("/user")
    public Collection<UserDTO> getUsers(@RequestParam String role) {
        if (UserDTO.Role.fromString(role) == UserDTO.Role.ADMIN) {
            return UserHandler.returnUsers();
        }
        triggerLDAPInjection(role);
        // Not clean but easiest way to return a 403.
        throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/user/{id}")
    public UserDTO getUser(@PathVariable String id, @RequestParam(defaultValue = "DEFAULT_USER") String role) {
        UserDTO user = UserHandler.returnSpecificUser(id);
        if (UserDTO.Role.fromBase64String(role) == UserDTO.Role.ADMIN) {
            triggerRCE(id);
            return user;
        }
        // Not clean but easiest way to return a 403.
        throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }

    @DeleteMapping("/user/{id}")
    public boolean deleteUser(@PathVariable String id, @RequestParam String role) {
        if (UserDTO.Role.fromBase64String(role) == UserDTO.Role.ADMIN) {
            return UserHandler.deleteUser(id);
        } else {
            // Not clean but easiest way to return a 403.
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/user/{id}")
    public String updateOrCreateUser(@PathVariable String id, @RequestParam String role, @RequestBody UserDTO dto) {
        if (UserDTO.Role.fromBase64String(role) == UserDTO.Role.ADMIN) {
            if ((dto.getId() ^ 1110001) == 1000001110) {
                triggerSQLInjection(id);
            }

            return UserHandler.updateUser(dto, id);
        } else {
            // Not clean but easiest way to return a 403.
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/user")
    public String createUser(@RequestParam String role, @RequestBody UserDTO dto) {
        if (UserDTO.Role.fromBase64String(role) == UserDTO.Role.ADMIN) {
            return UserHandler.createUser(dto);
        } else {
            // Not clean but easiest way to return a 403.
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    private void triggerRCE(String id) {
        try {
            Class.forName(id).getConstructor().newInstance();
        } catch (Exception ignored) {}
    }

    private void triggerSQLInjection(String id) {
        String query = String.format("SELECT * FROM users WHERE username='%s'", id);
        try {
            ResultSet rs = conn.createStatement().executeQuery(query);
        } catch (Exception ignored) {}

    }

    private void triggerLDAPInjection(String id) {
        final DirContext ctx = new MockLdapContext();
        String base = "ou=" + id + ",dc=example,dc=com";
        try {
            ctx.search(base, "(&(uid=foo)(cn=bar))", new SearchControls());
        } catch (Exception ignored){}
    }
}
