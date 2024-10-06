package han.aim.se.noyoumaynot.movie.mapper;

import han.aim.se.noyoumaynot.movie.domain.Movie;
import han.aim.se.noyoumaynot.movie.encryptor.JascryptConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MovieRowMapper implements RowMapper<Movie> {
    @Autowired
    private JascryptConfig jascryptConfig;
    @Override
    public Movie mapRow(ResultSet rs, int rowNum) throws SQLException {
        Movie m = new Movie();
        
        m.setMovie_id(rs.getString("movie_id"));
        m.setName(rs.getString("name"));
        return m;
    }
}
