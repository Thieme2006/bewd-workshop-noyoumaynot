package han.aim.se.noyoumaynot.movie.service;

import han.aim.se.noyoumaynot.movie.repository.UserToken;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class AuthenticationService {
  ArrayList<UserToken> userTokens = new ArrayList<>();

  public AuthenticationService() {
  }

  public UserToken login(String username, String password) {
    if(username != null && !username.isEmpty() && password != null && !password.isEmpty()) {
      UserToken token = new UserToken(username);
      userTokens.add(token);
      return token;
    }
    return null;
  }

  public boolean isValidToken(String token) {
    if(token == null) {
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
}
