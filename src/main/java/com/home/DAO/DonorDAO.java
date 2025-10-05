package com.home.DAO;

import com.home.model.Donor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class DonorDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final class DonorRowMapper implements RowMapper<Donor> {
        @Override
        public Donor mapRow(ResultSet rs, int rowNum) throws SQLException {
            Donor donor = new Donor();
            donor.setId(rs.getLong("id"));
            donor.setUsername(rs.getString("username"));
            donor.setPassword(rs.getString("password"));
            donor.setFirstName(rs.getString("first_name"));
            donor.setLastName(rs.getString("last_name"));
            donor.setEmail(rs.getString("email"));
            donor.setPhone(rs.getString("phone"));
            donor.setAge(rs.getInt("age"));
            donor.setWeight(rs.getDouble("weight"));
            donor.setGender(rs.getString("gender"));
            donor.setBloodType(rs.getString("blood_type"));
            
            donor.setCity(rs.getString("city"));
            donor.setState(rs.getString("state"));
            
            
            donor.setTermsAccepted(rs.getBoolean("terms_accepted"));
            donor.setNotificationsEnabled(rs.getBoolean("notifications_enabled"));
            donor.setEnabled(rs.getBoolean("enabled"));
            
            
            
            return donor;
        }
    }

    public void save(Donor donor) {
        String sql = "INSERT INTO donor (username, password, first_name, last_name, email, phone, " +
                     "age, weight, gender, blood_type, city,state,terms_accepted, " +
                     "notifications_enabled, enabled) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?)";
        
        jdbcTemplate.update(sql,
            donor.getUsername(),
            donor.getPassword(),
            donor.getFirstName(),
            donor.getLastName(),
            donor.getEmail(),
            donor.getPhone(),
            donor.getAge(),
            donor.getWeight(),
            donor.getGender(),
            donor.getBloodType(),
            
            donor.getCity(),
            donor.getState(),
            donor.getTermsAccepted(),
            donor.getNotificationsEnabled(),
            donor.getEnabled() != null ? donor.getEnabled() : true
        );
    }

    public Donor findByUsername(String username) {
        String sql = "SELECT * FROM donor WHERE username = ?";
        List<Donor> donors = jdbcTemplate.query(sql, new DonorRowMapper(), username);
        return donors.isEmpty() ? null : donors.get(0);
    }

    public Donor findByEmail(String email) {
        String sql = "SELECT * FROM donor WHERE email = ?";
        List<Donor> donors = jdbcTemplate.query(sql, new DonorRowMapper(), email);
        return donors.isEmpty() ? null : donors.get(0);
    }

    public List<Donor> findByBloodType(String bloodType) {
        String sql = "SELECT * FROM donor WHERE blood_type = ? AND enabled = true";
        return jdbcTemplate.query(sql, new DonorRowMapper(), bloodType);
    }
    public List<Donor> findByBloodTypeAndLocation(String bloodType, String city, String state) {
        String sql = "SELECT * FROM donor WHERE blood_type = ? AND LOWER(city) = LOWER(?) AND LOWER(state) = LOWER(?)";
        return jdbcTemplate.query(sql, new DonorRowMapper(), bloodType, city, state);
    }
    public List<Donor> findAll() {
        String sql = "SELECT * FROM donor";
        return jdbcTemplate.query(sql, new DonorRowMapper());
    }
}