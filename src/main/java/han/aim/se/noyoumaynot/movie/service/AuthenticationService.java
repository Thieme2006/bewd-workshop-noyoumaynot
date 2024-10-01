package han.aim.se.noyoumaynot.movie.service;

import han.aim.se.noyoumaynot.movie.other.Role;
import han.aim.se.noyoumaynot.movie.other.User;
import han.aim.se.noyoumaynot.movie.repository.UserToken;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthenticationService {
  private Map<String, User> users = new HashMap<>();

  ArrayList<UserToken> userTokens = new ArrayList<>();



  private String username = "Ghost_Unknown";
  private String password = "Testing12342";

  public AuthenticationService() {
    Role adminRole = new Role("admin", true);
    Role userRole = new Role("user", false);

    User admin = new User("Bert", "bolleharry", adminRole);
    User endUser = new User("Jeman", "Bollekanes", userRole);

    users.put(admin.getUsername(), admin);
    users.put(endUser.getUsername(), endUser);
  }

  public UserToken login(String username, String password) {
    User user = users.get(username);
    if(user != null && user.getPassword().equals(password)) {
      UserToken token = new UserToken(user.getUsername());
      userTokens.add(token);
      return token;
    }
    return null;
  }

  public boolean isValidToken(String token) {
    if(token == null || token.isEmpty()) {
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
     if(users.containsKey(username)){
       User user = users.get(username);
       return user.getRole().isBeheerder();
     }
     return false;
  }
}
