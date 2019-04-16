package com.cd2cd.mapper.gen;

import com.cd2cd.domain.ProFun;
import com.cd2cd.domain.gen.ProFunCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SuperProFunMapper {
    long countByExample(ProFunCriteria example);

    int deleteByExample(ProFunCriteria example);

    int deleteByPrimaryKey(Long id);

    int insert(ProFun record);

    int insertSelective(ProFun record);

    List<ProFun> selectByExample(ProFunCriteria example);

    ProFun selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ProFun record, @Param("example") ProFunCriteria example);

    int updateByExample(@Param("record") ProFun record, @Param("example") ProFunCriteria example);

    int updateByPrimaryKeySelective(ProFun record);

    int updateByPrimaryKey(ProFun record);
}