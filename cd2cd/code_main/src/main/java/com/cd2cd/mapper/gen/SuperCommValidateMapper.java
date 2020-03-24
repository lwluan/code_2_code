package com.cd2cd.mapper.gen;

import com.cd2cd.domain.CommValidate;
import com.cd2cd.domain.gen.CommValidateCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SuperCommValidateMapper {
    long countByExample(CommValidateCriteria example);

    int deleteByExample(CommValidateCriteria example);

    int deleteByPrimaryKey(Long id);

    int insert(CommValidate record);

    int insertSelective(CommValidate record);

    List<CommValidate> selectByExample(CommValidateCriteria example);

    CommValidate selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") CommValidate record, @Param("example") CommValidateCriteria example);

    int updateByExample(@Param("record") CommValidate record, @Param("example") CommValidateCriteria example);

    int updateByPrimaryKeySelective(CommValidate record);

    int updateByPrimaryKey(CommValidate record);
}