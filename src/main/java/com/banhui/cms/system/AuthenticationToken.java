package com.banhui.cms.system;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class AuthenticationToken {
	
	/**
	 * 
	 */
	private String userId;
	
	private Map<String,Object> sessionInfo;
	
	
	public AuthenticationToken() {
		
	}
	
	public AuthenticationToken(String userId) {
		this.userId = userId;
	}

	public boolean isGuest(){
		if(StringUtils.isEmpty(userId)){
			return false;
		}
		return true;
	}

	public Map<String, Object> getSessionInfo() {
		return sessionInfo;
	}

	public void setSessionInfo(Map<String, Object> sessionInfo) {
		this.sessionInfo = sessionInfo;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
