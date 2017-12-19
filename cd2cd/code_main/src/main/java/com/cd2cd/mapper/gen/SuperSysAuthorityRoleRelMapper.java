package com.cd2cd.mapper.gen;

import com.cd2cd.domain.SysAuthorityRoleRel;
import com.cd2cd.domain.gen.SysAuthorityRoleRelCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SuperSysAuthorityRoleRelMapper {
    long countByExample(SysAuthorityRoleRelCriteria example);

    int deleteByExample(SysAuthorityRoleRelCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(SysAuthorityRoleRel record);

    int insertSelective(SysAuthorityRoleRel record);

    List<SysAuthorityRoleRel> selectByExample(SysAuthorityRoleRelCriteria example);

    SysAuthorityRoleRel selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SysAuthorityRoleRel record, @Param("example") SysAuthorityRoleRelCriteria example);

    int updateByExample(@Param("record") SysAuthorityRoleRel record, @Param("example") SysAuthorityRoleRelCriteria example);

    int updateByPrimaryKeySelective(SysAuthorityRoleRel record);

    int updateByPrimaryKey(SysAuthorityRoleRel record);
}