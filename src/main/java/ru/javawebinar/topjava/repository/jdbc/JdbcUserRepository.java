package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);
    private static final ResultSetExtractor<List<User>> SET_EXTRACTOR = new UserResultSetExtractor();
    private static final String SQL_INSERT_USER_ROLES = "INSERT INTO user_roles (user_id, role) VALUES (?,?)";


    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
        BatchPreparedStatementSetter batchPreparedStatementSetter = new UserRolesBatchPreparedStatementSetter(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
            jdbcTemplate.batchUpdate(SQL_INSERT_USER_ROLES, batchPreparedStatementSetter);
            return user;
        } else if (namedParameterJdbcTemplate.update(
                "UPDATE users SET name=:name, email=:email, password=:password, " +
                        "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource) != 0
                ) {
            jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", user.getId());
            jdbcTemplate.batchUpdate(SQL_INSERT_USER_ROLES, batchPreparedStatementSetter);
            return user;
        }
        return null;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users u LEFT JOIN user_roles ur ON u.id = ur.user_id WHERE u.id=?", SET_EXTRACTOR, id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT * FROM users u LEFT JOIN user_roles ur ON u.id = ur.user_id WHERE email=?", SET_EXTRACTOR, email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT * FROM users u INNER JOIN user_roles r ON u.id = r.user_id ORDER BY u.name, u.email", SET_EXTRACTOR);
    }

    private static class UserResultSetExtractor implements ResultSetExtractor<List<User>> {
        @Override
        public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Integer, User> users = new HashMap<>();
            while (rs.next()) {
                User rsUser = users.get(rs.getInt("id"));
                if (rsUser != null) {
                    Role newRole = Enum.valueOf(Role.class, rs.getString("role"));
                    rsUser.getRoles().add(newRole);
                } else {
                    rsUser = new User();
                    rsUser.setId(rs.getInt("id"));
                    rsUser.setName(rs.getString("name"));
                    rsUser.setEmail(rs.getString("email"));
                    rsUser.setPassword(rs.getString("password"));
                    rsUser.setCaloriesPerDay(rs.getInt("calories_per_day"));
                    rsUser.setEnabled(rs.getBoolean("enabled"));
                    rsUser.setRegistered(rs.getDate("registered"));
                    if (rs.getString("role") != null) {
                        rsUser.setRoles(new HashSet<>() {{
                            add(Enum.valueOf(Role.class, rs.getString("role")));
                        }});
                    }
                    users.put(rsUser.getId(), rsUser);
                }
            }
            return new ArrayList<>(users.values());
        }
    }

    private class UserRolesBatchPreparedStatementSetter implements BatchPreparedStatementSetter {
        private final User user;
        private final List<Role> roles;

        public UserRolesBatchPreparedStatementSetter(User user) {
            this.user = user;
            this.roles = new ArrayList<>(user.getRoles());
        }

        @Override
        public void setValues(PreparedStatement ps, int i) throws SQLException {
            ps.setInt(1, user.getId());
            ps.setString(2, roles.get(i).toString());
        }

        @Override
        public int getBatchSize() {
            return roles.size();
        }
    }
}
