package han.aim.se.noyoumaynot.movie.mapper;

import han.aim.se.noyoumaynot.movie.other.HashObject;
import han.aim.se.noyoumaynot.movie.other.User;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HashRowMapper implements RowMapper<HashObject> {
    @Override
    public HashObject mapRow(ResultSet rs, int rowNum) throws SQLException {
        String hid = rs.getString("hid");
        String salt = rs.getString("salt");

        HashObject hido = new HashObject(hid, salt);

       return hido;
    }
}
