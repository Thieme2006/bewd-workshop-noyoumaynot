package han.aim.se.noyoumaynot.movie.controller;

import han.aim.se.noyoumaynot.movie.domain.Movie;
import han.aim.se.noyoumaynot.movie.mapper.MovieRowMapper;
import han.aim.se.noyoumaynot.movie.repository.UserToken;
import han.aim.se.noyoumaynot.movie.service.AuthenticationService;
import han.aim.se.noyoumaynot.movie.service.MovieService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.naming.AuthenticationException;
import java.nio.channels.ScatteringByteChannel;
import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping("/movies")
public class MovieController {
    private final MovieService movieService;
    private final AuthenticationService authenticationService;
    private String username = "Ghost_Unknown";
    private String password = "Testing12342";
    public String token;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public MovieController(MovieService movieService, AuthenticationService authenticationService) {
        this.movieService = movieService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public String logUserIn(@RequestBody Map<String, String> bodyItems) throws AuthenticationException {
        var token = authenticationService.login(bodyItems.get("username"), bodyItems.get("password"));
        String tokenIndex;
        try {
           tokenIndex = token.getToken();
        } catch (Exception e) {
            throw new AuthenticationException("Invalid username or password");
        }
        this.token = tokenIndex;
        return this.token;
    }


    @GetMapping
    public ArrayList<Movie> getAllMovies(HttpServletRequest request, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorisation) throws Exception {
        authenticate(authorisation);

        String sql = "SELECT * FROM Movie";

        ArrayList<Movie> movies = (ArrayList<Movie>) jdbcTemplate.query(sql, new MovieRowMapper());

        System.out.print("Movies shown!");
        return movies;
    }

        @GetMapping("/show")
        public Movie getMovieById(@RequestParam("id") String id, HttpServletRequest request, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorisation) throws Exception {
             authenticate(authorisation);
             try {
                String sql = "SELECT * FROM Movie WHERE movie_id = ?";
                Movie movie = jdbcTemplate.queryForObject(sql, new Object[]{id}, new MovieRowMapper());
                return movie;
            } catch (EmptyResultDataAccessException e) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found or something else went wrong");
            }
        }

    @PostMapping("/add")
    public Movie addMovie(@RequestBody Movie movie, HttpServletRequest request, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorisation) throws Exception {
        var auth = authenticate(authorisation);
            if(!authenticationService.isUserAdmin(auth)) {
                throw new AuthenticationException("You are not authorized to add movie");
            }
        String SQL = "INSERT INTO Movie (movie_id, name) VALUES (?, ?)";
        var updatedRows = jdbcTemplate.update(SQL, movie.getMovie_id(), movie.getName());
        if(updatedRows > 0) {
            return movie;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteMovie(@PathVariable("id") String id, HttpServletRequest request, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorisation) throws Exception {
        var auth = authenticate(authorisation);
        if(!authenticationService.isUserAdmin(auth)) {
            throw new AuthenticationException("You are not authorized to add movie");
        }
        String SQL = "DELETE FROM Movie WHERE movie_id = ?";
        var numbOfRowsDeleted = jdbcTemplate.update(SQL, id);
        if (numbOfRowsDeleted > 0) {
            return ResponseEntity.ok().build();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found");
        }
    }

    private String authenticate(String token) throws Exception {
        if (authenticationService.isValidToken(token)){
            return authenticationService.getUsername(token);
        } else {
            throw new AuthenticationException("Invalid token");
        }
    }
}
