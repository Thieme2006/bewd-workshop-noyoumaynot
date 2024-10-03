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
        System.out.print("Movies shown!");
        return movieService.getMovieList();
    }

        @GetMapping("/show")
        public Movie getMovieById(@RequestParam("id") String id, HttpServletRequest request, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorisation) throws Exception {
            // Optionally authenticate the user
            // authenticate(authorisation);

            // Query for a single movie using the movie_id
            try {
                String sql = "SELECT * FROM Movie WHERE movie_id = ?";


                // Use queryForObject to get a single movie
                Movie movie = jdbcTemplate.queryForObject(sql, new Object[]{id}, new MovieRowMapper());

                // Return the movie object
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
        movieService.insertMovie(movie);
        return movie;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteMovie(@PathVariable("id") String id, HttpServletRequest request, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorisation) throws Exception {
        var auth = authenticate(authorisation);
        if(!authenticationService.isUserAdmin(auth)) {
            throw new AuthenticationException("You are not authorized to add movie");
        }
        movieService.deleteMovie(id);
        return ResponseEntity.ok().build();
    }

    private String authenticate(String token) throws Exception {
        if (authenticationService.isValidToken(token)){
            return authenticationService.getUsername(token);
        } else {
            throw new AuthenticationException("Invalid token");
        }
    }


}
