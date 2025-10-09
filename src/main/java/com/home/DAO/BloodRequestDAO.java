package com.home.DAO;

import com.home.model.BloodRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Repository
public class BloodRequestDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final class BloodRequestRowMapper implements RowMapper<BloodRequest> {
        @Override
        public BloodRequest mapRow(ResultSet rs, int rowNum) throws SQLException {
            BloodRequest request = new BloodRequest();
            request.setId(rs.getLong("id"));
            request.setPatientName(rs.getString("patient_name"));
            request.setRequiredBloodType(rs.getString("required_blood_type"));
            request.setPatientAge(rs.getObject("patient_age", Integer.class));
            request.setUrgencyLevel(rs.getString("urgency_level"));
            request.setHospital(rs.getString("hospital"));
            request.setHospitalCity(rs.getString("hospital_city"));
            request.setHospitalDistrict(rs.getString("hospital_district"));  // NEW
            request.setHospitalState(rs.getString("hospital_state"));
            request.setContactPerson(rs.getString("contact_person"));
            request.setContactPhone(rs.getString("contact_phone"));
            request.setContactEmail(rs.getString("contact_email"));
            return request;
        }
    }

    public Long save(BloodRequest request) {
        String sql = "INSERT INTO blood_request (patient_name, required_blood_type, patient_age, " +
                "urgency_level, hospital, hospital_city, hospital_district, hospital_state, " +
                "contact_person, contact_phone, contact_email) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, request.getPatientName());
            ps.setString(2, request.getRequiredBloodType());
            ps.setObject(3, request.getPatientAge());
            ps.setString(4, request.getUrgencyLevel());
            ps.setString(5, request.getHospital());
            ps.setString(6, request.getHospitalCity());
            ps.setString(7, request.getHospitalDistrict());  // NEW
            ps.setString(8, request.getHospitalState());
            ps.setString(9, request.getContactPerson());
            ps.setString(10, request.getContactPhone());
            ps.setString(11, request.getContactEmail());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public BloodRequest findById(Long id) {
        String sql = "SELECT * FROM blood_request WHERE id = ?";
        List<BloodRequest> requests = jdbcTemplate.query(sql, new BloodRequestRowMapper(), id);
        return requests.isEmpty() ? null : requests.get(0);
    }
}