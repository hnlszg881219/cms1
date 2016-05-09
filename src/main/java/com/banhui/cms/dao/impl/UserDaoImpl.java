package com.banhui.cms.dao.impl;

import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.banhui.cms.dao.UserDao;
import com.banhui.cms.entities.User;
import com.banhui.cms.mapper.UserMapper;

@Repository("userDao")
public class UserDaoImpl implements UserDao {
	
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
		return namedParameterJdbcTemplate;
	}

	@Autowired
	public void setDataSource(DataSource dataSource) {
	   this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	
	public int createUser(final String loginName, final String password, 
			final String mobile, final String email) {
		final String sql = "insert into user(login_name, password, mobile, email, create_time)"
				+ "select :login_name, :password, :mobile, :email, :create_time"
				+ " where  not exists(select 1 from user where login_name = :login_name"
				+ " or mobile = :mobile or email = :email)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		String[] keyColumnNames = {"user_id"};
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("login_name", loginName);
		paramSource.addValue("password", password);
		paramSource.addValue("mobile", mobile);
		paramSource.addValue("email", email);
		paramSource.addValue("create_time", new Date());
		int c = namedParameterJdbcTemplate.update(sql, paramSource, keyHolder, keyColumnNames);
		if(c>0){
			return keyHolder.getKey().intValue();
		}
		return 0;
	}

	public boolean isExistsUserByLoginName(final String loginName) {
		final String sql = "select case when exists(select 1 from user "
				+ "where login_name = :login_name) then 1 else 0 end";
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("login_name", loginName);
		return namedParameterJdbcTemplate.queryForObject(sql, paramSource, Boolean.class);
	}

	public boolean isExistsUserByMobile(String mobile) {
		final String sql = "select case when exists(select 1 from user "
				+ "where mobile = :mobile) then 1 else 0 end";
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("mobile", mobile);
		return namedParameterJdbcTemplate.queryForObject(sql, paramSource, Boolean.class);
	}

	public boolean isExistUserByEmail(final String email) {
		final String sql = "select case when exists(select 1 from user "
				+ "where email = :email) then 1 else 0 end";
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("email", email);
		return namedParameterJdbcTemplate.queryForObject(sql, paramSource, Boolean.class);
	}

	public User queryUserById(final long userId) {
		final String sql = "select user_id, login_name, mobile, email, create_time, update_time"
				+ " from user where user_id = :user_id";
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("user_id", userId);
		List<User> list = namedParameterJdbcTemplate.query(sql, paramSource, new UserMapper());
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}

	public long queryUserIdByLoginName(String loginName) {
		final String sql = "select user_id from user where login_name = :login_name";
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("login_name", loginName);
		List<Long> list = namedParameterJdbcTemplate.queryForList(sql, paramSource, Long.class);
		if(list.size()>0){
			return list.get(0);
		}
		return 0;
	}

	public boolean updateUserPwdById(long userId, String password, final Date updateTime) {
		final String sql = "update user"
				+ " set password = :password,"
				+ " update_time = :update_time"
				+ " where user_id = :user_id";
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("user_id", userId);
		paramSource.addValue("password", password);
		paramSource.addValue("update_time", updateTime);
		return namedParameterJdbcTemplate.update(sql, paramSource)>0?true:false;
	}

	public boolean updateUserMobileById(long userId, String mobile, final Date updateTime) {
		final String sql = "update user"
				+ " set mobile = :mobile,"
				+ " update_time = :update_time"
				+ " where user_id = :user_id";
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("user_id", userId);
		paramSource.addValue("mobile", mobile);
		paramSource.addValue("update_time", updateTime);
		return namedParameterJdbcTemplate.update(sql, paramSource)>0?true:false;
	}

	public boolean updateUserEmailById(long userId, String email, final Date updateTime) {
		final String sql = "update user "
				+ " set email = :email,"
				+ " update_time = :update_time"
				+ " where user_id = :user_id";
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("user_id", userId);
		paramSource.addValue("email", email);
		paramSource.addValue("update_time", updateTime);
		return namedParameterJdbcTemplate.update(sql, paramSource)>0?true:false;
	}

	public boolean updateUserPwdById(long userId, String newPassword,
			String oldPassword, Date updateTime) {
		final String sql = "update user"
				+ " set password = :new_password,"
				+ " update_time = :update_time"
				+ " where user_id = :user_id"
				+ " and password = :old_password";
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("user_id", userId);
		paramSource.addValue("new_password", newPassword);
		paramSource.addValue("old_password", oldPassword);
		paramSource.addValue("update_time", updateTime);
		return namedParameterJdbcTemplate.update(sql, paramSource)>0?true:false;
	}

	public long authenticate(String loginNameOrMobile, String password) {
		final String sql = "select user_id from user"
				+ " where (login_name = :login_name_or_mobile or mobile = :login_name_or_mobile)"
				+ " and password = :password";
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("login_name_or_mobile", loginNameOrMobile);
		paramSource.addValue("password", password);
		List<Long> list = namedParameterJdbcTemplate.queryForList(sql, paramSource, Long.class);
		if(list.size()>0){
			return list.get(0);
		}
		return 0;
	}

}
