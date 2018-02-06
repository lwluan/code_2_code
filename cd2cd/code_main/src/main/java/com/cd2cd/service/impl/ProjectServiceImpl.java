package com.cd2cd.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cd2cd.comm.ServiceCode;
import com.cd2cd.domain.ProFile;
import com.cd2cd.domain.ProModule;
import com.cd2cd.domain.ProProject;
import com.cd2cd.domain.gen.ProFileCriteria;
import com.cd2cd.domain.gen.ProModuleCriteria;
import com.cd2cd.mapper.ProFileMapper;
import com.cd2cd.mapper.ProModuleMapper;
import com.cd2cd.mapper.ProProjectMapper;
import com.cd2cd.service.ProjectService;
import com.cd2cd.util.FileTypeEnum;
import com.cd2cd.util.PackageTypeEnum;
import com.cd2cd.util.ProjectModuleTypeEnum;
import com.cd2cd.vo.BaseRes;

@Service
public class ProjectServiceImpl implements ProjectService {

	@Autowired
	ProProjectMapper proProjectMapper;

	@Autowired
	ProModuleMapper proModuleMapper;
	
	@Autowired
	ProFileMapper proFileMapper;

	@Override
	public BaseRes<String> fetchProjectFileTree(Long projectId, String packageType, Long moduleId) {

		ProProject mProProject = proProjectMapper.selectByPrimaryKey(projectId);

		BaseRes<String> res = null;
		if (mProProject == null) {
			res = new BaseRes<String>();
			res.setServiceCode(ServiceCode.NOT_EXISTS_PROJECT);
			return res;
		}
		
		List<ProModule> modules = null;
		if (moduleId == null || moduleId < 1 ) {
			// fetch All module
			ProModuleCriteria mProModuleCriteria = new ProModuleCriteria();
			mProModuleCriteria.createCriteria().andProjectIdEqualTo(projectId);
			modules = proModuleMapper.selectByExample(mProModuleCriteria);
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
			mProFileCriteria.createCriteria()
			.andProjectIdEqualTo(projectId)
			.andModuleIdEqualTo(moduleId);
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
				JSONObject moduleNode = newJson(treeId, pId, _module, "package");
				modulePid = moduleNode.getInt("id");
				rootArray.put(moduleNode);
				
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
				
				String type = file.getType();
				String dType = file.getDtype();
				JSONObject temp = null;
				Integer _pId = null;
				
				if( FileTypeEnum.controller.eq(type) ) {
					_pId = controllerNodeId;
				} else if( FileTypeEnum.service.eq(type) ) {
					_pId = serviceNodeId;
				} else if( FileTypeEnum.mapper.eq(type) ) {
					_pId = mapperNodeId;
				} else if( FileTypeEnum.vo.eq(type) ) {
					_pId = voNodeId;
				} else if( FileTypeEnum.domain.eq(type) ) {
					_pId = domainNodeId;
				}
				
				temp = newJson(treeId, _pId, file.getName() + ".java", type);
				temp.put("dType", dType);
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
			mProFileCriteria.createCriteria()
			.andProjectIdEqualTo(projectId)
			.andModuleIdEqualTo(moduleId);
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
				String _basePkgName = basePkgName + "." + module.getName();
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
				
				String type = file.getType();
				String dType = file.getDtype();
				JSONObject temp = null;
				Integer pId = null;
				
				if( FileTypeEnum.controller.eq(type) ) {
					pId = controllerNodeId;
				} else if( FileTypeEnum.service.eq(type) ) {
					pId = serviceNodeId;
				} else if( FileTypeEnum.mapper.eq(type) ) {
					pId = mapperNodeId;
				} else if( FileTypeEnum.vo.eq(type) ) {
					pId = voNodeId;
				} else if( FileTypeEnum.domain.eq(type) ) {
					pId = domainNodeId;
				}
				
				temp = newJson(treeId, pId, file.getName() + ".java", type);
				temp.put("dType", dType);
				rootArray.put(temp);
			}
		}
	}
	
	private JSONObject newJson(TreeId treeId, Integer pId, String name, String fileType) throws JSONException {
		
		treeId.index ++;
		
		JSONObject packageNode = new JSONObject();
		packageNode.put("id", treeId.index);
		packageNode.put("pId", pId);
		packageNode.put("name", name);
		packageNode.put("fileType", fileType);
		return packageNode;
	}
	
	private class TreeId {
		public Integer index = 1;
	}
	

}
