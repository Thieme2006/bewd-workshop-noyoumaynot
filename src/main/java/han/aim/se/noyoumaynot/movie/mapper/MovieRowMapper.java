package han.aim.se.noyoumaynot.movie.mapper;

import han.aim.se.noyoumaynot.movie.domain.Movie;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MovieRowMapper implements RowMapper<Movie> {
    @Override
    public Movie mapRow(ResultSet rs, int rowNum) throws SQLException {
        Movie m = new Movie();
        m.setMovie_id(rs.getString("movie_id"));
        m.setName(rs.getString("name"));
        return m;
    }
}
