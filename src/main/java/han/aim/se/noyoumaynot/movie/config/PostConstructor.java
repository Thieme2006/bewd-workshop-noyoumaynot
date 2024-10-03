package han.aim.se.noyoumaynot.movie.config;

import han.aim.se.noyoumaynot.movie.hasher.PasswordUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class PostConstructor {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @PostConstruct
    public void init() {
        generateHashedUser("Bert", "134562", "admin");
        generateHashedUser("Gert", "q42536twa", "user");
    }

    private void generateHashedUser(String username, String password, String role) {
        try {
            String SQL = "INSERT INTO user_Table (username, password, role) VALUES (?, ?, ?)";
            String SQLS = "INSERT INTO HIDF (hid, salt) VALUES (?, ?)";

            String adminSalt = PasswordUtil.generateSalt();
            String hashedPassword = PasswordUtil.hashPassword(password, adminSalt);

            jdbcTemplate.update(SQL, username, hashedPassword, role);
            jdbcTemplate.update(SQLS, username, adminSalt);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }
}
