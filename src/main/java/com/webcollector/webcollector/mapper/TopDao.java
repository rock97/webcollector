package com.webcollector.webcollector.mapper;

import com.webcollector.webcollector.bean.Top;

import java.text.MessageFormat;
import java.util.Date;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Mapper
public interface TopDao {
    String COLUMNS = "id,title,heat,status";

    @Select("select "+COLUMNS+" from top order by id desc limit #{top}")
    List<Top> getTop(@Param("top") int top);

    @Select("select "+COLUMNS+" from top where title in (" +
            "<foreach collection='list' item='item' index='index' separator=','>" +
            "#{item}"+
            "</foreach>)")
    List<Top> getList(List<String> list);

    @Insert("insert into top (sequence,title,url,create_time,status,heat,type) values (#{sequence},#{title},#{url},#{date},#{status},#{heat},#{type})")
    void insert(Top top,Date date);

    @Insert("<script>  insert into top(sequence,title,heat,type,status,create_time) values" +
            "<foreach collection='list' item='item' index='index' separator=','>"+
            "(#{item.sequence},#{item.title},#{item.heat},#{item.type},#{item.status},now())"+
            "</foreach> </script>"
    )
    void bachInsert(@Param("list") List<Top> list, Date date);

    @Select("select id from top where status = 1 and create_time >= #{date} order by id desc")
    List<Long> getLastMinute(Date date);

    @Select("select id from top where status = 2 group by title order by id desc limit #{top}")
    List<Long> findDeletedTop(int top);

    @Select("select id from top where status = 2 and create_time >= #{date} group by title order by id desc")
    List<Long> findLastMinuteDeleted(Date date);

    @Select("select id from top where status = 2 and create_time >= #{start} and  create_time <=#{end}  group by title order by id desc")
    List<Long> findLastDayDeletedTop(Date start,Date end);

    @Select("select id from top where sequence < #{index} and status =1 group by title order by id desc limit #{base},#{top}")
    List<Long> findHistoryBurst(int index,int base,int top);

    @Select("<script>select "+COLUMNS+" from top where id in ("
            + "<foreach collection='list' item='item' index='index' separator=','>#{item}</foreach>)</script>")
    List<Top> findListByIds(@Param("list") List<Long> list);

}
