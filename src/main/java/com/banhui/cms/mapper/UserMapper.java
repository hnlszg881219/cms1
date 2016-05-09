package com.banhui.cms.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.banhui.cms.entities.User;

public class UserMapper implements RowMapper<User> {

	public User mapRow(ResultSet rs, int rowNum) throws SQLException {
		if(rowNum<0){
			return null;
		}
		User user = new User();
		user.setUserId(rs.getLong("user_id"));
		user.setLoginName(rs.getString("login_name"));
		user.setMobile(rs.getString("mobile"));
		user.setEmail(rs.getString("email"));
		user.setCreateTime(rs.getDate("create_time"));
		user.setUpdateTime(rs.getDate("update_time"));
		return user;
	}

}
