package db.migration;

import com.example.demo.enums.Role;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class V2__Admin extends AbstractMigration {
    @Override
    protected void migrate(NamedParameterJdbcTemplate template) {
        insertAdminUser(template);
        Long userId = getAdminUserId(template);
        insertAdminUserRoles(userId, template);
    }

    private void insertAdminUser(NamedParameterJdbcTemplate template) {
        String query = "INSERT INTO users (username, password, active, name, email) " +
                "VALUES (:username, :password, :active, :name, :email)";
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("username", "admin")
                .addValue("password", encodePassword("admin"))
                .addValue("active", true)
                .addValue("name", "Администратор")
                .addValue("email", "admin@demo.example.com");

        template.update(query, parameters);
    }

    private String encodePassword(String password) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        return passwordEncoder.encode(password);
    }

    public Long getAdminUserId(NamedParameterJdbcTemplate template) {
        String query = "SELECT id FROM users WHERE username = :username";
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("username", "admin");

        return template.queryForObject(query, parameters, Long.class);
    }

    private void insertAdminUserRoles(Long userId, NamedParameterJdbcTemplate template) {
        String query = "INSERT INTO user_roles (user_id, role) VALUES (:user_id, :role)";

        Role[] roles = {Role.ROLE_USER, Role.ROLE_ADMIN};
        for (Role role : roles) {
            SqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("user_id", userId)
                    .addValue("role", role.name());
            template.update(query, parameters);
        }
    }
}
