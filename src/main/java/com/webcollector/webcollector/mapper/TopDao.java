package com.webcollector.webcollector.mapper;

import com.webcollector.webcollector.bean.Top;
import java.util.Date;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.List;

@Mapper
public interface TopDao {

    @Select("select * from top order by id desc limit #{top}")
    List<Top> getTop(@Param("top") int top);

    @Select("select * from top where title in (" +
            "<foreach collection='list' item='item' index='index' separator=','>" +
            "item"+
            "</foreach>)")
    List<Top> getList(List<String> list);

    @Insert("insert into top (sequence,title,create_time,status,heat,type) values (#{sequence},#{title},#{date},#{status},#{heat},#{type})")
    void insert(Top top,Date date);

    @Insert("insert into top(sequence,title,heat,type,status,create_time) values" +
            "<foreach collection='list' item='item' index='index' separator=','>"+
            "(#{item.sequence},#{item.title},#{item.heat},#{item.type},#{item.status},#{date})"+
            "</foreach>"
    )
    void bachInsert(@Param("list") List<Top> list, Date date);

    @Select("select * from top order by id desc where create_time = #{date}")
    List<Top> getLastMinute(Date date);

    @Select("select * from top order by id desc where status = 2 order by id desc limit #{top}")
    List<Top> findDeletedTop(int top);

}
