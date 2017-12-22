package com.cd2cd.controller;

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

import com.cd2cd.comm.DValid;
import com.cd2cd.comm.ServiceCode;
import com.cd2cd.service.ProProjectService;
import com.cd2cd.vo.BaseRes;
import com.cd2cd.vo.DataPageWrapper;
import com.cd2cd.vo.ProProjectVo;

@Controller
@RequestMapping("proProject")
public class ProProjectController extends BaseController {

	private static Logger LOG = LoggerFactory.getLogger(ProProjectController.class);
	
	@Resource
	private ProProjectService proProjectService;

	@RequestMapping( value = "list", method = RequestMethod.GET)
	public @ResponseBody BaseRes<DataPageWrapper<ProProjectVo>> queryPageList(
			Integer currPage, Integer pageSize, ProProjectVo proProjectVo) {

		LOG.info("currPage={}, pageSize={}, proProjectVo={}", currPage, pageSize, proProjectVo);
		
		BaseRes<DataPageWrapper<ProProjectVo>> res = proProjectService.list(currPage, pageSize, proProjectVo);

		return res;
	}
	
	@RequestMapping( value = "detail/{id}", method = RequestMethod.GET)
	public @ResponseBody BaseRes<ProProjectVo> detail(@PathVariable("id") Long id) {
		
		BaseRes<ProProjectVo> res = new BaseRes<ProProjectVo>();
		
		ProProjectVo mProProjectVo = proProjectService.detail(id);
		res.setData(mProProjectVo);
		res.setServiceCode(ServiceCode.SUCCESS);
		
		return res;
	}
	
	@RequestMapping( value = "del/{id}", method = RequestMethod.GET)
	public @ResponseBody BaseRes<String> del(@PathVariable("id") Long id) {
		
		BaseRes<String> res = new BaseRes<String>();
		
		boolean success = proProjectService.del(id);
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
			@RequestBody ProProjectVo proProjectVo, 
			BindingResult bindingResult) {
		
		BaseRes<String> res = new BaseRes<String>();
		ServiceCode serviceCode = proProjectService.add(proProjectVo);
		res.setServiceCode(serviceCode);
		return res;
	}
	
	@RequestMapping(value = "modify", method = RequestMethod.POST)
	public @ResponseBody BaseRes<String> modify(
			@Validated(value = {DValid.ModifyEntity.class}) 
			@RequestBody ProProjectVo proProjectVo, 
			BindingResult bindingResult) {
		
		BaseRes<String> res = new BaseRes<String>();
		ServiceCode serviceCode = proProjectService.modify(proProjectVo);
		res.setServiceCode(serviceCode);
		return res;
	}
	
	@RequestMapping(value = "genProject", method = RequestMethod.POST)
	public @ResponseBody BaseRes<String> genProject(
			@Validated(value = {DValid.ModifyEntity.class}) 
			@RequestBody ProProjectVo proProjectVo, 
			BindingResult bindingResult) {
		
		proProjectService.modify(proProjectVo);
		BaseRes<String> res = proProjectService.genProject(proProjectVo.getId());
		
		return res;
	}
}
