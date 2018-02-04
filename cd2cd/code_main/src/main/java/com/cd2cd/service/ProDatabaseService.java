package com.cd2cd.service;

import java.util.List;

import com.cd2cd.vo.BaseRes;
import com.cd2cd.vo.DbTreeNode;
import com.cd2cd.vo.ProDatabaseVo;
import com.cd2cd.vo.ProTableColumnVo;
import com.cd2cd.vo.ProTableVo;

public interface ProDatabaseService {
	
	BaseRes<List<ProDatabaseVo>> databaseList();
	
	BaseRes<List<DbTreeNode>> databaseTree();
	
	ProDatabaseVo dbDetail(Long id);
	
	boolean addDb(ProDatabaseVo proDatabaseVo);
	
	boolean modifyDb(ProDatabaseVo proDatabaseVo);

	boolean delDb(Long id);

	ProTableVo tableDetail(Long id);

	ProTableVo addTable(ProTableVo proTableVo);

	boolean modifyTable(ProTableVo proTableVo);

	boolean delTable(Long id);

	ProTableColumnVo addTableColumn(ProTableColumnVo proTableColumnVo);

	boolean modifyTableColumn(ProTableColumnVo proTableColumnVo);

	boolean delTableColumn(Long id);
	
}
