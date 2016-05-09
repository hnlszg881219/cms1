package com.banhui.cms.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Date;

import org.joda.time.DateTime;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.dbunit.annotation.ExpectedDataSet;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBeanByType;

import com.banhui.cms.dao.UserDao;
import com.banhui.cms.entities.User;

@SpringApplicationContext({"root_content.xml"})
public class UserDaoImplTest extends UnitilsJUnit4{
	
	@SpringBeanByType
	private UserDao userDao;
	
	@Test
	@DataSet("/user.xml")
	public void testIsExistsUserByEmail() throws Exception{
		String email = "395732330@qq.com";
		assertEquals(true, userDao.isExistUserByEmail(email));
		email = "295732331@qq.com";
		assertEquals(false, userDao.isExistUserByEmail(email));
	}
	
	@Test
	@DataSet("/user.xml")
	public void testQueryUserById() throws Exception{
		User user = userDao.queryUserById(1);
		assertNotNull(user);
		user = userDao.queryUserById(3);
		assertNull(user);
	}
	
	@Test
	@DataSet("/user.xml")
	public void testIsExistsUserByLoginName() throws Exception{
	    String loginName = "admin";
	    assertEquals(true, userDao.isExistsUserByLoginName(loginName));
	    loginName = "admin2";
	    assertEquals(false, userDao.isExistsUserByLoginName(loginName));
	}
	
	@Test
	@DataSet("/user.xml")
	public void testIsExistsUserByMobile() throws Exception{
	    String mobile = "12345678901";
	    assertEquals(true, userDao.isExistsUserByMobile(mobile));
	    mobile = "212345678901";
	    assertEquals(false, userDao.isExistsUserByMobile(mobile));
	}
	
	
	
	@Test
	@DataSet("/user.xml")
	public void testQueryUserIdByLoginName() throws Exception{
	   final long userId = 1L;
	   String loginName = "admin";
	   assertEquals(userId, userDao.queryUserIdByLoginName(loginName));
	   loginName = "admin1";
	   assertNotEquals(userId, userDao.queryUserIdByLoginName(loginName));
	}
	
	@Test
	@DataSet("/user.xml")
	@ExpectedDataSet("/excepted_user.xml")
	public void updateUserEmailById() throws Exception{
		final long userId = 1L;
		final String email = "395732331@qq.com";
		final Date updateTime = new DateTime(2016, 12, 13, 12,12,12).toDate();
		userDao.updateUserEmailById(userId, email, updateTime);
	}
	
	@Test
	@DataSet("/user.xml")
	public void testAuthenticate() throws Exception{
		final String loginName = "admin";
		final String password = "12345678";
		final String mobile = "12345678901";
		
		assertEquals(1L, userDao.authenticate(loginName, password));
		assertEquals(1L, userDao.authenticate(mobile, password));
	}
	
	@Test
	@DataSet("/user.xml")
	@ExpectedDataSet("/excepted1_user.xml")
	public void testUpdateUserPwdById()throws Exception{
		final long userId = 1L;
		final String password = "395732330";
		final Date updateTime = new DateTime(2016, 12, 13, 12,12,12).toDate();
		userDao.updateUserPwdById(userId, password, updateTime);
	}
	
	@Test
	@DataSet("/user.xml")
	@ExpectedDataSet("/excepted2_user.xml")
	public void testupdateUserMobileById() throws Exception{
		final long userId = 1L;
		final String mobile = "18918102624";
		final Date updateTime = new DateTime(2016, 12, 13, 12,12,12).toDate();
		userDao.updateUserMobileById(userId, mobile, updateTime);
	}
	
}
