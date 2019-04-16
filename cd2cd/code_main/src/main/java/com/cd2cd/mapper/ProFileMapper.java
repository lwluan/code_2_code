package com.cd2cd.mapper;

import java.util.List;

import com.cd2cd.domain.ProFile;
import com.cd2cd.domain.gen.ProFileCriteria;
import com.cd2cd.mapper.gen.SuperProFileMapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProFileMapper extends SuperProFileMapper {

	List<ProFile> selectFileAndModule(ProFileCriteria mProFileCriteria);
}