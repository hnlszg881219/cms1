package com.banhui.cms.web;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.banhui.cms.dao.UserDao;
import com.banhui.cms.entities.User;
import com.banhui.cms.system.AuthenticationToken;

@Controller
@RequestMapping("/account")
public class AccountService {
	Logger logger = Logger.getLogger(AccountService.class);
	
	static Pattern LOGIN_NAME_PATTERN = Pattern.compile("^[0-9a-zA-Z_]{6,18}$", Pattern.CASE_INSENSITIVE);
	static Pattern PASSWORD_PATTERN = Pattern.compile("^[0-9a-zA-Z\u4E00-\u9FA5]{8,20}$", Pattern.CASE_INSENSITIVE);
	static Pattern MOBILE_PATTERN = Pattern.compile("^[1][3-8][0-9]{9}$", Pattern.CASE_INSENSITIVE);
	static Pattern ID_CARD_PATTERN = Pattern.compile("^[0-9]{15}|[0-9]{17}[0-9X]$",Pattern.CASE_INSENSITIVE);
	
	private UserDao userDao;
	
	@Autowired
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	@RequestMapping(path="/signIn", method = {RequestMethod.POST})
	@ResponseBody
	/**
	 * 登录
	 * 
	 * @param loginNameOrMobile
	 *        用户名或手机号
	 * @param password
	 *        密码
	 * @param session
	 * 
	 * @return
	 * 
	 */
	public Map<String,Object> signIn(@RequestParam("login_name_or_mobile") String loginNameOrMobile,
			@RequestParam("pwd") String password, HttpSession session){
		Map<String,Object> map = new HashMap<String,Object>();
		AuthenticationToken token = new AuthenticationToken();
		if(StringUtils.isEmpty(loginNameOrMobile)){
			map.put("message", "用户名或手机号不能为空!");
			map.put("success", false);
			return map;
		}
		
		if(StringUtils.isEmpty(password)){
			map.put("message", "密码不能为空!");
			map.put("success", false);
			return map;
		}
		
		final long userId = userDao.authenticate(loginNameOrMobile, hashPwd(password));
		if(userId>0){
			token.setUserId(userId+"");
			map.put("message", "登录成功 !");
			map.put("success", true);
		}else{
			map.put("success", false);
			map.put("message", "用户名或密码错误!");
		}
		session.setAttribute("token", token);
		return map;
	}
	
	@RequestMapping(path="/signOut", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> signOut(HttpSession session){
		Map<String,Object> map = new HashMap<String,Object>();
		session.removeAttribute("token");
		map.put("message", "登出成功!");
		map.put("success", true);
		return map;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	@ResponseBody
	public User queryUserInfo(HttpSession session){
		AuthenticationToken token = (AuthenticationToken) session.getAttribute("token");
		User user = new User();
		if(token == null || token.isGuest()){
			return user;
		}
		final long userId = NumberUtils.toLong(token.getUserId());
		return userDao.queryUserById(userId);
	}
	
	@RequestMapping(method = RequestMethod.PUT)
	@ResponseBody
	/**
	 * 创建用户
	 * 
	 * @param loginName
	 *        用户名
	 * @param password
	 *        密码
	 * @param mobile
	 *        手机号
	 * @param email
	 *        邮箱
	 * @return
	 *       成功返回userId
	 */
	public Map<String, Object> createUser(@RequestParam String loginName, 
			@RequestParam String password, @RequestParam String mobile, 
			@RequestParam String email){
		Map<String,Object> map = new HashMap<String,Object>();
		if (!LOGIN_NAME_PATTERN.matcher(loginName).matches()){
			map.put("success", false);
			map.put("message", "用户名只能为数字字母下划线且长度为6-18!");
			return map;
		}
		
		if (!PASSWORD_PATTERN.matcher(password).matches()){
			map.put("success", false);
			map.put("message", "密码只能为数字字母中文且长度为8-20!");
			return map;
		}
		
		if(!MOBILE_PATTERN.matcher(mobile).matches()){
			map.put("success", false);
			map.put("message", "手机号的格式不对");
			return map;
		}
		
		//判断登录名是否存在
		if(userDao.isExistsUserByLoginName(loginName)){
			map.put("success", false);
			map.put("message", "用户名已存在!");
			return map;
		}
		
		//判断手机号是否存在
		if(userDao.isExistsUserByMobile(mobile)){
			map.put("success", false);
			map.put("message", "手机号已存在!");
			return map;
		}
		
		//判断邮箱是否存在
		if(userDao.isExistUserByEmail(email)){
			map.put("success", false);
			map.put("message", "邮箱已存在!");
			return map;
		}
		
		final long userId  = userDao.createUser(loginName, hashPwd(password), mobile, email);
		if(userId>0){
			map.put("success", true);
			map.put("message", "创建成功!");
			map.put("userId", userId);
			return map;
		}
		map.put("success", false);
		map.put("message", "创建失败!");
		return map;
	}
	
	public static String hashPwd(String password){
		String salt = "abc";
		return  DigestUtils.sha1Hex(salt+password);
	}
	
	public static void main(String[] args){
		
	}

}
