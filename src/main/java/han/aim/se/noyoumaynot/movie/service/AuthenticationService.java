package han.aim.se.noyoumaynot.movie.service;

import han.aim.se.noyoumaynot.movie.repository.UserToken;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AuthenticationService {
  ArrayList<UserToken> userTokens = new ArrayList<>();

  public UserToken login(String username, String password) {
    return null;
  }

  public boolean isValidToken(String token) {
    return true;
  }

  public String getUsername(String token) {
    return null;
  }
}
