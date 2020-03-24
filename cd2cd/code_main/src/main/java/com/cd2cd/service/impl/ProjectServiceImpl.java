package com.cd2cd.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.cd2cd.domain.*;
import com.cd2cd.domain.gen.*;
import com.cd2cd.mapper.*;
import com.cd2cd.service.SysUserService;
import com.cd2cd.vo.*;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.ResultMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.JavaTypeResolver;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.internal.types.JavaTypeResolverDefaultImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.cd2cd.comm.ServiceCode;
import com.cd2cd.dom.java.TypeEnum;
import com.cd2cd.dom.java.TypeEnum.CollectionType;
import com.cd2cd.dom.java.TypeEnum.FieldDataType;
import com.cd2cd.dom.java.TypeEnum.FileTypeEnum;
import com.cd2cd.dom.java.TypeEnum.FunArgType;
import com.cd2cd.dom.java.TypeEnum.PackageTypeEnum;
import com.cd2cd.domain.gen.ProFileCriteria;
import com.cd2cd.service.ProjectService;
import com.cd2cd.util.BeanUtil;
import com.cd2cd.util.Constants.ProjectModuleTypeEnum;
import com.cd2cd.util.ObjectTypeUtil;
import com.cd2cd.util.mbg.Constants;

import javax.annotation.Resource;

@Service
public class ProjectServiceImpl implements ProjectService {
	private static final Logger LOG = LoggerFactory.getLogger(ProjectServiceImpl.class);
	@Autowired
	ProProjectMapper proProjectMapper;
	@Autowired
	ProModuleMapper proModuleMapper;
	@Autowired
	ProFileMapper proFileMapper;
	@Autowired
	ProFieldMapper proFieldMapper;
	@Autowired
	ProTableColumnMapper proTableColumnMapper;
	@Autowired
	ProProjectDatabaseRelMapper proProjectDatabaseRelMapper;
	@Autowired
	ProTableMapper proTableMapper;
	@Autowired
	ProFunMapper proFunMapper;
	@Autowired
	ProPageMapper proPageMapper;
	@Autowired
	ProFunArgMapper proFunArgMapper;
	@Autowired
	CommValidateMapper commValidateMapper;

	@Resource
	ProMicroServiceMapper microServiceMapper;
	/**
	 * fdfdsafa
	 */
	static {
		int i=0;
	}

	/**
	 * fdafdafdsf
	 */
	{
		int aa=0;
	}

	/**
	 * fdsa
	 * fdsaf
	 * fds
	 * afds
	 * afds
	 * fds;
	 * @param projectId
	 * @param packageType
	 * @param moduleId
	 * @return
	 */
	public BaseRes<String> fetchProjectFileTreeOld(Long projectId, String packageType, Long moduleId) {

		ProProject mProProject = proProjectMapper.selectByPrimaryKey(projectId);

		if (mProProject == null) {
			return new BaseRes<>(ServiceCode.NOT_EXISTS_PROJECT);
		}


		/** 微服务项目 */


		ProModule commProModule = new ProModule();
		/**
		 * moduleId: -1:为公共模块, 0:全部 example: com.test.controller
		 */
		List<ProModule> modules = new ArrayList<ProModule>();
		if (moduleId == null || moduleId == 0) {
			// fetch All module
			ProModuleCriteria mProModuleCriteria = new ProModuleCriteria();
			mProModuleCriteria.createCriteria().andProjectIdEqualTo(projectId);
			modules = proModuleMapper.selectByExample(mProModuleCriteria);
			modules.add(commProModule);

		} else if (moduleId == -1) {

			modules.add(commProModule);

		} else {
			// fetch one module
			modules = new ArrayList<ProModule>();
			ProModule mProModule = proModuleMapper.selectByPrimaryKey(moduleId);
			modules.add(mProModule);
		}

		TreeId treeId = new TreeId();

		// construct tree of json
		JSONArray rootArray = new JSONArray();
		try {

			JSONObject src = newJson(treeId, null, "src", "folder", 0L);
			src.put("open", true);

			rootArray.put(src);

			if (PackageTypeEnum.Flat.name().equals(packageType)) {
				processProjectByFlat(rootArray, treeId, src, mProProject, modules, null);
			} else {
				processProjectByHierarchical(rootArray, treeId, src, mProProject, modules, null);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return new BaseRes<>(rootArray.toString(), ServiceCode.SUCCESS);
	}

	@Override
	public BaseRes<String> fetchProjectFileTree(Long projectId, String packageType, Long moduleId) {

		ProProject mProProject = proProjectMapper.selectByPrimaryKey(projectId);

		if (mProProject == null) {
			return new BaseRes<>(ServiceCode.NOT_EXISTS_PROJECT);
		}

		TreeId treeId = new TreeId();
		JSONArray rootArray = new JSONArray();

		/** 微服务项目 */
		if("micro".equalsIgnoreCase(mProProject.getProType())) {
			ProMicroServiceCriteria example = new ProMicroServiceCriteria();
			example.createCriteria().andDelFlagEqualTo(0).andProjectIdEqualTo(projectId);
			example.setOrderByClause(" name asc ");
			List<ProMicroService> micros = microServiceMapper.selectByExample(example);

			for(ProMicroService ms: micros) {
				JSONObject project = getProjectTreeBy(treeId, mProProject, packageType, moduleId, rootArray, ms);
				rootArray.put(project);
			}

		} else {
			JSONObject project = getProjectTreeBy(treeId, mProProject, packageType, moduleId, rootArray, null);
			rootArray.put(project);
		}

		return new BaseRes<>(rootArray.toString(), ServiceCode.SUCCESS);
	}

	private JSONObject getProjectTreeBy(TreeId treeId, ProProject mProProject, String packageType, Long moduleId, JSONArray rootArray, ProMicroService microService) {

		Long projectId = mProProject.getId();

		ProModule commProModule = new ProModule();
		/**
		 * moduleId: -1:为公共模块, 0:全部 example: com.test.controller
		 */
		Long microId = 0L;
		if(null != microService) {
			microId = microService.getId();
			commProModule.setMicroId(microId);
		}

		List<ProModule> modules = new ArrayList<ProModule>();
		if (moduleId == null || moduleId == 0) {
			// fetch All module
			ProModuleCriteria mProModuleCriteria = new ProModuleCriteria();
			ProModuleCriteria.Criteria mCriteria = mProModuleCriteria.createCriteria();
			mCriteria.andProjectIdEqualTo(projectId);

			if(null != microService) {
				mCriteria.andMicroIdEqualTo(microService.getId());
				microId = microService.getId();
			}

			modules = proModuleMapper.selectByExample(mProModuleCriteria);
			modules.add(commProModule);

		} else if (moduleId == -1) {

			modules.add(commProModule);

		} else {
			// fetch one module
			modules = new ArrayList<ProModule>();
			ProModule mProModule = proModuleMapper.selectByPrimaryKey(moduleId);
			modules.add(mProModule);
		}

		try {

			String srcName = "src";
			if(null != microService) {
				srcName = microService.getArtifactId()+"("+microService.getName()+")";
			}

			JSONObject src = newJson(treeId, null, srcName, "folder", microId);
			src.put("open", true);

			if (PackageTypeEnum.Flat.name().equals(packageType)) {
				processProjectByFlat(rootArray, treeId, src, mProProject, modules, microService);
			} else {
				processProjectByHierarchical(rootArray, treeId, src, mProProject, modules, microService);
			}

			return src;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	/** Package Type Hierarchical */
	private void processProjectByHierarchical(JSONArray rootArray, TreeId treeId, JSONObject src, ProProject proProject, List<ProModule> modules, ProMicroService microService) throws JSONException {

		// root module-_package
		Long projectId = proProject.getId();
		String groupId = proProject.getGroupId();
		String artifctId = proProject.getArtifactId();

		Long microId = 0L;
		String basePkgName = groupId + "." + artifctId;
		if(microService != null) {
			microId = microService.getId();
			basePkgName = groupId + "." + microService.getArtifactId().replaceAll("-", "_");
		}
		int pId = src.getInt("id");

		JSONObject artifctIdNode = newJson(treeId, pId, basePkgName, "package", microId);
		artifctIdNode.put("open", true);
		pId = artifctIdNode.getInt("id");
		rootArray.put(artifctIdNode);

		JSONObject controllerNode = null;
		JSONObject serviceNode = null;
		JSONObject mapperNode = null;
		JSONObject voNode = null;
		JSONObject domainNode = null;

		for (int i = 0; i < modules.size(); i++) {

			ProModule module = modules.get(i);
			Long moduleId = module.getId();

			// query file list, the same controller\service\vo\dmain\mapper\page
			ProFileCriteria mProFileCriteria = new ProFileCriteria();
			ProFileCriteria.Criteria mCriteria = mProFileCriteria.createCriteria();

			Long moduleMicroId = module.getMicroId();


			if (null != moduleId && moduleId > 0) {
				mCriteria.andProjectIdEqualTo(projectId);
				mCriteria.andModuleIdEqualTo(moduleId);
				if(moduleMicroId != null) {
					// 单项目为空
					mCriteria.andMicroIdEqualTo(moduleMicroId);
				}
			} else {

				// 或者是当前项目的模块ID为空 - - - 多服务问题
				ProFileCriteria.Criteria orCriteria1 = mProFileCriteria.or();
//				ProFileCriteria.Criteria orCriteria2 = mProFileCriteria.or();

				// 所有项目公用 projectId为空
				mCriteria.andProjectIdIsNull();


				orCriteria1.andModuleIdIsNull().andProjectIdEqualTo(projectId);

				if(moduleMicroId != null) {
					// 单项目为空
					orCriteria1.andMicroIdEqualTo(moduleMicroId);
//					orCriteria2.andMicroIdEqualTo(moduleMicroId);
				}
			}

			List<ProFile> proFiles = proFileMapper.selectByExample(mProFileCriteria);

			LOG.info("proFiles-size={}", proFiles.size());
			
			Integer modulePid = pId;

			// package -_ file
			if (ProjectModuleTypeEnum.standard.name().equals(proProject.getPackageType())) {
				if (controllerNode == null) {
					controllerNode = newJson(treeId, modulePid, "controller", "package", microId);
					serviceNode = newJson(treeId, modulePid, "service", "package", microId);
					mapperNode = newJson(treeId, modulePid, "mapper", "package", microId);
					voNode = newJson(treeId, modulePid, "vo", "package", microId);
					domainNode = newJson(treeId, modulePid, "domain", "package", microId);

					rootArray.put(controllerNode);
					rootArray.put(serviceNode);
					rootArray.put(mapperNode);
					rootArray.put(voNode);
					rootArray.put(domainNode);
				}
			} else {
				String _module = module.getName();
				if (StringUtils.isNotEmpty(_module)) {
					JSONObject moduleNode = newJson(treeId, pId, _module, "package", microId);
					modulePid = moduleNode.getInt("id");
					rootArray.put(moduleNode);
				} else {
					_module = "";
				}

				controllerNode = newJson(treeId, modulePid, _module, "controller", "package", microId);
				
				LOG.info("_module={}", _module);
				
				serviceNode = newJson(treeId, modulePid, _module, "service", "package", microId);
				mapperNode = newJson(treeId, modulePid, _module, "mapper", "package", microId);
				voNode = newJson(treeId, modulePid, _module, "vo", "package", microId);
				domainNode = newJson(treeId, modulePid, _module, "domain", "package", microId);

				rootArray.put(controllerNode);
				rootArray.put(serviceNode);
				rootArray.put(mapperNode);
				rootArray.put(voNode);
				rootArray.put(domainNode);
			}

			Integer controllerNodeId = controllerNode.getInt("id");
			Integer serviceNodeId = serviceNode.getInt("id");
			Integer mapperNodeId = mapperNode.getInt("id");
			Integer voNodeId = voNode.getInt("id");
			Integer domainNodeId = domainNode.getInt("id");

			for (ProFile file : proFiles) {

				// module name within in package name
				LOG.info("fileName={}", file.getName());
				
				String fileType = file.getFileType();
				String classType = file.getClassType();
				JSONObject temp = null;
				Integer _pId = null;

				if (FileTypeEnum.controller.eq(fileType)) {
					_pId = controllerNodeId;
				} else if (FileTypeEnum.service.eq(fileType)) {
					_pId = serviceNodeId;
				} else if (FileTypeEnum.mapper.eq(fileType)) {
					_pId = mapperNodeId;
				} else if (FileTypeEnum.vo.eq(fileType)) {
					_pId = voNodeId;
				} else if (FileTypeEnum.domain.eq(fileType)) {
					_pId = domainNodeId;
				}

				temp = newJson(treeId, _pId, file.getName() + ".java", fileType, file.getId(), microId);
				temp.put("classType", classType);
				rootArray.put(temp);
			}
		}

	}

	/** Package Type Flat */
	private void processProjectByFlat(JSONArray rootArray, TreeId treeId, JSONObject src, ProProject proProject, List<ProModule> modules, ProMicroService microService)
			throws JSONException {

		Long microId = 0L;
		Long projectId = proProject.getId();
		String groupId = proProject.getGroupId();
		String artifctId = proProject.getArtifactId();

		String basePkgName = groupId + "." + artifctId;
		if(microService != null) {
			microId = microService.getId();
			basePkgName = groupId + "." + microService.getArtifactId().replaceAll("-", "_");
		}

		int srcId = src.getInt("id");

		JSONObject controllerNode = null;
		JSONObject serviceNode = null;
		JSONObject mapperNode = null;
		JSONObject voNode = null;
		JSONObject domainNode = null;

		for (ProModule module : modules) {

			Long moduleId = module.getId();

			// query file list, the same controller\service\vo\dmain\mapper\page
			ProFileCriteria mProFileCriteria = new ProFileCriteria();
			ProFileCriteria.Criteria mCriteria = mProFileCriteria.createCriteria();

			Long moduleMicroId = module.getMicroId();
			if(moduleMicroId != null) {
				// 单项目为空
				mCriteria.andMicroIdEqualTo(moduleMicroId);
			}

			if (moduleId != null && moduleId > 0) {
				mCriteria.andProjectIdEqualTo(projectId);
				mCriteria.andModuleIdEqualTo(moduleId);
			} else {

				// 所有项目公用 projectId为空
				mCriteria.andProjectIdIsNull();

				// 或者是当前项目的模块ID为空
				mProFileCriteria.or().andModuleIdIsNull().andProjectIdEqualTo(projectId);
			}

			List<ProFile> proFiles = proFileMapper.selectByExample(mProFileCriteria);

			// package -_ file

			if (ProjectModuleTypeEnum.standard.name().equals(proProject.getPackageType())) {
				if (controllerNode == null) {
					controllerNode = newJson(treeId, srcId, basePkgName + ".controller", "package", microId);
					serviceNode = newJson(treeId, srcId, basePkgName + ".service", "package", microId);
					mapperNode = newJson(treeId, srcId, basePkgName + ".mapper", "package", microId);
					voNode = newJson(treeId, srcId, basePkgName + ".vo", "package", microId);
					domainNode = newJson(treeId, srcId, basePkgName + ".domain", "package", microId);

					rootArray.put(controllerNode);
					rootArray.put(serviceNode);
					rootArray.put(mapperNode);
					rootArray.put(voNode);
					rootArray.put(domainNode);
				}
			} else {
				String _basePkgName = basePkgName;
				if (StringUtils.isNotEmpty(module.getName())) {
					_basePkgName = _basePkgName + "." + module.getName();
				}

				controllerNode = newJson(treeId, srcId, _basePkgName + ".controller", "package", microId);
				serviceNode = newJson(treeId, srcId, _basePkgName + ".service", "package", microId);
				mapperNode = newJson(treeId, srcId, _basePkgName + ".mapper", "package", microId);
				voNode = newJson(treeId, srcId, _basePkgName + ".vo", "package", microId);
				domainNode = newJson(treeId, srcId, _basePkgName + ".domain", "package", microId);

				rootArray.put(controllerNode);
				rootArray.put(serviceNode);
				rootArray.put(mapperNode);
				rootArray.put(voNode);
				rootArray.put(domainNode);
			}

			Integer controllerNodeId = controllerNode.getInt("id");
			Integer serviceNodeId = serviceNode.getInt("id");
			Integer mapperNodeId = mapperNode.getInt("id");
			Integer voNodeId = voNode.getInt("id");
			Integer domainNodeId = domainNode.getInt("id");

			for (ProFile file : proFiles) {

				System.out.println("fileName=" + file.getName());
				// module name within in package name

				String fileType = file.getFileType();
				String classType = file.getClassType();
				JSONObject temp = null;
				Integer pId = null;

				if (FileTypeEnum.controller.eq(fileType)) {
					pId = controllerNodeId;
				} else if (FileTypeEnum.service.eq(fileType)) {
					pId = serviceNodeId;
				} else if (FileTypeEnum.mapper.eq(fileType)) {
					pId = mapperNodeId;
				} else if (FileTypeEnum.vo.eq(fileType)) {
					pId = voNodeId;
				} else if (FileTypeEnum.domain.eq(fileType)) {
					pId = domainNodeId;
				}

				temp = newJson(treeId, pId, file.getName() + ".java", fileType, file.getId(), microId);
				temp.put("classType", classType);
				rootArray.put(temp);
			}
		}
	}

	private JSONObject newJson(TreeId treeId, Integer pId, String name, String fileType, Long microId) throws JSONException {
		return this.newJson(treeId, pId, "", name, fileType, 0l, microId);
	}

	private JSONObject newJson(TreeId treeId, Integer pId, String moduleName, String name, String fileType, Long microId) throws JSONException {
		return this.newJson(treeId, pId, moduleName, name, fileType, 0L, microId);
	}

	private JSONObject newJson(TreeId treeId, Integer pId, String moduleName, String name, String fileType, Long fileId, Long microId)
			throws JSONException {
		treeId.index++;

		JSONObject packageNode = new JSONObject();
		packageNode.put("id", treeId.index);
		packageNode.put("indentify", moduleName + "_" + name);
		packageNode.put("pId", pId);
		packageNode.put("fileId", fileId);
		packageNode.put("name", name);
		packageNode.put("fileType", fileType);
		packageNode.put("microId", microId);
		return packageNode;

	}

	private JSONObject newJson(TreeId treeId, Integer pId, String name, String fileType, Long fileId, Long microId) throws JSONException {
		return this.newJson(treeId, pId, "", name, fileType, fileId, microId);
	}

	static class cccc {

	}

	private class TreeId {
		public Integer index = 1;
	}

	@Override
	public BaseRes<List<ProTableVo>> fetchTableListByProjectHasDb(Long projectId, String from) {

		BaseRes<List<ProTableVo>> res = new BaseRes<List<ProTableVo>>();
		// 1、获取所关联数据库
		ProProjectDatabaseRelCriteria mProProjectDatabaseRelCriteria = new ProProjectDatabaseRelCriteria();
		mProProjectDatabaseRelCriteria.createCriteria().andProjectIdEqualTo(projectId);
		List<ProProjectDatabaseRel> dbs = proProjectDatabaseRelMapper.selectByExample(mProProjectDatabaseRelCriteria);

		// 2、获取数据库关联表
		if (dbs.size() > 0) {
			List<Long> ids = new ArrayList<Long>();
			for (ProProjectDatabaseRel db : dbs) {
				ids.add(db.getDatabaseId());
			}

			ProTableCriteria mProTableCriteria = new ProTableCriteria();
			mProTableCriteria.createCriteria().andDatabaseIdIn(ids);
			List<ProTable> tables = proTableMapper.selectByExample(mProTableCriteria);

			if (tables.size() > 0) {

				List<ProTableVo> tabVos = BeanUtil.voConvertList(tables, ProTableVo.class);
				if ("vo".equalsIgnoreCase(from)) {
					ProProject proProject = proProjectMapper.selectByPrimaryKey(projectId);
					String ignoreTables = proProject.getIgnoreTables(); // 忽略生成表

					tabVos = tabVos.stream().filter(table -> ignoreTables.indexOf("\"" + table.getId() + "\"") < 0)
							.collect(Collectors.toList());
				}

				res.setData(tabVos);
			}
		}

		res.setServiceCode(ServiceCode.SUCCESS);
		return res;
	}

	@Override
	public BaseRes<String> addFile(ProFileVo proFileVo) {

		proFileVo.setCreateTime(new Date());
		proFileVo.setUpdateTime(new Date());

		// 公共模块
		if (proFileVo.getModuleId() == null || proFileVo.getModuleId() == 0) {
			proFileVo.setModuleId(null);
		}

		BaseRes<String> res = new BaseRes<String>();

		int effect = proFileMapper.insertSelective(proFileVo);
		if (effect > 0) {
			res.setServiceCode(ServiceCode.SUCCESS);
		} else {
			res.setServiceCode(ServiceCode.FAILED);
		}
		return res;
	}

	@Override
	public BaseRes<ProFileVo> fetchFileInfo(Long fileId) {

		BaseRes<ProFileVo> res = new BaseRes<ProFileVo>();
		ProFile proFile = proFileMapper.selectByPrimaryKey(fileId);

		ProFileVo mProFileVo = BeanUtil.voConvert(proFile, ProFileVo.class);

		ProFieldCriteria mProFieldCriteria = new ProFieldCriteria();
		mProFieldCriteria.createCriteria().andFileIdEqualTo(fileId);
		List<ProField> proFields = proFieldMapper.selectByExample(mProFieldCriteria);

		mProFileVo.setFields(proFields);
		if (proFile.getSuperId() != null && proFile.getSuperId() > 0) {

			if (Constants.FileType.VO.equals(proFile.getFileType())) {

				// fetch table
				ProTable proTable = proTableMapper.selectByPrimaryKey(proFile.getSuperId());

				ProTableColumnCriteria mProTableColumnCriteria = new ProTableColumnCriteria();
				mProTableColumnCriteria.createCriteria().andTableIdEqualTo(proTable.getId());
				List<ProTableColumn> columns = proTableColumnMapper.selectByExample(mProTableColumnCriteria);
				ProTableVo table = BeanUtil.voConvert(proTable, ProTableVo.class);
				table.setColumns(columns);
				mProFileVo.setTable(table);
			}
		}

		res.setData(mProFileVo);
		res.setServiceCode(ServiceCode.SUCCESS);
		return res;
	}

	@Override
	public BaseRes<ProFieldVo> saveOrUpdateFieldToFile(ProFieldVo fileVo) {

		fileVo.setUpdateTime(new Date());
		int effect = 0;
		// update
		if (fileVo.getId() != null) {

			effect = proFieldMapper.updateByPrimaryKeySelective(fileVo);
		} else {
			// to save
			fileVo.setCreateTime(new Date());
			effect = proFieldMapper.insert(fileVo);
		}

		LOG.debug("effect={}", effect);

		BaseRes<ProFieldVo> res = new BaseRes<ProFieldVo>();
		res.setData(fileVo);
		res.setServiceCode(ServiceCode.SUCCESS);
		return res;
	}

	@Override
	public BaseRes<String> delFieldFromFile(Long id) {
		proFieldMapper.deleteByPrimaryKey(id);
		BaseRes<String> res = new BaseRes<String>();
		res.setServiceCode(ServiceCode.SUCCESS);
		return res;
	}

	@Override
	public BaseRes<List<ProFileVo>> fetchFileByClassType(Long projectId, String fileType) {

		ProFileCriteria mProFileCriteria = new ProFileCriteria();
		ProFileCriteria.Criteria criteria = mProFileCriteria.createCriteria();

		criteria.andProjectIdEqualTo(projectId).andFileTypeEqualTo(fileType);

		// 获取 公供 VO
		if (FileTypeEnum.vo.name().equalsIgnoreCase(fileType)) {
			mProFileCriteria.or().andProjectIdIsNull();
		}

		LOG.info("projectId={}, fileType={}", projectId, fileType);

		List<ProFile> files = proFileMapper.selectByExample(mProFileCriteria);

		BaseRes<List<ProFileVo>> res = new BaseRes<List<ProFileVo>>();
		List<ProFileVo> data = BeanUtil.voConvertList(files, ProFileVo.class);
		res.setData(data);
		res.setServiceCode(ServiceCode.SUCCESS);
		return res;
	}

	@Override
	public BaseRes<ProFileVo> modifyFileInfo(ProFileVo proFileVo) {

		int effected = proFileMapper.updateByPrimaryKeySelective(proFileVo);
		LOG.info("effected={}", effected);

		BaseRes<ProFileVo> res = new BaseRes<ProFileVo>();
		res.setServiceCode(ServiceCode.SUCCESS);
		return res;
	}

	@Override
	public BaseRes<List<ProTableVo>> fetchAllTablesByProject(Long projectId) {
		/**
		 * fetch talbes from db of project
		 */

		List<Long> dbIds = new ArrayList<Long>();

		ProProjectDatabaseRelCriteria mProProjectDatabaseRelCriteria = new ProProjectDatabaseRelCriteria();
		mProProjectDatabaseRelCriteria.createCriteria().andProjectIdEqualTo(projectId);
		List<ProProjectDatabaseRel> pdrs = proProjectDatabaseRelMapper.selectByExample(mProProjectDatabaseRelCriteria);
		for (ProProjectDatabaseRel pdr : pdrs) {
			dbIds.add(pdr.getDatabaseId());
		}

		ProTableCriteria mProTableCriteria = new ProTableCriteria();
		mProTableCriteria.createCriteria().andDatabaseIdIn(dbIds);
		List<ProTable> tables = proTableMapper.selectByExample(mProTableCriteria);
		List<ProTableVo> tablesVo = BeanUtil.voConvertList(tables, ProTableVo.class);

		BaseRes<List<ProTableVo>> res = new BaseRes<List<ProTableVo>>();
		res.setData(tablesVo);
		res.setServiceCode(ServiceCode.SUCCESS);
		return res;
	}

	@Override
	public BaseRes<ProTableVo> fetchColumnsByTableId(Long tableId) {

		ProTable mProTable = proTableMapper.selectByPrimaryKey(tableId);

		ProTableColumnCriteria mProTableColumnCriteria = new ProTableColumnCriteria();
		mProTableColumnCriteria.createCriteria().andTableIdEqualTo(tableId);
		List<ProTableColumn> columns = proTableColumnMapper.selectByExample(mProTableColumnCriteria);
		ProTableVo mProTableVo = BeanUtil.voConvert(mProTable, ProTableVo.class);
		mProTableVo.setColumns(columns);

		BaseRes<ProTableVo> res = new BaseRes<ProTableVo>();
		res.setData(mProTableVo);
		res.setServiceCode(ServiceCode.SUCCESS);
		return res;
	}

	@Override
	public BaseRes<String> delFileById(Long fileId) {
		proFileMapper.deleteByPrimaryKey(fileId);

		BaseRes<String> res = new BaseRes<String>();
		res.setServiceCode(ServiceCode.SUCCESS);
		return res;
	}

	@Override
	public BaseRes<List<ProFunVo>> fetchFunsByFileId(Long fileId) {
		ProFunCriteria mProFunCriteria = new ProFunCriteria();
		mProFunCriteria.createCriteria().andCidEqualTo(fileId);
		List<ProFun> funs = proFunMapper.selectByExample(mProFunCriteria);

		List<ProFunVo> funVos = BeanUtil.voConvertList(funs, ProFunVo.class);
		BaseRes<List<ProFunVo>> res = new BaseRes<List<ProFunVo>>();
		res.setData(funVos);
		res.setServiceCode(ServiceCode.SUCCESS);
		return res;
	}

	@Override
	public BaseRes<ProFunVo> addFunction(ProFunVo proFunVo) {
		proFunVo.setCreateTime(new Date());
		proFunVo.setUpdateTime(new Date());
		int effect = proFunMapper.insertSelective(proFunVo);

		LOG.debug("effect={}", effect);

		BaseRes<ProFunVo> res = new BaseRes<ProFunVo>();
		res.setData(proFunVo);
		res.setServiceCode(ServiceCode.SUCCESS);
		return res;
	}

	@Override
	public BaseRes<ProFunVo> modifyFunction(ProFunVo proFunVo) {

		proFunVo.setUpdateTime(new Date());
		int effect = proFunMapper.updateByPrimaryKeySelective(proFunVo);

		LOG.debug("effect={}", effect);

		BaseRes<ProFunVo> res = new BaseRes<ProFunVo>();
		res.setData(proFunVo);
		res.setServiceCode(ServiceCode.SUCCESS);
		return res;
	}

	@Override
	public BaseRes<String> deleteFunctionByFunId(Long funId) {

		int effect = proFunMapper.deleteByPrimaryKey(funId);

		// 删除此方法的参数引用
		ProFunArgCriteria funArgCriteria = new ProFunArgCriteria();
		funArgCriteria.createCriteria().andFunIdEqualTo(funId);

		proFunArgMapper.deleteByExample(funArgCriteria);

		LOG.debug("effect={}", effect);

		BaseRes<String> res = new BaseRes<String>();
		res.setServiceCode(ServiceCode.SUCCESS);
		return res;
	}

	@Override
	public BaseRes<List<ProPageVo>> fetchAllPageByProjectId(Long projectId) {

		ProPageCriteria mProPageCriteria = new ProPageCriteria();
		mProPageCriteria.createCriteria().andProjectIdEqualTo(projectId);
		List<ProPage> pages = proPageMapper.selectByExample(mProPageCriteria);
		List<ProPageVo> pageVos = BeanUtil.voConvertList(pages, ProPageVo.class);

		BaseRes<List<ProPageVo>> res = new BaseRes<List<ProPageVo>>();
		res.setData(pageVos);
		res.setServiceCode(ServiceCode.SUCCESS);
		return res;
	}

	@Override
	public BaseRes<ProFileVo> fetchFileWithFieldByVoId(Long fileId) {

		BaseRes<ProFileVo> res = new BaseRes<ProFileVo>();
		ProFile proFile = proFileMapper.selectByPrimaryKey(fileId);

		ProFileVo mProFileVo = BeanUtil.voConvert(proFile, ProFileVo.class);

		ProFieldCriteria mProFieldCriteria = new ProFieldCriteria();
		mProFieldCriteria.createCriteria().andFileIdEqualTo(fileId);
		List<ProField> proFields = proFieldMapper.selectByExample(mProFieldCriteria);

		mProFileVo.setFields(proFields);
		if (proFile.getSuperId() != null && proFile.getSuperId() > 0) {

			if (Constants.FileType.VO.equals(proFile.getFileType())) {

				// fetch table
				ProTable proTable = proTableMapper.selectByPrimaryKey(proFile.getSuperId());

				ProTableColumnCriteria mProTableColumnCriteria = new ProTableColumnCriteria();
				mProTableColumnCriteria.createCriteria().andTableIdEqualTo(proTable.getId());
				List<ProTableColumn> columns = proTableColumnMapper.selectByExample(mProTableColumnCriteria);
				ProTableVo table = BeanUtil.voConvert(proTable, ProTableVo.class);
				table.setColumns(columns);
				mProFileVo.setTable(table);
			}
		}

		res.setData(mProFileVo);
		res.setServiceCode(ServiceCode.SUCCESS);
		return res;
	}

	@Override
	public BaseRes<List<ProFunArg>> listFunArg(Long funId) {
		/**
		 * 返回参数为vo时，成员变量包括，已添加和未添加的vo成员变量， 已添加排在前面显示 vo最多显示两级参数
		 */
		List<ProFunArg> args = proFunArgMapper.fetchFunArgsByFunId(funId);

		BaseRes<List<ProFunArg>> res = new BaseRes<List<ProFunArg>>(ServiceCode.SUCCESS);
		// 查出vo成员属性列表
		for (ProFunArg fa : args) {
			if (FunArgType.vo.name().equals(fa.getArgType())) {
				Long argTypeId = fa.getArgTypeId();

				ProFile mProFile = proFileMapper.selectByPrimaryKey(argTypeId);
				Map<String, ProFunArg> voFiledMap = new HashMap<>();

				// vo是否继承了数据库表
				Long superId = mProFile.getSuperId();
				if (superId != null && superId > 0) {
					ProTableColumnCriteria mProTableColumnCriteria = new ProTableColumnCriteria();
					mProTableColumnCriteria.createCriteria().andTableIdEqualTo(superId);
					List<ProTableColumn> columns = proTableColumnMapper.selectByExample(mProTableColumnCriteria);

					columns.stream().forEach(field -> {
						ProFunArg _fa = new ProFunArg("children");
						_fa.setArgType(FunArgType.base.name());
						_fa.setName(field.getName());
						_fa.setFunId(funId);
						_fa.setCollectionType(CollectionType.single.name());
						_fa.setFieldId(field.getId());
						_fa.setPid(fa.getId());
						_fa.setFieldType("table");
						_fa.setComment(field.getComment());
						_fa.setArgTypeName(field.getMysqlType());
						voFiledMap.put(_fa.getName(), _fa);
					});
				}

				ProFieldCriteria mProFieldCriteria = new ProFieldCriteria();
				mProFieldCriteria.createCriteria().andFileIdEqualTo(argTypeId);

				// 获取vo定义成员域
				List<ProField> proFiles = proFieldMapper.selectByExample(mProFieldCriteria);
				proFiles.stream().forEach(field -> {

					if (FieldDataType.base.name().equals(field.getDataType())) {
						ProFunArg _fa = new ProFunArg("children");
						_fa.setArgType(FunArgType.base.name());
						_fa.setName(field.getName());
						_fa.setFunId(funId);
						_fa.setCollectionType(field.getCollectionType());
						_fa.setFieldId(field.getId());
						_fa.setPid(fa.getId());
						_fa.setFieldType("vo");
						_fa.setComment(field.getComment());
						_fa.setArgTypeName(field.getTypePath());
						voFiledMap.put(_fa.getName(), _fa);
					}
				});

				if (voFiledMap.size() > 0) {

					// merge name 一至合并
					if (!CollectionUtils.isEmpty(fa.getChildren())) {
						fa.getChildren().stream().forEach(__fa -> {
							voFiledMap.remove(__fa.getName());
						});
						fa.getChildren().addAll(new ArrayList<ProFunArg>(voFiledMap.values()));
					} else {
						fa.setChildren(new ArrayList<ProFunArg>(voFiledMap.values()));
					}
				}
			}
		}

		res.setData(args);

		return res;
	}

	@Override
	public BaseRes<String> addFunArg(ProFunArgVo proFunArg) {

		/**
		 * fieldId: 2314; funId: 25 类型： vo或table
		 */
		if (null != proFunArg.getFieldId()) {

			if ("vo".equals(proFunArg.getFieldType())) {

				ProField mProField = proFieldMapper.selectByPrimaryKey(proFunArg.getFieldId());
				if (mProField != null) {
					proFunArg.setName(mProField.getName());
					proFunArg.setArgType(mProField.getDataType());
					proFunArg.setArgTypeName(mProField.getTypePath());
					proFunArg.setCollectionType(mProField.getCollectionType());
					proFunArg.setComment(mProField.getComment());

					if (FieldDataType.vo.name().equals(mProField.getDataType())) {
						proFunArg.setArgTypeId(Long.valueOf(mProField.getTypeKey()));
					}
				}
			} else {
				// 添加的有可能是 数据库表的字段
				ProTableColumn mProTableColumn = proTableColumnMapper.selectByPrimaryKey(proFunArg.getFieldId());
				proFunArg.setName(mProTableColumn.getName());

				// DECIMAL 3
				IntrospectedColumn introspectedColumn = ObjectTypeUtil.getIntrospectedColumn(mProTableColumn.getMysqlType());

				JavaTypeResolver javaTypeResolver = new JavaTypeResolverDefaultImpl();
				FullyQualifiedJavaType fullyQualifiedJavaType = javaTypeResolver.calculateJavaType(introspectedColumn);
				proFunArg.setArgTypeName(fullyQualifiedJavaType.getFullyQualifiedName());

				LOG.info("type={}, num={},  ss={}", fullyQualifiedJavaType.getShortName(),
						javaTypeResolver.calculateJdbcTypeName(introspectedColumn));

				proFunArg.setArgType(TypeEnum.FunArgType.base.name());
				proFunArg.setCollectionType(TypeEnum.CollectionType.single.name());
				proFunArg.setComment(mProTableColumn.getComment());
			}
			// name argType argTypeName argTypeId collectionType comment
		}

		proFunArg.setCreateTime(new Date());
		proFunArg.setUpdateTime(new Date());
		proFunArgMapper.insertSelective(proFunArg);
		return new BaseRes<>(ServiceCode.SUCCESS, "ok");
	}

	@Override
	public BaseRes<String> modifyFunArg(ProFunArgVo proFunArg) {
		proFunArg.setUpdateTime(new Date());
		proFunArgMapper.updateByPrimaryKeySelective(proFunArg);
		return new BaseRes<String>(ServiceCode.SUCCESS, "ok");
	}

	@Override
	public BaseRes<String> deleteFunArg(Long argId) {

		proFunArgMapper.deleteByPrimaryKey(argId);

		List<ProFunArg> args = proFunArgMapper.fetchFunArgsChildrenById(argId);
		recursionDeleteArg(args);

		return new BaseRes<String>(ServiceCode.SUCCESS, "ok");
	}

	private void recursionDeleteArg(List<ProFunArg> args) {
		if (args != null) {
			for (ProFunArg a : args) {
				proFunArgMapper.deleteByPrimaryKey(a.getId());
				recursionDeleteArg(a.getChildren());
			}
		}
	}

	@Override
	public BaseRes<List<CommValidate>> validateList(Long proId) {
		CommValidateCriteria mCommValidateCriteria = new CommValidateCriteria();
		mCommValidateCriteria.createCriteria().andProIdEqualTo(proId);

		mCommValidateCriteria.or().andProIdIsNull();
		mCommValidateCriteria.or().andProIdEqualTo(0l);

		List<CommValidate> list = commValidateMapper.selectByExample(mCommValidateCriteria);
		return new BaseRes<List<CommValidate>>(ServiceCode.SUCCESS, list);
	}

	public static class dddd<TT extends SysUser & SysUserService, E, F>extends BaseRes<SysUser> {
	    private TT user;
	    private E e;
	    private F f;
    }

    public class fff extends SysUser implements Serializable {

    }

	private enum aaa {
		aaa, bbb, ccc;

	}
}
