package com.cd2cd.controller;

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

import com.cd2cd.comm.ServiceCode;
import com.cd2cd.service.ProProjectService;
import com.cd2cd.vo.BaseRes;
import com.cd2cd.vo.DataPageWrapper;
import com.cd2cd.vo.ProModuleVo;
import com.cd2cd.vo.ProProjectVo;

@Controller
@RequestMapping("proProject")
public class ProProjectController extends BaseController {

	private static Logger LOG = LoggerFactory.getLogger(ProProjectController.class);

	@Resource
	private ProProjectService proProjectService;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public @ResponseBody BaseRes<DataPageWrapper<ProProjectVo>> queryPageList(Integer currPage, Integer pageSize, ProProjectVo proProjectVo) {

		LOG.info("currPage={}, pageSize={}", currPage, pageSize);
		
		BaseRes<DataPageWrapper<ProProjectVo>> res = proProjectService.list(currPage, pageSize, proProjectVo);

		return res;
	}
	
	@RequestMapping(value = "projectList", method = RequestMethod.GET)
	public @ResponseBody BaseRes<List<ProProjectVo>> projectList() {

		BaseRes<List<ProProjectVo>> res = proProjectService.projectList();

		return res;
	}

	@RequestMapping(value = "projectInfo", method = RequestMethod.GET)
	public @ResponseBody BaseRes<ProProjectVo> detail(Long id) {

		BaseRes<ProProjectVo> res = new BaseRes<ProProjectVo>();

		ProProjectVo mProProjectVo = proProjectService.detail(id);
		res.setData(mProProjectVo);
		res.setServiceCode(ServiceCode.SUCCESS);

		return res;
	}

	@RequestMapping(value = "projectInfo", method = RequestMethod.DELETE)
	public @ResponseBody BaseRes<String> delProjectInfo(Long id) {

		BaseRes<String> res = new BaseRes<String>();

		boolean success = proProjectService.del(id);
		if (success) {
			res.setServiceCode(ServiceCode.SUCCESS);
		} else {
			res.setServiceCode(ServiceCode.FAILED);
		}

		return res;
	}

	@RequestMapping(value = "projectInfo", method = RequestMethod.POST)
	public @ResponseBody BaseRes<String> addProjectInfo(@Validated @RequestBody ProProjectVo proProjectVo, BindingResult bindingResult) {

		BaseRes<String> res = new BaseRes<String>();
		ServiceCode serviceCode = proProjectService.add(proProjectVo);
		res.setServiceCode(serviceCode);
		return res;
	}

	@RequestMapping(value = "projectInfo", method = RequestMethod.PUT)
	public @ResponseBody BaseRes<String> modifyProjectInfo(@Validated @RequestBody ProProjectVo proProjectVo, BindingResult bindingResult) {

		BaseRes<String> res = new BaseRes<String>();
		ServiceCode serviceCode = proProjectService.modify(proProjectVo);
		res.setServiceCode(serviceCode);
		return res;
	}

	@RequestMapping(value = "genProject", method = RequestMethod.POST)
	public @ResponseBody BaseRes<String> genProject(@Validated @RequestBody ProProjectVo proProjectVo, BindingResult bindingResult) {

		proProjectService.modify(proProjectVo);
		BaseRes<String> res = proProjectService.genProject(proProjectVo.getId());

		return res;
	}

	
	// TODO - - - - - modele - - - - -
	@RequestMapping(value = "moduleList", method = RequestMethod.GET)
	public @ResponseBody BaseRes<DataPageWrapper<ProModuleVo>> moduleList(Integer currPage, Integer pageSize, ProModuleVo proModuleVo) {

		BaseRes<DataPageWrapper<ProModuleVo>> res = proProjectService.moduleList(currPage, pageSize, proModuleVo);

		return res;
	}
	
	@RequestMapping(value = "projectModuleList", method = RequestMethod.GET)
	public @ResponseBody BaseRes<List<ProModuleVo>> projectModuleList(Long projectId) {

		BaseRes<List<ProModuleVo>> res = proProjectService.projectModuleList(projectId);

		return res;
	}

	@RequestMapping(value = "moduleDetail/{id}", method = RequestMethod.GET)
	public @ResponseBody BaseRes<ProModuleVo> moduleDetail(@PathVariable("id") Long id) {

		BaseRes<ProModuleVo> res = new BaseRes<ProModuleVo>();

		ProModuleVo mProModuleVo = proProjectService.moduleDetail(id);
		res.setData(mProModuleVo);
		res.setServiceCode(ServiceCode.SUCCESS);

		return res;
	}

	@RequestMapping(value = "delModule/{id}", method = RequestMethod.GET)
	public @ResponseBody BaseRes<String> delModule(@PathVariable("id") Long id) {

		BaseRes<String> res = new BaseRes<String>();

		boolean success = proProjectService.delModule(id);
		if (success) {
			res.setServiceCode(ServiceCode.SUCCESS);
		} else {
			res.setServiceCode(ServiceCode.FAILED);
		}

		return res;
	}

	@RequestMapping(value = "addModule", method = RequestMethod.POST)
	public @ResponseBody BaseRes<String> addModule(@Validated @RequestBody ProModuleVo proModuleVo, BindingResult bindingResult) {

		BaseRes<String> res = new BaseRes<String>();
		ServiceCode serviceCode = proProjectService.addModule(proModuleVo);
		res.setServiceCode(serviceCode);
		return res;
	}

	@RequestMapping(value = "modifyModule", method = RequestMethod.POST)
	public @ResponseBody BaseRes<String> modify(@Validated @RequestBody ProModuleVo proModuleVo, BindingResult bindingResult) {

		BaseRes<String> res = new BaseRes<String>();
		ServiceCode serviceCode = proProjectService.modifyModule(proModuleVo);
		res.setServiceCode(serviceCode);
		return res;
	}

}
