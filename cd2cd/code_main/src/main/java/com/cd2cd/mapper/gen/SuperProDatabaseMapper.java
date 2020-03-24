package com.cd2cd.mapper.gen;

import com.cd2cd.domain.ProDatabase;
import com.cd2cd.domain.gen.ProDatabaseCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SuperProDatabaseMapper {
    long countByExample(ProDatabaseCriteria example);

    int deleteByExample(ProDatabaseCriteria example);

    int deleteByPrimaryKey(Long id);

    int insert(ProDatabase record);

    int insertSelective(ProDatabase record);

    List<ProDatabase> selectByExample(ProDatabaseCriteria example);

    ProDatabase selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ProDatabase record, @Param("example") ProDatabaseCriteria example);

    int updateByExample(@Param("record") ProDatabase record, @Param("example") ProDatabaseCriteria example);

    int updateByPrimaryKeySelective(ProDatabase record);

    int updateByPrimaryKey(ProDatabase record);
}