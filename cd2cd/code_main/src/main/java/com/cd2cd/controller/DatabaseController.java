package com.cd2cd.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cd2cd.comm.ServiceCode;
import com.cd2cd.domain.ProTable;
import com.cd2cd.service.ProDatabaseService;
import com.cd2cd.vo.BaseRes;
import com.cd2cd.vo.DbTreeNode;
import com.cd2cd.vo.ProDatabaseVo;
import com.cd2cd.vo.ProTableColumnVo;
import com.cd2cd.vo.ProTableVo;

@Controller
@RequestMapping("database")
public class DatabaseController {

	@Resource
	private ProDatabaseService proDatabaseService;
	
	@RequestMapping("databaseList")
	public @ResponseBody BaseRes<List<ProDatabaseVo>> databaseList() {
		BaseRes<List<ProDatabaseVo>> res = proDatabaseService.databaseList();
		return res;
	}
	
	@RequestMapping("databaseTree")
	public @ResponseBody BaseRes<List<DbTreeNode>> databaseTree() {
		BaseRes<List<DbTreeNode>> res = proDatabaseService.databaseTree();
		return res;
	}
	
	@RequestMapping(value = "dbInfo", method = RequestMethod.GET)
	public @ResponseBody BaseRes<ProDatabaseVo> dbDetail(ProDatabaseVo proDatabase) {
		BaseRes<ProDatabaseVo> res = new BaseRes<ProDatabaseVo>();
		ProDatabaseVo mProDatabaseVo = proDatabaseService.dbDetail(proDatabase.getId());
		res.setData(mProDatabaseVo);
		res.setServiceCode(ServiceCode.SUCCESS);
		return res;
	}
	
	@RequestMapping(value = "dbInfo", method = RequestMethod.POST)
	public @ResponseBody BaseRes<ProDatabaseVo> addDb(
			@Validated 
			@RequestBody ProDatabaseVo proDatabaseVo, 
			BindingResult bindingResult) {
		
		BaseRes<ProDatabaseVo> res = new BaseRes<>();
		boolean success = proDatabaseService.addDb(proDatabaseVo);
		if( success ) {
			res.setData(proDatabaseVo);
			res.setServiceCode(ServiceCode.SUCCESS);
		} else {
			res.setServiceCode(ServiceCode.FAILED);
		}
		return res;
	}
	
	@RequestMapping(value = "dbInfo", method = RequestMethod.PUT)
	public @ResponseBody BaseRes<String> modifyDb(
			@Validated 
			@RequestBody ProDatabaseVo proDatabaseVo, 
			BindingResult bindingResult) {
		
		BaseRes<String> res = new BaseRes<String>();
		boolean success = proDatabaseService.modifyDb(proDatabaseVo);
		if( success ) {
			res.setServiceCode(ServiceCode.SUCCESS);
		} else {
			res.setServiceCode(ServiceCode.FAILED);
		}
		return res;
	}
	
	@RequestMapping(value = "dbInfo", method = RequestMethod.DELETE)
	public @ResponseBody BaseRes<String> delDb(ProDatabaseVo proDatabaseVo) {
		
		BaseRes<String> res = new BaseRes<String>();
		boolean success = proDatabaseService.delDb(proDatabaseVo.getId());
		if( success ) {
			res.setServiceCode(ServiceCode.SUCCESS);
		} else {
			res.setServiceCode(ServiceCode.FAILED);
		}
		return res;
	}
	
	/**
	 * 数据库表同步到项目
	 * @param proDatabaseVo
	 * @return
	 */
	@RequestMapping(value = "asyncDbToProFromDbBase", method = RequestMethod.GET)
	public @ResponseBody BaseRes<String> asyncDbToProFromDbBase(ProDatabaseVo proDatabaseVo) {
		
		BaseRes<String> res = new BaseRes<String>();
		
		proDatabaseService.asyncDbToProFromDbBase(proDatabaseVo.getId());
		
		res.setServiceCode(ServiceCode.SUCCESS);
		
		return res;
	}
	
	/**
	 * 项目中的表同步到数据库
	 * @param proDatabaseVo
	 * @return
	 */
	@RequestMapping(value = "asyncDbToDbBaseFromPro", method = RequestMethod.GET)
	public @ResponseBody BaseRes<String> asyncDbToDbBaseFromPro(ProDatabaseVo proDatabaseVo) {
		
		BaseRes<String> res = new BaseRes<String>();
		res.setServiceCode(ServiceCode.SUCCESS);
		proDatabaseService.asyncDbToDbBaseFromPro(proDatabaseVo.getId());
		return res;
	}
	
	// ----- table -----
	
	@RequestMapping(value = "tableList", method = RequestMethod.GET)
	public @ResponseBody BaseRes<List<ProTable>> tableList(Long id) {
		BaseRes<List<ProTable>> res = new BaseRes<List<ProTable>>();
		
		List<ProTable> tables = proDatabaseService.getTableListByDbId(id);
		res.setData(tables);
		
		res.setServiceCode(ServiceCode.SUCCESS);
		return res;
	} 
	
	@RequestMapping(value = "tableDetail", method = RequestMethod.GET)
	public @ResponseBody BaseRes<ProTableVo> tableDetail(ProTableVo proTableVo) {
		BaseRes<ProTableVo> res = new BaseRes<ProTableVo>();
		ProTableVo mProTableVo = proDatabaseService.tableDetail(proTableVo.getId());
		res.setData(mProTableVo);
		res.setServiceCode(ServiceCode.SUCCESS);
		return res;
	} 
	
	@RequestMapping(value = "addTable", method = RequestMethod.POST)
	public @ResponseBody BaseRes<ProTableVo> addTable (
			@Validated 
			@RequestBody ProTableVo proTableVo, 
			BindingResult bindingResult) {
		
		BaseRes<ProTableVo> res = new BaseRes<ProTableVo>();
		ProTableVo mProTableVo = proDatabaseService.addTable(proTableVo);
		res.setData(mProTableVo);
		res.setServiceCode(ServiceCode.SUCCESS);
		
		return res;
	}
	
	@RequestMapping(value = "modifyTable", method = RequestMethod.POST)
	public @ResponseBody BaseRes<String> modifyTable (
			@Validated 
			@RequestBody ProTableVo proTableVo, 
			BindingResult bindingResult) {
		
		BaseRes<String> res = new BaseRes<String>();
		boolean success = proDatabaseService.modifyTable(proTableVo);
		if( success ) {
			res.setServiceCode(ServiceCode.SUCCESS);
		} else {
			res.setServiceCode(ServiceCode.FAILED);
		}
		return res;
	}
	
	@RequestMapping(value = "delTable", method = RequestMethod.GET)
	public @ResponseBody BaseRes<String> delTable (ProTableVo proTableVo) {
		
		BaseRes<String> res = new BaseRes<String>();
		boolean success = proDatabaseService.delTable(proTableVo.getId());
		if( success ) {
			res.setServiceCode(ServiceCode.SUCCESS);
		} else {
			res.setServiceCode(ServiceCode.FAILED);
		}
		return res;
	}
	
	// table column
	@RequestMapping(value = "addTableColumn", method = RequestMethod.POST)
	public @ResponseBody BaseRes<ProTableColumnVo> addTableColumn (
			@Validated 
			@RequestBody ProTableColumnVo proTableColumnVo, 
			BindingResult bindingResult) {
		
		BaseRes<ProTableColumnVo> res = new BaseRes<ProTableColumnVo>();
		ProTableColumnVo mProTableColumnVo = proDatabaseService.addTableColumn(proTableColumnVo);
		res.setData(mProTableColumnVo);
		res.setServiceCode(ServiceCode.SUCCESS);
		
		return res;
	}
	
	@RequestMapping(value = "modifyTableColumn", method = RequestMethod.POST)
	public @ResponseBody BaseRes<String> modifyTableColumn (
			@Validated 
			@RequestBody ProTableColumnVo proTableColumnVo, 
			BindingResult bindingResult) {
		
		BaseRes<String> res = new BaseRes<String>();
		boolean success = proDatabaseService.modifyTableColumn(proTableColumnVo);
		if( success ) {
			res.setServiceCode(ServiceCode.SUCCESS);
		} else {
			res.setServiceCode(ServiceCode.FAILED);
		}
		return res;
	}
	
	@RequestMapping(value = "delTableColumn", method = RequestMethod.GET)
	public @ResponseBody BaseRes<String> delTableColumn (ProTableColumnVo proTableColumnVo) {
		
		BaseRes<String> res = new BaseRes<String>();
		boolean success = proDatabaseService.delTableColumn(proTableColumnVo.getId());
		if( success ) {
			res.setServiceCode(ServiceCode.SUCCESS);
		} else {
			res.setServiceCode(ServiceCode.FAILED);
		}
		return res;
	}
	
}
