package com.cd2cd.admin.controller;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cd2cd.admin.comm.DValid;
import com.cd2cd.admin.comm.ServiceCode;
import com.cd2cd.admin.service.SysRoleService;
import com.cd2cd.admin.vo.BaseRes;
import com.cd2cd.admin.vo.DataPageWrapper;
import com.cd2cd.admin.vo.ObjDataWrapper;
import com.cd2cd.admin.vo.SysAuthorityVo;
import com.cd2cd.admin.vo.SysRoleVo;

@Controller
@RequestMapping("sysRole")
public class SysRoleController extends BaseController {

	private static Logger LOG = LoggerFactory.getLogger(SysRoleController.class);
	
	@Resource
	private SysRoleService sysRoleService;
	
	@RequestMapping( value = "list", method = RequestMethod.GET)
	public @ResponseBody BaseRes<DataPageWrapper<SysRoleVo>> queryPageList(
			Integer currPage, Integer pageSize, SysRoleVo sysRoleVo) {

		LOG.info("currPage={}, pageSize={}, sysRoleVo={}", currPage, pageSize, sysRoleVo);
		
		BaseRes<DataPageWrapper<SysRoleVo>> res = sysRoleService.list(currPage, pageSize, sysRoleVo);

		return res;
	}
	
	@RequestMapping( value = "detail/{id}", method = RequestMethod.GET)
	public @ResponseBody BaseRes<ObjDataWrapper<SysRoleVo, List<SysAuthorityVo>, Object>> detail(@PathVariable("id") Integer id) {
		
		BaseRes<ObjDataWrapper<SysRoleVo, List<SysAuthorityVo>, Object>> res = new BaseRes<ObjDataWrapper<SysRoleVo, List<SysAuthorityVo>, Object>>();
		
		ObjDataWrapper<SysRoleVo, List<SysAuthorityVo>, Object> objDataWrap = sysRoleService.detail(id);
		res.setData(objDataWrap);
		res.setServiceCode(ServiceCode.SUCCESS);
		
		return res;
	}
	
	@RequestMapping( value = "del/{id}", method = RequestMethod.GET)
	public @ResponseBody BaseRes<String> del(@PathVariable("id") Integer id) {
		
		BaseRes<String> res = new BaseRes<String>();
		
		boolean success = sysRoleService.del(id);
		if( success ) {
			res.setServiceCode(ServiceCode.SUCCESS);
		} else {
			res.setServiceCode(ServiceCode.FAILED);
		}
		
		return res;
	}
	
	@RequestMapping(value = "add", method = RequestMethod.POST)
	public @ResponseBody BaseRes<String> add(
			@Validated(value = {DValid.AddEntity.class}) 
			@RequestBody SysRoleVo sysRoleVo, 
			BindingResult bindingResult) {
		
		BaseRes<String> res = new BaseRes<String>();
		ServiceCode serviceCode = sysRoleService.add(sysRoleVo);
		res.setServiceCode(serviceCode);
		return res;
	}
	
	@RequestMapping(value = "modify", method = RequestMethod.POST)
	public @ResponseBody BaseRes<String> modify(
			@Validated(value = {DValid.ModifyEntity.class}) 
			@RequestBody SysRoleVo sysRoleVo, 
			BindingResult bindingResult) {
		
		BaseRes<String> res = new BaseRes<String>();
		ServiceCode serviceCode = sysRoleService.modify(sysRoleVo);
		res.setServiceCode(serviceCode);
		return res;
	}
	
}
