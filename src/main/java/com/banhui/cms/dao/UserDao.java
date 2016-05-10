package com.banhui.cms.dao;

import java.util.Date;

import com.banhui.cms.entities.User;

public interface UserDao {
	
	/**
	 * 
	 * 
	 * @param loginName
	 *        
	 * @param password
	 *        
	 * @param mobile
	 *        
	 * @param email
	 *        
	 * @return
	 *   sucess(return user_id) failed(return 0)
	 */
    int createUser(final String loginName, final String password, final String mobile, final String email);

    /**
     * 
     * 
     * @param loginName
     *        
     * @return
     *      exist(return true),not exist(return false)
     */
    boolean isExistsUserByLoginName(final String loginName);
    
    /**
     * 
     * 
     * @param mobile
     *        
     * @return
     *      exist(return true),not exist(return false) 
     */
    boolean isExistsUserByMobile(final String mobile);
    
    /**
     * 
     * 
     * @param email
     *       
     * @return
     *     exist(return true),not exist(return false) 
     */
    boolean isExistUserByEmail(final String email);
    
    /**
     * 
     * 
     * @param userId
     *       
     * @return
     *    
     */
    User queryUserById(final long userId);
    
    /**
     * 
     * 
     * @param loginName
     *        
     * @return
     * 
     */
    long queryUserIdByLoginName(final String loginName);
    
    /**
     * 
     * 
     * @param userId
     *        
     * @param password
     *        
     * @param updateTime
     *        
     * @return
     *
     */
    int updateUserPwdById(final long userId, final String password, final Date updateTime);
    
    /**
     * 
     * 
     * @param userId
     *       
     * @param newPassword
     *         
     * @param oldPassword
     *        
     * @param updateTime
     *        
     * @return
     * 
     */
    int updateUserPwdById(final long userId, final String newPassword, final String oldPassword, final Date updateTime);
    /**
     *
     * 
     * @param userId
     *        
     * @param mobile
     *        
     * @param updateTime
     *        
     * @return
     * 
     */
    int updateUserMobileById(final long userId, final String mobile, final Date updateTime);
    
    /**
     * 
     * 
     * @param userId
     *        
     * @param email
     *        
     * @param updateTime
     *        
     * @return
     * 
     */
    int updateUserEmailById(final long userId, final String email, final Date updateTime);
    
    /**
     * 
     * @param loginNameOrMobile
     * 
     * @param password
     * 
     * @return
     *  success(return user_id),fail(return 0)
     */
    long authenticate(final String loginNameOrMobile, final String password);
}
