package com.webcollector.webcollector.mapper;

import com.webcollector.webcollector.bean.TopDeleted;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface TopDeletedDao {

    @Select("<script> select title from top_deleted where title in ("
            + "<foreach collection='titleList' item='item' index='index' separator=','> "
            + "#{item}"+
            "</foreach> )"
            + "</script> ")
    List<String> getTitleList(@Param("titleList")List<String> titleList);

    @Update("<script> update top_deleted set count = count + 1 where  title in ("
            + "<foreach collection='titleList' item='item' index='index' separator=','> "
            + "#{item}"+
            "</foreach>)"
            + "</script> ")
    void addinc(@Param("titleList")List<String> titleList);

    @Insert("<script>  insert into top_deleted(top_id,title,count,create_time,update_time) values " +
            "<foreach collection='list' item='item' index='index' separator=','>"+
            " (#{item.topId},#{item.title},1,now(),now()) "+
            "</foreach>"
            + "</script>"
    )
    void bachInsert(@Param("list")List<TopDeleted> list);

    @Select("SELECT top_deleted.id,top_deleted.title,top.heat,top_deleted.count,top.sequence "
            + "FROM top JOIN top_deleted on top.ID = top_deleted.top_id "
            + "ORDER BY top_deleted.update_time DESC LIMIT top")
    List<TopDeleted> getTop(@Param("top")int top);

    @Select("<script> select title from top_deleted where title in (<foreach collection='titleList' item='item' index='index' separator=','> #{item} </foreach> )"
            +" UNION "+
            " select title from top where title in (<foreach collection='titleList' item='item' index='index' separator=','> #{item} </foreach>)"
            + "</script> ")
    List<String> getTitleListAll(@Param("titleList")List<String> titleList);

    @Select("select top_id as topId from top_deleted where update_time >= #{date}")
    List<Long> findLastMinuteDeleted(@Param("date")Date date);

}
