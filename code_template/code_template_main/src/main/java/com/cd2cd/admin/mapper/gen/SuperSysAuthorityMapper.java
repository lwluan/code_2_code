package com.cd2cd.admin.mapper.gen;

import com.cd2cd.admin.domain.SysAuthority;
import com.cd2cd.admin.domain.gen.SysAuthorityCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SuperSysAuthorityMapper {
    long countByExample(SysAuthorityCriteria example);

    int deleteByExample(SysAuthorityCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(SysAuthority record);

    int insertSelective(SysAuthority record);

    List<SysAuthority> selectByExample(SysAuthorityCriteria example);

    SysAuthority selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SysAuthority record, @Param("example") SysAuthorityCriteria example);

    int updateByExample(@Param("record") SysAuthority record, @Param("example") SysAuthorityCriteria example);

    int updateByPrimaryKeySelective(SysAuthority record);

    int updateByPrimaryKey(SysAuthority record);
}