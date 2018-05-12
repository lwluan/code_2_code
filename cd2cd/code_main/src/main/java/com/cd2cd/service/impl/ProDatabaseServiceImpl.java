package com.cd2cd.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.cd2cd.util.mbg.Constants;
import com.cd2cd.util.mbg.DBUtil;
import com.cd2cd.vo.BaseRes;
import com.cd2cd.vo.DbTreeNode;
import com.cd2cd.vo.ProDatabaseVo;
import com.cd2cd.vo.ProTableColumnVo;
import com.cd2cd.vo.ProTableVo;

@Service
public class ProDatabaseServiceImpl implements ProDatabaseService {

	private static final Logger LOG = LoggerFactory.getLogger(ProDatabaseServiceImpl.class);
	
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

	@Override
	public ProTableVo addTable(ProTableVo proTableVo) {
		proTableVo.setCreateTime(new Date());
		proTableVo.setUpdateTime(new Date());
		proTableMapper.insert(proTableVo);
		return proTableVo;
	}

	@Override
	public boolean modifyTable(ProTableVo proTableVo) {
		proTableVo.setUpdateTime(new Date());
		return 0 < proTableMapper.updateByPrimaryKeySelective(proTableVo);
	}

	@Override
	public boolean delTable(Long id) {
		return 0 < proTableMapper.deleteByPrimaryKey(id);
	}

	@Override
	public ProTableColumnVo addTableColumn(ProTableColumnVo proTableColumnVo) {
		proTableColumnVo.setCreateTime(new Date());
		proTableColumnVo.setUpdateTime(new Date());
		proTableColumnMapper.insert(proTableColumnVo);
		return proTableColumnVo;
	}

	@Override
	public boolean modifyTableColumn(ProTableColumnVo proTableColumnVo) {
		proTableColumnVo.setUpdateTime(new Date());
		return 0 < proTableColumnMapper.updateByPrimaryKeySelective(proTableColumnVo);
	}

	@Override
	public boolean delTableColumn(Long id) {
		return 0 < proTableColumnMapper.deleteByPrimaryKey(id);
	}

	@Override
	public BaseRes<List<ProDatabaseVo>> databaseList() {
		List<ProDatabase> databases = proDatabaseMapper.selectByExample(null);
		
		BaseRes<List<ProDatabaseVo>> res = new BaseRes<List<ProDatabaseVo>>();
		List<ProDatabaseVo> list = BeanUtil.voConvertList(databases, ProDatabaseVo.class);
		
		res.setData(list);
		return res;
	}

	@Override
	public boolean asyncDbToProFromDbBase(Long databaseId) {
		
		// show tables
		// Utils 
		ProDatabase mProDatabase = proDatabaseMapper.selectByPrimaryKey(databaseId);
		
		String host = mProDatabase.getHostname();
		int port = 3306;
		String user = mProDatabase.getUsername();
		String password = mProDatabase.getPassword();
		String db = mProDatabase.getDbName();
		
		List<Map<String, Object>> tables = DBUtil.exeQuerySql("show table status", host, port, user, password, db);
		
		for( Map<String, Object> tabMap : tables ) {
			
			String tabName = tabMap.get("Name").toString();
			String comment = tabMap.get("Comment").toString();
			
			LOG.info("tabName={}, comment={}", tabName, comment);
			
			// 判断表在项目的数据库是否存在
			ProTableCriteria mProTableCriteria = new ProTableCriteria();
			mProTableCriteria.createCriteria().andNameEqualTo(tabName);
			List<ProTable> proTab = proTableMapper.selectByExample(mProTableCriteria);
			ProTable mProTable = null;
			if( proTab.size() > 0 ) {
				mProTable = proTab.get(0);
			}
			
			List<Map<String, Object>> columns = DBUtil.exeQuerySql("show full columns from " + tabName, host, port, user, password, db);
			
			if( mProTable == null ) {
				mProTable = new ProTable();
				mProTable.setName(tabName);
				mProTable.setComment(comment);
				mProTable.setDatabaseId(databaseId);
				mProTable.setCreateTime(new Date());
				mProTable.setUpdateTime(new Date());
				proTableMapper.insert(mProTable);
				
			} else {
				mProTable.setComment(comment);
				mProTable.setUpdateTime(new Date());
				proTableMapper.updateByPrimaryKeySelective(mProTable);
				
			}
			
			Long tableId = mProTable.getId();
			
			
			List<String> allColumns = new ArrayList<String>();
			for( Map<String, Object> col: columns ) {
				String field = col.get("Field").toString();
				String type = col.get("Type").toString();
				String _null = col.get("Null").toString(); // NO YES
				String key = col.get("Key").toString(); // PRI
				String _comment = col.get("Comment").toString();
				String defaultValue = null;
				if( null != col.get("Default") ) {
					defaultValue = col.get("Default").toString();
				}

				allColumns.add(field);
				
				// TODO key uniq pri uml
				
				ProTableColumn mProTableColumn = new ProTableColumn();
				mProTableColumn.setKeyType(key);
				mProTableColumn.setComment(_comment);
				mProTableColumn.setDefaultValue(defaultValue);
				mProTableColumn.setName(field);
				mProTableColumn.setMysqlType(type);
				mProTableColumn.setAllowNull(_null);
				mProTableColumn.setTableId(tableId);
				mProTableColumn.setUpdateTime(new Date());
				
				
				ProTableColumnCriteria nProTableColumnCriteria = new ProTableColumnCriteria();
				nProTableColumnCriteria.createCriteria()
				.andTableIdEqualTo(mProTable.getId())
				.andNameEqualTo(field);
				
				List<ProTableColumn> proColumns = proTableColumnMapper.selectByExample(nProTableColumnCriteria);
				if( proColumns.size() > 0 ) {
					
					
					// update table columns
					proTableColumnMapper.updateByPrimaryKeySelective(mProTableColumn);
				} else {
					
					// insert table columns
					mProTableColumn.setCreateTime(new Date());
					proTableColumnMapper.insertSelective(mProTableColumn);
				}
			}
			
			// delete other columns
			ProTableColumnCriteria delProTableColumnCriteria = new ProTableColumnCriteria();
			delProTableColumnCriteria.createCriteria()
			.andNameNotIn(allColumns)
			.andTableIdEqualTo(tableId);
			proTableColumnMapper.deleteByExample(delProTableColumnCriteria);
		}
		return true;
	}

	@Override
	public boolean asyncDbToDbBaseFromPro(Long databaseId) {
		
		ProDatabase mProDatabase = proDatabaseMapper.selectByPrimaryKey(databaseId);
		
		String host = mProDatabase.getHostname();
		int port = 3306;
		String user = mProDatabase.getUsername();
		String password = mProDatabase.getPassword();
		String db = mProDatabase.getDbName();
		
		
		// query all table from project 
		ProTableCriteria mProTableCriteria = new ProTableCriteria();
		mProTableCriteria.createCriteria()
		.andDatabaseIdEqualTo(databaseId);
		List<ProTable> tables = proTableMapper.selectByExample(mProTableCriteria);
		
		for( ProTable tab : tables ) {
			
			String tableName = tab.getName();
			String comment = tab.getComment();
			
			StringBuffer createTab = new StringBuffer();
			
			createTab.append( "create table IF NOT EXISTS " );
			createTab.append( tableName );
			createTab.append( "(" );
			
			// 主键过滤
			List<String> primaryKey = new ArrayList<String>();
			
			ProTableColumnCriteria mProTableColumnCriteria = new ProTableColumnCriteria();
			mProTableColumnCriteria.createCriteria()
			.andTableIdEqualTo(tab.getId());
			List<ProTableColumn> columns = proTableColumnMapper.selectByExample(mProTableColumnCriteria);
			for( ProTableColumn c: columns ) {
				
				createTab.append( c.getName() );
				createTab.append(" ");
				createTab.append(c.getMysqlType());
				createTab.append(" ");
				
				if( "NO".equals(c.getAllowNull()) ) {
					createTab.append(" NOT NULL ");
				}
				
				if( null != c.getDefaultValue() ) {
					createTab.append(" DEFAULT ");
					if( "NULL".equals(c.getDefaultValue()) ) {
						createTab.append(" NULL ");
					} else if( Constants.DEFAULT_NO_STRING.contains(c.getDefaultValue()) ) {
//						  CURRENT_TIMESTAMP
						createTab.append(" "+c.getDefaultValue()+" ");
					} else {
						createTab.append(" '"+c.getDefaultValue()+"' ");
					}
				}
				
				if( StringUtils.isNotEmpty(c.getComment()) ) {
					createTab.append(" COMMENT '"+c.getComment()+"' ");
				}
				
				if( "PRI".equals(c.getKeyType()) ) {
					primaryKey.add(c.getName());
				}
				
				
				// pri key
				// AUTO_INCREMENT
				
				createTab.append(", ");
			}
			
			
			// primary key 
			// primaryKey.append("");
			
			
			
			// delete last char that ','
			
			
			createTab.append( ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='"+comment+"'" );
			
			String sql = createTab.toString();
			DBUtil.exeQuerySql(sql, host, port, user, password, db);
			
			
			// - - - update columns - - - 
			
		}
		
		return true;
	}

}
