package com.webcollector.webcollector.mapper;

import com.webcollector.webcollector.bean.TopHistory;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

@Mapper
public interface TopHistoryDao {
    String COLUMNS = "id,title,heat,status";

    @Select("select "+COLUMNS+" from top_history order by id desc limit #{top}")
    List<TopHistory> getTop(@Param("top") int top);

    @Select("<script> select "+COLUMNS+" from top_history where title in (" +
            "<foreach collection='list' item='i' index='index' separator=','>" +
            "#{i}"+
            "</foreach>)</script>")
    List<TopHistory> getList(@Param("list")List<String> list);


    @Insert("<script>  insert into top_history(sequence,title,heat,type,status,create_time) values" +
            "<foreach collection='list' item='item' index='index' separator=','>"+
            "(#{item.sequence},#{item.title},#{item.heat},#{item.type},#{item.status},#{item.createTime})"+
            "</foreach> </script>"
    )
    void bachInsert(@Param("list") List<TopHistory> list);

    @Update("update top_history set heat = #{p.heat} where id = #{p.id}" )
    void update(@Param("p") TopHistory p);

    @Select("select "+COLUMNS+" from top_history where status = 1 and create_time >= #{date} order by id desc")
    List<TopHistory> getLastMinute(Date date);

    @Select("select "+COLUMNS+" from top_history where status = 2 order by id desc limit #{top}")
    List<TopHistory> findDeletedTop(int top);

    @Select("select "+COLUMNS+" from top_history where status = 2 and create_time >= #{date}  order by id desc")
    List<TopHistory> findLastMinuteDeleted(Date date);

    @Select("select "+COLUMNS+" from top_history where status = 2 and create_time >= #{start} and  create_time <=#{end}  order by id desc")
    List<TopHistory> findLastDayDeletedTop(Date start, Date end);

    @Select("select "+COLUMNS+" from top_history where sequence < #{index} and status =1 order by id desc limit #{base},#{top}")
    List<TopHistory> findHistoryBurst(int index, int base, int top);

}
