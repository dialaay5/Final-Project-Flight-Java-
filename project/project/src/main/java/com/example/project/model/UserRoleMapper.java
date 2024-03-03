package com.example.project.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRoleMapper  implements RowMapper<UserRole> {

    @Override
    public UserRole mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new UserRole(
                rs.getInt("id"),
                UserRoleName.valueOf(rs.getString("role_name"))
        );
    }
}
