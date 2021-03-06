package com.live106.mars.account.db.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;

import com.live106.mars.account.db.model.User;
import com.live106.mars.account.db.model.UserExample;

//@CacheNamespaceRef(value=com.live106.mars.account.db.mapper.UserMapper.class)
public interface UserMapper {
    /**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table user
	 * @mbggenerated  Wed Oct 28 11:51:13 CST 2015
	 */
	int countByExample(UserExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table user
	 * @mbggenerated  Wed Oct 28 11:51:13 CST 2015
	 */
	int deleteByExample(UserExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table user
	 * @mbggenerated  Wed Oct 28 11:51:13 CST 2015
	 */
	int deleteByPrimaryKey(Integer id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table user
	 * @mbggenerated  Wed Oct 28 11:51:13 CST 2015
	 */
	int insert(User record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table user
	 * @mbggenerated  Wed Oct 28 11:51:13 CST 2015
	 */
//	@SelectKey(statement="call next value for TestSequence", keyProperty="id", before=true, resultType=int.class)
	int insertSelective(User record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table user
	 * @mbggenerated  Wed Oct 28 11:51:13 CST 2015
	 */
	List<User> selectByExample(UserExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table user
	 * @mbggenerated  Wed Oct 28 11:51:13 CST 2015
	 */
	User selectByPrimaryKey(Integer id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table user
	 * @mbggenerated  Wed Oct 28 11:51:13 CST 2015
	 */
	int updateByExampleSelective(@Param("record") User record, @Param("example") UserExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table user
	 * @mbggenerated  Wed Oct 28 11:51:13 CST 2015
	 */
	int updateByExample(@Param("record") User record, @Param("example") UserExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table user
	 * @mbggenerated  Wed Oct 28 11:51:13 CST 2015
	 */
	int updateByPrimaryKeySelective(User record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table user
	 * @mbggenerated  Wed Oct 28 11:51:13 CST 2015
	 */
	int updateByPrimaryKey(User record);

	@Select("select * from user where username=#{username}")
    User selectByUsername(String username);
    
    @Select("select password from user where username=#{username}")
    String getPassword(String username);
    	
//    @Select("select * from user where machineId=#{machineId}")
//    @Options(useCache=true)
    User selectByMachineId(String machineId);
}