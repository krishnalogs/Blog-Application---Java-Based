package com.blogApplication.repository;

import com.blogApplication.model.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import org.springframework.lang.NonNull;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @NonNull
    private static final RowMapper<User> USER_ROW_MAPPER = new RowMapper<User>() {
        @Override
        public User mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
            User u = new User();
            u.setId(rs.getInt("id"));
            u.setUsername(rs.getString("username"));
            u.setEmail(rs.getString("email"));
            u.setPassword(rs.getString("password"));
            return u;
        }
    };

    public Optional<User> findByUsername(String username) {
        try {
            User u = jdbcTemplate.queryForObject(
                    "SELECT id, username, email, password FROM users WHERE username = ?",
                    USER_ROW_MAPPER,
                    username);
            return Optional.ofNullable(u);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public boolean existsByUsername(String username) {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(1) FROM users WHERE username = ?", Integer.class, username);
        return count != null && count > 0;
    }

    public boolean existsByEmail(String email) {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(1) FROM users WHERE email = ?", Integer.class, email);
        return count != null && count > 0;
    }

    public int save(User userWithEncodedPassword) {
        return jdbcTemplate.update(
                "INSERT INTO users (username, email, password) VALUES (?, ?, ?)",
                userWithEncodedPassword.getUsername(),
                userWithEncodedPassword.getEmail(),
                userWithEncodedPassword.getPassword()
        );
    }
}
