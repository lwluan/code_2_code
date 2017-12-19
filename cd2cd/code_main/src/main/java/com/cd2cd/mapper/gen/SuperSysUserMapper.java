package com.cd2cd.mapper.gen;

import com.cd2cd.domain.SysUser;
import com.cd2cd.domain.gen.SysUserCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SuperSysUserMapper {
    long countByExample(SysUserCriteria example);

    int deleteByExample(SysUserCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    List<SysUser> selectByExample(SysUserCriteria example);

    SysUser selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SysUser record, @Param("example") SysUserCriteria example);

    int updateByExample(@Param("record") SysUser record, @Param("example") SysUserCriteria example);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);
}