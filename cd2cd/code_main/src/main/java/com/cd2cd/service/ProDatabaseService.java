package com.cd2cd.service;

import java.util.List;

import com.cd2cd.vo.BaseRes;
import com.cd2cd.vo.DbTreeNode;
import com.cd2cd.vo.ProDatabaseVo;
import com.cd2cd.vo.ProTableVo;

public interface ProDatabaseService {
	
	BaseRes<List<DbTreeNode>> databaseTree();
	
	ProDatabaseVo dbDetail(Long id);
	
	boolean addDb(ProDatabaseVo proDatabaseVo);
	
	boolean modifyDb(ProDatabaseVo proDatabaseVo);

	boolean delDb(Long id);

	ProTableVo tableDetail(Long id);
	
}
