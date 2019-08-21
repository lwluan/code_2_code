package com.cd2cd.admin.controller;

import javax.annotation.Resource;

import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cd2cd.admin.domain.SysUser;
import com.cd2cd.admin.service.SysUserService;
import com.cd2cd.admin.vo.BaseRes;
import com.cd2cd.admin.vo.DataPageWrapper;
import com.cd2cd.admin.vo.SysUserVo;
import com.cd2cd.admin.vo.SysUserVo.AddEntityInfo;
import com.cd2cd.admin.vo.SysUserVo.DeleteEntity;
import com.cd2cd.admin.vo.SysUserVo.ModifyEntityInfo;

@RestController
@RequestMapping("adapi/sysUser")
public class SysUserController extends BaseController {

	@Resource
	private SysUserService sysUserService;

	@GetMapping("entityPage")
	public BaseRes<DataPageWrapper<SysUser>> entityPage(SysUserVo sysUserVo) {
		return sysUserService.entityPage(sysUserVo);
	}
	
	@GetMapping("entityInfo/{id}")
	public BaseRes<SysUser> entityInfo(@PathVariable("id") Integer id) {
		return sysUserService.entityInfo(id);
	}
	
	@DeleteMapping("entityInfo")
	public BaseRes<String> deleteEntity(
			@Validated({DeleteEntity.class}) @RequestBody SysUserVo sysUserVo,
			BindingResult bindingResult) {
		return sysUserService.deleteEntity(sysUserVo.getId());
	}
	
	@PostMapping("entityInfo")
	public BaseRes<String> addEntityInfo(
			@Validated({AddEntityInfo.class}) @RequestBody SysUserVo sysUserVo, 
			BindingResult bindingResult) {
		return sysUserService.addEntityInfo(sysUserVo);
	}
	
	@PutMapping("entityInfo")
	public BaseRes<String> modifyEntityInfo(
			@Validated({ModifyEntityInfo.class}) 
			@RequestBody SysUserVo sysUserVo, 
			BindingResult bindingResult) {
		return sysUserService.modifyEntityInfo(sysUserVo);
	}
}
