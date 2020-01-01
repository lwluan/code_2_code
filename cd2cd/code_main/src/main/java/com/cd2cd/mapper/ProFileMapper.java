package com.cd2cd.mapper;

import com.cd2cd.domain.ProFile;
import com.cd2cd.domain.gen.ProFileCriteria;
import com.cd2cd.mapper.gen.SuperProFileMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProFileMapper extends SuperProFileMapper {

	List<ProFile> selectFileAndModule(ProFileCriteria mProFileCriteria);

	List<ProFile> selectFileAndModuleByMicro(@Param("microId") Long microId, @Param("fileType") String fileType);

}