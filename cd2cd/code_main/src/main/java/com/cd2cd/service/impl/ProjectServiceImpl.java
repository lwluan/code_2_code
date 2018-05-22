package com.cd2cd.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cd2cd.comm.ServiceCode;
import com.cd2cd.domain.ProField;
import com.cd2cd.domain.ProFile;
import com.cd2cd.domain.ProModule;
import com.cd2cd.domain.ProProject;
import com.cd2cd.domain.ProProjectDatabaseRel;
import com.cd2cd.domain.ProTable;
import com.cd2cd.domain.ProTableColumn;
import com.cd2cd.domain.gen.ProFieldCriteria;
import com.cd2cd.domain.gen.ProFileCriteria;
import com.cd2cd.domain.gen.ProModuleCriteria;
import com.cd2cd.domain.gen.ProProjectDatabaseRelCriteria;
import com.cd2cd.domain.gen.ProTableColumnCriteria;
import com.cd2cd.domain.gen.ProTableCriteria;
import com.cd2cd.mapper.ProFieldMapper;
import com.cd2cd.mapper.ProFileMapper;
import com.cd2cd.mapper.ProModuleMapper;
import com.cd2cd.mapper.ProProjectDatabaseRelMapper;
import com.cd2cd.mapper.ProProjectMapper;
import com.cd2cd.mapper.ProTableColumnMapper;
import com.cd2cd.mapper.ProTableMapper;
import com.cd2cd.service.ProjectService;
import com.cd2cd.util.BeanUtil;
import com.cd2cd.util.FileTypeEnum;
import com.cd2cd.util.PackageTypeEnum;
import com.cd2cd.util.ProjectModuleTypeEnum;
import com.cd2cd.util.mbg.Constants;
import com.cd2cd.vo.BaseRes;
import com.cd2cd.vo.ProFieldVo;
import com.cd2cd.vo.ProFileVo;
import com.cd2cd.vo.ProTableColumnVo;
import com.cd2cd.vo.ProTableVo;

@Service
public class ProjectServiceImpl implements ProjectService {
	private static final Logger LOG = LoggerFactory.getLogger(ProjectServiceImpl.class);
	@Autowired ProProjectMapper proProjectMapper;
	@Autowired ProModuleMapper proModuleMapper;
	@Autowired ProFileMapper proFileMapper;
	@Autowired ProFieldMapper proFieldMapper;
	@Autowired ProTableColumnMapper proTableColumnMapper; 
	@Autowired ProProjectDatabaseRelMapper	proProjectDatabaseRelMapper;
	@Autowired ProTableMapper proTableMapper;
	
	@Override
	public BaseRes<String> fetchProjectFileTree(Long projectId, String packageType, Long moduleId) {

		ProProject mProProject = proProjectMapper.selectByPrimaryKey(projectId);

		BaseRes<String> res = null;
		if (mProProject == null) {
			res = new BaseRes<String>();
			res.setServiceCode(ServiceCode.NOT_EXISTS_PROJECT);
			return res;
		}
		
		ProModule commProModule = new ProModule();
		
		/**
		 * moduleId: -1:为公共模块, 0:他部
		 * example: com.test.controller
		 */
		List<ProModule> modules = new ArrayList<ProModule>();
		if (moduleId == null || moduleId == 0 ) {
			// fetch All module
			ProModuleCriteria mProModuleCriteria = new ProModuleCriteria();
			mProModuleCriteria.createCriteria().andProjectIdEqualTo(projectId);
			modules = proModuleMapper.selectByExample(mProModuleCriteria);
			modules.add(commProModule);
			
		} else if(moduleId == -1) { 
			
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
			
			JSONObject src = newJson(treeId, null, "src", "folder");
			src.put("open", true);
			
			rootArray.put(src);
			
			if( PackageTypeEnum.Flat.name().equals(packageType) ) {
				processProjectByFlat(rootArray, treeId, src, mProProject, modules);
			} else {
				processProjectByHierarchical(rootArray, treeId, src, mProProject, modules);
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		res = new BaseRes<String>();
		res.setData(rootArray.toString());
		res.setServiceCode(ServiceCode.SUCCESS);
		return res;
	}
	
	private void processProjectByHierarchical(JSONArray rootArray, TreeId treeId, JSONObject src, ProProject proProject, List<ProModule> modules) throws JSONException {
		
		// root module-_package
		Long projectId = proProject.getId();
		String groupId = proProject.getGroupId();
		String artifctId = proProject.getArtifactId();
		
		String basePkgName = groupId + "." + artifctId;
		
		int pId = src.getInt("id");
		
		JSONObject artifctIdNode = newJson(treeId, pId, basePkgName, "package");
		artifctIdNode.put("open", true);
		pId = artifctIdNode.getInt("id");
		rootArray.put(artifctIdNode);
		
		JSONObject controllerNode = null;
		JSONObject serviceNode = null;
		JSONObject mapperNode = null;
		JSONObject voNode = null;
		JSONObject domainNode = null;
		
		for(int i=0; i<modules.size(); i++) {
			
			ProModule module = modules.get(i);
			Long moduleId = module.getId();
			
			// query file list, the same controller\service\vo\dmain\mapper\page
			ProFileCriteria mProFileCriteria = new ProFileCriteria();
			ProFileCriteria.Criteria mCriteria = mProFileCriteria.createCriteria();
			mCriteria.andProjectIdEqualTo(projectId);
			if( moduleId != null && moduleId > 0 ) { 
				mCriteria.andModuleIdEqualTo(moduleId);
			} else {
				mCriteria.andModuleIdIsNull();
			}
			List<ProFile> proFiles = proFileMapper.selectByExample(mProFileCriteria);
			
			Integer modulePid = pId;
			
			// package -_ file
			if(ProjectModuleTypeEnum.standard.name().equals(proProject.getPackageType()) ) {
				if( controllerNode == null ) {
					controllerNode = newJson(treeId, modulePid, "controller", "package");
					serviceNode = newJson(treeId, modulePid, "service", "package");
					mapperNode = newJson(treeId, modulePid, "mapper", "package");
					voNode = newJson(treeId, modulePid, "vo", "package");
					domainNode = newJson(treeId, modulePid, "domain", "package");
					
					rootArray.put(controllerNode);
					rootArray.put(serviceNode);
					rootArray.put(mapperNode);
					rootArray.put(voNode);
					rootArray.put(domainNode);
				}
			} else {
				String _module = module.getName();
				if( StringUtils.isNotEmpty(_module) ) {
					JSONObject moduleNode = newJson(treeId, pId, _module, "package");
					modulePid = moduleNode.getInt("id");
					rootArray.put(moduleNode);
				}
				
				controllerNode = newJson(treeId, modulePid, "controller", "package");
				serviceNode = newJson(treeId, modulePid, "service", "package");
				mapperNode = newJson(treeId, modulePid, "mapper", "package");
				voNode = newJson(treeId, modulePid, "vo", "package");
				domainNode = newJson(treeId, modulePid, "domain", "package");
				
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
			
			for( ProFile file: proFiles ) {
				
				// module name within in package name
				
				String fileType = file.getFileType();
				String classType = file.getClassType();
				JSONObject temp = null;
				Integer _pId = null;
				
				if( FileTypeEnum.controller.eq(fileType) ) {
					_pId = controllerNodeId;
				} else if( FileTypeEnum.service.eq(fileType) ) {
					_pId = serviceNodeId;
				} else if( FileTypeEnum.mapper.eq(fileType) ) {
					_pId = mapperNodeId;
				} else if( FileTypeEnum.vo.eq(fileType) ) {
					_pId = voNodeId;
				} else if( FileTypeEnum.domain.eq(fileType) ) {
					_pId = domainNodeId;
				}
				
				temp = newJson(treeId, _pId, file.getName() + ".java", fileType, file.getId());
				temp.put("classType", classType);
				rootArray.put(temp);
			}
		}
		
	}

	private void processProjectByFlat(JSONArray rootArray, TreeId treeId, JSONObject src, ProProject proProject, List<ProModule> modules) throws JSONException {
		
		Long projectId = proProject.getId();
		String groupId = proProject.getGroupId();
		String artifctId = proProject.getArtifactId();
		
		String basePkgName = groupId + "." + artifctId;
		
		
		int srcId = src.getInt("id");
		
		JSONObject controllerNode = null;
		JSONObject serviceNode = null;
		JSONObject mapperNode = null;
		JSONObject voNode = null;
		JSONObject domainNode = null;
		
		for( ProModule module : modules ) {
			
			Long moduleId = module.getId();
			
			// query file list, the same controller\service\vo\dmain\mapper\page
			ProFileCriteria mProFileCriteria = new ProFileCriteria();
			ProFileCriteria.Criteria mCriteria = mProFileCriteria.createCriteria();
			mCriteria.andProjectIdEqualTo(projectId);
			if( moduleId != null && moduleId > 0 ) { 
				mCriteria.andModuleIdEqualTo(moduleId);
			} else {
				mCriteria.andModuleIdIsNull();
			}
			
			List<ProFile> proFiles = proFileMapper.selectByExample(mProFileCriteria);
			
			// package -_ file
			
			if(ProjectModuleTypeEnum.standard.name().equals(proProject.getPackageType()) ) {
				if( controllerNode == null ) {
					controllerNode = newJson(treeId, srcId, basePkgName + ".controller", "package");
					serviceNode = newJson(treeId, srcId, basePkgName + ".service", "package");
					mapperNode = newJson(treeId, srcId, basePkgName + ".mapper", "package");
					voNode = newJson(treeId, srcId, basePkgName + ".vo", "package");
					domainNode = newJson(treeId, srcId, basePkgName + ".domain", "package");
					
					rootArray.put(controllerNode);
					rootArray.put(serviceNode);
					rootArray.put(mapperNode);
					rootArray.put(voNode);
					rootArray.put(domainNode);
				}
			} else {
				String _basePkgName = basePkgName;
				if( StringUtils.isNotEmpty(module.getName()) ) {
					_basePkgName = _basePkgName + "." + module.getName();
				}
				
				controllerNode = newJson(treeId, srcId, _basePkgName + ".controller", "package");
				serviceNode = newJson(treeId, srcId, _basePkgName + ".service", "package");
				mapperNode = newJson(treeId, srcId, _basePkgName + ".mapper", "package");
				voNode = newJson(treeId, srcId, _basePkgName + ".vo", "package");
				domainNode = newJson(treeId, srcId, _basePkgName + ".domain", "package");
				
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
			
			for( ProFile file: proFiles ) {
				
				// module name within in package name
				
				String fileType = file.getFileType();
				String classType = file.getClassType();
				JSONObject temp = null;
				Integer pId = null;
				
				if( FileTypeEnum.controller.eq(fileType) ) {
					pId = controllerNodeId;
				} else if( FileTypeEnum.service.eq(fileType) ) {
					pId = serviceNodeId;
				} else if( FileTypeEnum.mapper.eq(fileType) ) {
					pId = mapperNodeId;
				} else if( FileTypeEnum.vo.eq(fileType) ) {
					pId = voNodeId;
				} else if( FileTypeEnum.domain.eq(fileType) ) {
					pId = domainNodeId;
				}
				
				temp = newJson(treeId, pId, file.getName() + ".java", fileType, file.getId());
				temp.put("classType", classType);
				rootArray.put(temp);
			}
		}
	}
	
	private JSONObject newJson(TreeId treeId, Integer pId, String name, String fileType) throws JSONException {
		return this.newJson(treeId, pId, name, fileType, 0l);
	}
	
	private JSONObject newJson(TreeId treeId, Integer pId, String name, String fileType, Long fileId) throws JSONException {
		
		treeId.index ++;
		
		JSONObject packageNode = new JSONObject();
		packageNode.put("id", treeId.index);
		packageNode.put("pId", pId);
		packageNode.put("fileId", fileId);
		packageNode.put("name", name);
		packageNode.put("fileType", fileType);
		return packageNode;
	}
	
	private class TreeId {
		public Integer index = 1;
	}

	@Override
	public BaseRes<List<ProTableVo>> fetchTableListByProjectHasDb(Long projectId) {
		
		BaseRes<List<ProTableVo>> res = new BaseRes<List<ProTableVo>>();
		// 1、获取所关联数据库
		ProProjectDatabaseRelCriteria mProProjectDatabaseRelCriteria = new ProProjectDatabaseRelCriteria();
		mProProjectDatabaseRelCriteria.createCriteria().andProjectIdEqualTo(projectId);
		List<ProProjectDatabaseRel> dbs = proProjectDatabaseRelMapper.selectByExample(mProProjectDatabaseRelCriteria);
		
		// 2、获取数据库关联表
		if( dbs.size() > 0 ) {
			List<Long> ids = new ArrayList<Long>();
			for( ProProjectDatabaseRel db : dbs ) {
				ids.add(db.getDatabaseId());
			}
		
			ProTableCriteria mProTableCriteria = new ProTableCriteria();
			mProTableCriteria.createCriteria().andDatabaseIdIn(ids);
			List<ProTable> tables = proTableMapper.selectByExample(mProTableCriteria);
			
			if( tables.size() > 0 ) {
				List<ProTableVo> tabVos = BeanUtil.voConvertList(tables, ProTableVo.class);
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
		if( proFileVo.getModuleId() == null || proFileVo.getModuleId() == 0 ) {
			proFileVo.setModuleId(null);
		}
		
		BaseRes<String> res = new BaseRes<String>();
		
		int effect = proFileMapper.insert(proFileVo);
		if( effect > 0 ) {
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
		
		List<ProFieldVo> proFieldVos = BeanUtil.voConvertList(proFields, ProFieldVo.class);
		mProFileVo.setFields(proFieldVos);
		if( proFile.getSuperId() != null && proFile.getSuperId() > 0 ) {
			
			if( Constants.FileType.VO.equals(proFile.getFileType()) ) {
				
				// fetch table 
				ProTable proTable = proTableMapper.selectByPrimaryKey(proFile.getSuperId());
				
				ProTableColumnCriteria mProTableColumnCriteria = new ProTableColumnCriteria();
				mProTableColumnCriteria.createCriteria().andTableIdEqualTo(proTable.getId());
				List<ProTableColumn> columns = proTableColumnMapper.selectByExample(mProTableColumnCriteria);
				
				List<ProTableColumnVo> columnVos = BeanUtil.voConvertList(columns, ProTableColumnVo.class);
			
				ProTableVo table = BeanUtil.voConvert(proTable, ProTableVo.class);
				table.setColumns(columnVos);
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
		if( fileVo.getId() != null ) {
			
			effect = proFieldMapper.updateByPrimaryKey(fileVo);
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
	

}
