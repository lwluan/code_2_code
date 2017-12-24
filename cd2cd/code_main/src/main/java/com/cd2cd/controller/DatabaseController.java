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

import com.cd2cd.comm.DValid;
import com.cd2cd.comm.ServiceCode;
import com.cd2cd.service.ProDatabaseService;
import com.cd2cd.vo.BaseRes;
import com.cd2cd.vo.DbTreeNode;
import com.cd2cd.vo.ProDatabaseVo;
import com.cd2cd.vo.ProTableVo;

@Controller
@RequestMapping("database")
public class DatabaseController {

	@Resource
	private ProDatabaseService proDatabaseService;
	
	@RequestMapping("databaseTree")
	public @ResponseBody BaseRes<List<DbTreeNode>> databaseTree() {
		BaseRes<List<DbTreeNode>> res = proDatabaseService.databaseTree();
		return res;
	}
	
	@RequestMapping("dbDetail")
	public @ResponseBody BaseRes<ProDatabaseVo> dbDetail(ProDatabaseVo proDatabase) {
		BaseRes<ProDatabaseVo> res = new BaseRes<ProDatabaseVo>();
		ProDatabaseVo mProDatabaseVo = proDatabaseService.dbDetail(proDatabase.getId());
		res.setData(mProDatabaseVo);
		res.setServiceCode(ServiceCode.SUCCESS);
		return res;
	}
	
	@RequestMapping(value = "addDb", method = RequestMethod.POST)
	public @ResponseBody BaseRes<String> addDb(
			@Validated(value = {DValid.AddEntity.class}) 
			@RequestBody ProDatabaseVo proDatabaseVo, 
			BindingResult bindingResult) {
		
		BaseRes<String> res = new BaseRes<String>();
		boolean success = proDatabaseService.addDb(proDatabaseVo);
		if( success ) {
			res.setServiceCode(ServiceCode.SUCCESS);
		} else {
			res.setServiceCode(ServiceCode.FAILED);
		}
		return res;
	}
	
	@RequestMapping(value = "modifyDb", method = RequestMethod.POST)
	public @ResponseBody BaseRes<String> modifyDb(
			@Validated(value = {DValid.ModifyEntity.class}) 
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
	
	@RequestMapping(value = "delDb", method = RequestMethod.GET)
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
	
	@RequestMapping(value = "tableDetail", method = RequestMethod.GET)
	public @ResponseBody BaseRes<ProTableVo> tableDetail(ProTableVo proTableVo) {
		BaseRes<ProTableVo> res = new BaseRes<ProTableVo>();
		ProTableVo mProTableVo = proDatabaseService.tableDetail(proTableVo.getId());
		res.setData(mProTableVo);
		res.setServiceCode(ServiceCode.SUCCESS);
		return res;
	} 
}
