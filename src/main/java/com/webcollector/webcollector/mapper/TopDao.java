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
    String COLUMNS = "id,sequence,title,url,heat,type,status,create_time AS createTime ";

    @Select("select "+COLUMNS+" from top order by id desc limit #{top}")
    List<Top> getTop(@Param("top") int top);

    @Select("select "+COLUMNS+" from top where title in (" +
            "<foreach collection='list' item='item' index='index' separator=','>" +
            "#{item}"+
            "</foreach>)")
    List<Top> getList(List<String> list);

    @Insert("insert into top (sequence,title,url,create_time,status,heat,type) values (#{sequence},#{title},#{url},#{date},#{status},#{heat},#{type})")
    void insert(Top top,Date date);

    @Insert("<script>  insert into top(sequence,title,url,heat,type,status,create_time) values" +
            "<foreach collection='list' item='item' index='index' separator=','>"+
            "(#{item.sequence},#{item.title},#{item.url},#{item.heat},#{item.type},#{item.status},#{date})"+
            "</foreach> </script>"
    )
    void bachInsert(@Param("list") List<Top> list, Date date);

    @Select("select "+COLUMNS+" from top where status = 1 and create_time >= #{date} order by id desc")
    List<Top> getLastMinute(Date date);

    @Select("select "+COLUMNS+" from top where status = 2 group by title order by id desc limit #{top}")
    List<Top> findDeletedTop(int top);

    @Select("select "+COLUMNS+" from top where status = 2 and create_time >= #{date} group by title order by id desc")
    List<Top> findLastMinuteDeleted(Date date);

    @Select("select "+COLUMNS+" from top where sequence < #{index} and status =1 group by title order by id desc limit #{base},#{top}")
    List<Top> findHistoryBurst(int index,int base,int top);

}
