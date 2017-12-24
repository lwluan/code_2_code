package com.cd2cd.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cd2cd.comm.ServiceCode;
import com.cd2cd.domain.ProDatabase;
import com.cd2cd.domain.ProTable;
import com.cd2cd.domain.ProTableColumn;
import com.cd2cd.domain.gen.ProTableColumnCriteria;
import com.cd2cd.domain.gen.ProTableCriteria;
import com.cd2cd.mapper.ProDatabaseMapper;
import com.cd2cd.mapper.ProTableColumnMapper;
import com.cd2cd.mapper.ProTableMapper;
import com.cd2cd.service.ProDatabaseService;
import com.cd2cd.util.BeanUtil;
import com.cd2cd.vo.BaseRes;
import com.cd2cd.vo.DbTreeNode;
import com.cd2cd.vo.ProDatabaseVo;
import com.cd2cd.vo.ProTableColumnVo;
import com.cd2cd.vo.ProTableVo;

@Service
public class ProDatabaseServiceImpl implements ProDatabaseService {

	@Autowired
	private ProDatabaseMapper proDatabaseMapper;
	@Autowired
	private ProTableMapper proTableMapper;
	@Autowired
	private ProTableColumnMapper proTableColumnMapper;
	
	@Override
	public BaseRes<List<DbTreeNode>> databaseTree() {
		
		BaseRes<List<DbTreeNode>> res = new BaseRes<List<DbTreeNode>>();
		
		int index = 1;
		List<ProDatabase> databases = proDatabaseMapper.selectByExample(null);
		if( databases.size() > 0 ) {
			
			List<DbTreeNode> nodes = new ArrayList<DbTreeNode>();
			for( ProDatabase db: databases ) {
				
				DbTreeNode mDbTreeNode = new DbTreeNode();
				mDbTreeNode.setId(index);
				mDbTreeNode.setName(db.getDbName());
				mDbTreeNode.setValue(db.getId());
				mDbTreeNode.setType(1);
				
				nodes.add(mDbTreeNode);
				
				ProTableCriteria example = new ProTableCriteria();
				example.createCriteria().andDatabaseIdEqualTo(db.getId());
				List<ProTable> tables = proTableMapper.selectByExample(example);
				
				int pid = index;
				for( ProTable pt: tables ) {
					index ++;
					
					mDbTreeNode = new DbTreeNode();
					mDbTreeNode.setId(index);
					mDbTreeNode.setPid(pid);
					mDbTreeNode.setName(pt.getName());
					mDbTreeNode.setValue(pt.getId());
					mDbTreeNode.setType(2);
					
					nodes.add(mDbTreeNode);
				}
				
				index ++;
			}
			
			res.setData(nodes);
		}
		res.setServiceCode(ServiceCode.SUCCESS);
		return res;
	}

	@Override
	public ProDatabaseVo dbDetail(Long id) {
		ProDatabase mProDatabase = proDatabaseMapper.selectByPrimaryKey(id);
		if( null != mProDatabase ) {
			return BeanUtil.voConvert(mProDatabase, ProDatabaseVo.class);
		}
		return null;
	}

	@Override
	public boolean addDb(ProDatabaseVo proDatabaseVo) {
		return 0 < proDatabaseMapper.insert(proDatabaseVo);
	}

	@Override
	public boolean modifyDb(ProDatabaseVo proDatabaseVo) {
		return proDatabaseMapper.updateByPrimaryKeySelective(proDatabaseVo) > 0;
	}

	@Override
	public boolean delDb(Long id) {
		return 0 < proDatabaseMapper.deleteByPrimaryKey(id);
	}

	@Override
	public ProTableVo tableDetail(Long id) {
		ProTable mProTable = proTableMapper.selectByPrimaryKey(id);
		if( mProTable != null ) {
			ProTableVo mProTableVo = BeanUtil.voConvert(mProTable, ProTableVo.class);
			
			ProTableColumnCriteria example = new ProTableColumnCriteria();
			example.createCriteria().andTableIdEqualTo(mProTableVo.getId());
			List<ProTableColumn> tables = proTableColumnMapper.selectByExample(example);
			
			List<ProTableColumnVo> columns = BeanUtil.voConvertList(tables, ProTableColumnVo.class);
			mProTableVo.setColumns(columns);
			
			return mProTableVo;
		}
		return null;
	}

}
