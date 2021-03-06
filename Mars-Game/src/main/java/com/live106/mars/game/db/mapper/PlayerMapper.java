package com.live106.mars.game.db.mapper;

import com.live106.mars.game.db.model.Player;
import com.live106.mars.game.db.model.PlayerExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PlayerMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table player
     *
     * @mbggenerated Fri Oct 23 10:39:49 CST 2015
     */
    int countByExample(PlayerExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table player
     *
     * @mbggenerated Fri Oct 23 10:39:49 CST 2015
     */
    int deleteByExample(PlayerExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table player
     *
     * @mbggenerated Fri Oct 23 10:39:49 CST 2015
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table player
     *
     * @mbggenerated Fri Oct 23 10:39:49 CST 2015
     */
    int insert(Player record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table player
     *
     * @mbggenerated Fri Oct 23 10:39:49 CST 2015
     */
    int insertSelective(Player record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table player
     *
     * @mbggenerated Fri Oct 23 10:39:49 CST 2015
     */
    List<Player> selectByExample(PlayerExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table player
     *
     * @mbggenerated Fri Oct 23 10:39:49 CST 2015
     */
    Player selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table player
     *
     * @mbggenerated Fri Oct 23 10:39:49 CST 2015
     */
    int updateByExampleSelective(@Param("record") Player record, @Param("example") PlayerExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table player
     *
     * @mbggenerated Fri Oct 23 10:39:49 CST 2015
     */
    int updateByExample(@Param("record") Player record, @Param("example") PlayerExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table player
     *
     * @mbggenerated Fri Oct 23 10:39:49 CST 2015
     */
    int updateByPrimaryKeySelective(Player record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table player
     *
     * @mbggenerated Fri Oct 23 10:39:49 CST 2015
     */
    int updateByPrimaryKey(Player record);
}