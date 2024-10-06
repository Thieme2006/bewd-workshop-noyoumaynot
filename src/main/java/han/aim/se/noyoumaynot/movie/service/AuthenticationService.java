package han.aim.se.noyoumaynot.movie.service;

import han.aim.se.noyoumaynot.movie.encryptor.JascryptConfig;
import han.aim.se.noyoumaynot.movie.hasher.PasswordUtil;
import han.aim.se.noyoumaynot.movie.mapper.HashRowMapper;
import han.aim.se.noyoumaynot.movie.mapper.UserRowMapper;
import han.aim.se.noyoumaynot.movie.other.HashObject;
import han.aim.se.noyoumaynot.movie.other.Role;
import han.aim.se.noyoumaynot.movie.other.User;
import han.aim.se.noyoumaynot.movie.repository.UserToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthenticationService {
    private Map<String, User> users = new HashMap<>();
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private JascryptConfig jascryptConfig;

    ArrayList<UserToken> userTokens = new ArrayList<>();

    public AuthenticationService() {
        Role adminRole = new Role("admin", true);
        Role userRole = new Role("user", false);

        User admin = new User("Bert", "bolleharry", adminRole);
        User endUser = new User("Jeman", "Bollekanes", userRole);

        users.put(admin.getUsername(), admin);
        users.put(endUser.getUsername(), endUser);
    }

    public UserToken login(String username, String password) {
        try {
            String SQL = "SELECT * FROM user_Table WHERE username = ?";
            String SQLS = "SELECT salt FROM HIDF WHERE hid = (SELECT username FROM user_Table WHERE username = ?)";

            User user = jdbcTemplate.queryForObject(SQL, new Object[]{username}, new UserRowMapper());
            String salt = jdbcTemplate.queryForObject(SQLS, new Object[]{username}, String.class);

            if (user != null && salt != null) {
                String decryptedSalt = jascryptConfig.encryptor().decrypt(salt);

                String hashedPassword = PasswordUtil.hashPassword(password, decryptedSalt);

                String decryptedPassword = jascryptConfig.encryptor().decrypt(user.getPassword());

                if (hashedPassword.equals(decryptedPassword)) {
                    UserToken token = new UserToken(user.getUsername());
                    userTokens.add(token);
                    return token;
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public boolean isValidToken(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }
        return userTokens.stream().anyMatch(userToken -> userToken.getToken().equals(token));
    }

    public String getUsername(String token) {
        if (token.isEmpty() || token == null) {
            return null;
        }
        Optional<UserToken> userToken = userTokens.stream()
                .filter(ut -> ut.getToken().equals(token))
                .findFirst();

        return userToken.map(UserToken::getUsername).orElse(null);
    }

    public boolean isUserAdmin(String username) {
        try {
            String SQL = "SELECT role FROM user_Table WHERE username = ?";
            String role = jdbcTemplate.queryForObject(SQL, new Object[]{username}, String.class);
            if ("admin".equalsIgnoreCase(role)) {
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
