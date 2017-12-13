package com.cd2cd.controller;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cd2cd.comm.ServiceCode;
import com.cd2cd.domain.SysRole;
import com.cd2cd.service.SysUserService;
import com.cd2cd.vo.BaseRes;
import com.cd2cd.vo.DataPageWrapper;
import com.cd2cd.vo.ObjDataWrapper;
import com.cd2cd.vo.SysUserVo;

@Controller
@RequestMapping("sysUser")
public class SysUserController extends BaseController {

	private static Logger LOG = LoggerFactory.getLogger(SysUserController.class);
	
	@Resource
	private SysUserService sysUserService;

	@RequestMapping( value = "list", method = RequestMethod.GET)
	public @ResponseBody BaseRes<DataPageWrapper<SysUserVo>> queryPageList(
			Integer currPage, Integer pageSize, SysUserVo sysUserVo) {

		LOG.info("currPage={}, pageSize={}, sysUserVo={}", currPage, pageSize, sysUserVo);
		
		BaseRes<DataPageWrapper<SysUserVo>> res = sysUserService.list(currPage, pageSize, sysUserVo);

		return res;
	}
	
	@RequestMapping( value = "detail/{id}", method = RequestMethod.GET)
	public @ResponseBody BaseRes<ObjDataWrapper<SysUserVo, List<SysRole>, Object>> detail(@PathVariable("id") Integer id) {
		
		BaseRes<ObjDataWrapper<SysUserVo, List<SysRole>, Object>> res = new BaseRes<ObjDataWrapper<SysUserVo, List<SysRole>, Object>>();
		
		ObjDataWrapper<SysUserVo, List<SysRole>, Object> objDataWrap = sysUserService.detail(id);
		res.setData(objDataWrap);
		res.setServiceCode(ServiceCode.SUCCESS);
		
		return res;
	}
	
	@RequestMapping( value = "del/{id}", method = RequestMethod.GET)
	public @ResponseBody BaseRes<String> del(@PathVariable("id") Integer id) {
		
		BaseRes<String> res = new BaseRes<String>();
		
		boolean success = sysUserService.del(id);
		if( success ) {
			res.setServiceCode(ServiceCode.SUCCESS);
		} else {
			res.setServiceCode(ServiceCode.FAILED);
		}
		
		return res;
	}
}
