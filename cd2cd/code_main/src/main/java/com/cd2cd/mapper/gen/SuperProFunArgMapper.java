package com.cd2cd.mapper.gen;

import com.cd2cd.domain.ProFunArg;
import com.cd2cd.domain.gen.ProFunArgCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SuperProFunArgMapper {
    long countByExample(ProFunArgCriteria example);

    int deleteByExample(ProFunArgCriteria example);

    int deleteByPrimaryKey(Long id);

    int insert(ProFunArg record);

    int insertSelective(ProFunArg record);

    List<ProFunArg> selectByExample(ProFunArgCriteria example);

    ProFunArg selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ProFunArg record, @Param("example") ProFunArgCriteria example);

    int updateByExample(@Param("record") ProFunArg record, @Param("example") ProFunArgCriteria example);

    int updateByPrimaryKeySelective(ProFunArg record);

    int updateByPrimaryKey(ProFunArg record);
}