package com.cd2cd.admin.mapper.gen;

import com.cd2cd.admin.domain.SysUserRoleRel;
import com.cd2cd.admin.domain.gen.SysUserRoleRelCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SuperSysUserRoleRelMapper {
    long countByExample(SysUserRoleRelCriteria example);

    int deleteByExample(SysUserRoleRelCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(SysUserRoleRel record);

    int insertSelective(SysUserRoleRel record);

    List<SysUserRoleRel> selectByExample(SysUserRoleRelCriteria example);

    SysUserRoleRel selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SysUserRoleRel record, @Param("example") SysUserRoleRelCriteria example);

    int updateByExample(@Param("record") SysUserRoleRel record, @Param("example") SysUserRoleRelCriteria example);

    int updateByPrimaryKeySelective(SysUserRoleRel record);

    int updateByPrimaryKey(SysUserRoleRel record);
}