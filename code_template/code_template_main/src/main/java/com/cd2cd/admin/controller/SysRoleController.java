package com.cd2cd.admin.controller;

import java.util.List;

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

import com.cd2cd.admin.domain.SysAuthority;
import com.cd2cd.admin.domain.SysRole;
import com.cd2cd.admin.service.SysRoleService;
import com.cd2cd.admin.vo.BaseRes;
import com.cd2cd.admin.vo.PageData;
import com.cd2cd.admin.vo.SysRoleVo;
import com.cd2cd.admin.vo.SysRoleVo.AddEntityInfo;
import com.cd2cd.admin.vo.SysRoleVo.DeleteEntity;
import com.cd2cd.admin.vo.SysRoleVo.ModifyEntityInfo;

@RestController
@RequestMapping("adapi/sysRole")
public class SysRoleController extends BaseController {

	@Resource
	private SysRoleService sysRoleService;
	
	@GetMapping("entityPage")
	public BaseRes<PageData<SysRole>> entityPage(SysRoleVo sysUserVo) {
		return sysRoleService.entityPage(sysUserVo);
	}
	
	@GetMapping("entityInfo/{id}")
	public BaseRes<SysRole> entityInfo(@PathVariable("id") Integer id) {
		return sysRoleService.entityInfo(id);
	}
	
	@DeleteMapping("entityInfo")
	public BaseRes<String> deleteEntity(
			@Validated({DeleteEntity.class}) @RequestBody SysRoleVo sysUserVo,
			BindingResult bindingResult) {
		return sysRoleService.deleteEntity(sysUserVo.getId());
	}
	
	@PostMapping("entityInfo")
	public BaseRes<String> addEntityInfo(
			@Validated({AddEntityInfo.class}) @RequestBody SysRoleVo sysUserVo, 
			BindingResult bindingResult) {
		return sysRoleService.addEntityInfo(sysUserVo);
	}
	
	@PutMapping("entityInfo")
	public BaseRes<String> modifyEntityInfo(
			@Validated({ModifyEntityInfo.class}) 
			@RequestBody SysRoleVo sysUserVo, 
			BindingResult bindingResult) {
		return sysRoleService.modifyEntityInfo(sysUserVo);
	}
	
	@GetMapping("queryAllAuthoritys")
	public BaseRes<List<SysAuthority>> queryAllAuthoritys() {
		return sysRoleService.queryAllAuthoritys();
	}
}
