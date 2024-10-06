package han.aim.se.noyoumaynot.movie.intialiser;

import han.aim.se.noyoumaynot.movie.encryptor.JascryptConfig;
import han.aim.se.noyoumaynot.movie.hasher.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Component
public class UserDataInitializer implements CommandLineRunner {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private JascryptConfig jascryptConfig;

    @Override
    public void run(String... args) throws Exception {
        generateHashedUser("Bert", "134562", "admin");
        generateHashedUser("Gert", "q42536twa", "user");
        generateMovies("1", "Dune");
        generateMovies("2", "Dune 2");
        generateMovies("3", "Dune 3");
        generateMovies("4", "Dune 4");
        generateMovies("5", "Dune 5");
    }

    private void generateHashedUser(String username, String password, String role) {
        try {
            String SQL = "INSERT INTO user_Table (username, password, role) VALUES (?, ?, ?)";
            String SQLS = "INSERT INTO HIDF (hid, salt) VALUES (?, ?)";

            String salt = PasswordUtil.generateSalt();
            String hashedPassword = PasswordUtil.hashPassword(password, salt);

            String encryptedPassword = jascryptConfig.encryptor().encrypt(hashedPassword);
            String encryptedSalt = jascryptConfig.encryptor().encrypt(salt);

            jdbcTemplate.update(SQL, username, encryptedPassword, role);
            jdbcTemplate.update(SQLS, username, encryptedSalt);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    private void generateMovies(String movie_id, String name) {
        String SQL = "INSERT INTO Movie (movie_id, name) VALUES (?, ?)";

        String encryptedMovie_id = jascryptConfig.encryptor().encrypt(movie_id);
        String encryptedMovie_name = jascryptConfig.encryptor().encrypt(name);

        jdbcTemplate.update(SQL, encryptedMovie_id, encryptedMovie_name);
    }
}

