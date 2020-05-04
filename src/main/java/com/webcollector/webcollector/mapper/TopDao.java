package com.webcollector.webcollector.mapper;

import com.webcollector.webcollector.bean.Top;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

@Mapper
public interface TopDao {
    String COLUMNS = "id,title,heat,status,create_time as createTime";

    @Select("select "+COLUMNS+" from top limit #{start},#{end}")
    List<Top> getTop(int start,int end);

    @Select("<script>select "+COLUMNS+" from top where title in (" +
            "<foreach collection='list' item='item' index='index' separator=','>" +
            "#{item}"+
            "</foreach>)</script>")
    List<Top> getList(List<String> list);

    @Delete("<script> delete from top where id in (" +
            "<foreach collection='list' item='item' index='index' separator=','>" +
            "#{item}"+
            "</foreach>)</script> ")
    void delete(List<Long> list);

    @Insert("insert into top (sequence,title,status,heat,type) values (#{sequence},#{title},#{status},#{heat},#{type})")
    void insert(Top top,Date date);

    @Insert("<script>  insert into top(sequence,title,heat,type,status,create_time) values" +
            "<foreach collection='list' item='item' index='index' separator=','>"+
            "(#{item.sequence},#{item.title},#{item.heat},#{item.type},#{item.status},#{date})"+
            "</foreach> </script>"
    )
    void bachInsert(@Param("list") List<Top> list, Date date);

    @Select("select "+COLUMNS+" from top where status = 1 and create_time >= #{date} order by id desc")
    List<Top> getLastMinute(Date date);

    @Select("select "+COLUMNS+" from top where status = 2 order by id desc limit #{top}")
    List<Top> findDeletedTop(int top);

    @Select("select "+COLUMNS+" from top where status = 2 and create_time >= #{date}  order by id desc")
    List<Top> findLastMinuteDeleted(Date date);

    @Select("select "+COLUMNS+" from top where status = 1 and  create_time <=#{end}  order by id desc")
    List<Top> findLastTop(Date end);

    @Select("select "+COLUMNS+" from top where sequence < #{index} and status =1 order by id desc limit #{base},#{top}")
    List<Top> findHistoryBurst(int index,int base,int top);

    @Select("select "+COLUMNS+" from top where  create_time = #{start} ")
    List<Top> findMinute(Date start);

    @Select("select "+COLUMNS+" from top where title = #{title} order by id desc limit 1")
    Top getTopByTitle(String title);
}
