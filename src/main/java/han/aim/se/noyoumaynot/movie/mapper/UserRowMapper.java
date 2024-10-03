package han.aim.se.noyoumaynot.movie.mapper;

import han.aim.se.noyoumaynot.movie.other.Role;
import han.aim.se.noyoumaynot.movie.other.User;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        // Assuming your User table has columns: username, password, role
        String username = rs.getString("username");
        String password = rs.getString("password");
        String roleName = rs.getString("role");

        // Map role based on database value (adjust this based on your structure)
        Role role = new Role(roleName, "admin".equalsIgnoreCase(roleName));
        return new User(username, password, role);
    }
}
