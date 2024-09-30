package han.aim.se.noyoumaynot.movie.controller;

import han.aim.se.noyoumaynot.movie.domain.Movie;
import han.aim.se.noyoumaynot.movie.service.AuthenticationService;
import han.aim.se.noyoumaynot.movie.service.MovieService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.util.ArrayList;

@RestController
@RequestMapping("/movies")
public class MovieController {
    private final MovieService movieService;
    private final AuthenticationService authenticationService;
    private String username = "Ghost_Unknown";
    private String password = "Testing12342";

    @Autowired
    public MovieController(MovieService movieService, AuthenticationService authenticationService) {
        this.movieService = movieService;
        this.authenticationService = authenticationService;
    }

    @GetMapping
    public ArrayList<Movie> getAllMovies(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        authenticate(token);
        return movieService.getMovieList();
    }

    @GetMapping("/show")
    public Movie getMovieById(@RequestParam("id") String id, HttpServletRequest request) {
            String token = request.getHeader("Authorization");
        authenticate(token);
        Movie movie = movieService.getMovieById(id);
            return movie;
    }

    @PostMapping("/add")
    public Movie addMovie(@RequestBody Movie movie, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        authenticate(token);

        movieService.insertMovie(movie);
        return movie;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteMovie(@PathVariable("id") String id, HttpServletRequest request) throws Exception {
        String token = request.getHeader("Authorization");
        authenticate(token);
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
