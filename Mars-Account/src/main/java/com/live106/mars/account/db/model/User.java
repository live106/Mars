package com.live106.mars.account.db.model;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {

	private static final long serialVersionUID = -5329577117529185266L;
	
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column user.id
	 * @mbggenerated  Wed Oct 28 11:51:13 CST 2015
	 */
	private Integer id;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column user.username
	 * @mbggenerated  Wed Oct 28 11:51:13 CST 2015
	 */
	private String username;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column user.password
	 * @mbggenerated  Wed Oct 28 11:51:13 CST 2015
	 */
	private String password;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column user.createday
	 * @mbggenerated  Wed Oct 28 11:51:13 CST 2015
	 */
	private Date createday;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column user.machineId
	 * @mbggenerated  Wed Oct 28 11:51:13 CST 2015
	 */
	private String machineid;

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column user.id
	 * @return  the value of user.id
	 * @mbggenerated  Wed Oct 28 11:51:13 CST 2015
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column user.id
	 * @param id  the value for user.id
	 * @mbggenerated  Wed Oct 28 11:51:13 CST 2015
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column user.username
	 * @return  the value of user.username
	 * @mbggenerated  Wed Oct 28 11:51:13 CST 2015
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column user.username
	 * @param username  the value for user.username
	 * @mbggenerated  Wed Oct 28 11:51:13 CST 2015
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column user.password
	 * @return  the value of user.password
	 * @mbggenerated  Wed Oct 28 11:51:13 CST 2015
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column user.password
	 * @param password  the value for user.password
	 * @mbggenerated  Wed Oct 28 11:51:13 CST 2015
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column user.createday
	 * @return  the value of user.createday
	 * @mbggenerated  Wed Oct 28 11:51:13 CST 2015
	 */
	public Date getCreateday() {
		return createday;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column user.createday
	 * @param createday  the value for user.createday
	 * @mbggenerated  Wed Oct 28 11:51:13 CST 2015
	 */
	public void setCreateday(Date createday) {
		this.createday = createday;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column user.machineId
	 * @return  the value of user.machineId
	 * @mbggenerated  Wed Oct 28 11:51:13 CST 2015
	 */
	public String getMachineid() {
		return machineid;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column user.machineId
	 * @param machineid  the value for user.machineId
	 * @mbggenerated  Wed Oct 28 11:51:13 CST 2015
	 */
	public void setMachineid(String machineid) {
		this.machineid = machineid;
	}
}