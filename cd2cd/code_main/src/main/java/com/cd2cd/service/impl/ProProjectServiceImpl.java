package com.cd2cd.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.cd2cd.comm.ServiceCode;
import com.cd2cd.dom.java.FileIdsAndType;
import com.cd2cd.dom.java.TypeEnum.FileTypeEnum;
import com.cd2cd.domain.ProDatabase;
import com.cd2cd.domain.ProField;
import com.cd2cd.domain.ProFile;
import com.cd2cd.domain.ProFun;
import com.cd2cd.domain.ProFunArg;
import com.cd2cd.domain.ProModule;
import com.cd2cd.domain.ProProject;
import com.cd2cd.domain.ProProjectDatabaseRel;
import com.cd2cd.domain.ProTable;
import com.cd2cd.domain.gen.ProDatabaseCriteria;
import com.cd2cd.domain.gen.ProFieldCriteria;
import com.cd2cd.domain.gen.ProFileCriteria;
import com.cd2cd.domain.gen.ProFunCriteria;
import com.cd2cd.domain.gen.ProModuleCriteria;
import com.cd2cd.domain.gen.ProProjectCriteria;
import com.cd2cd.domain.gen.ProProjectCriteria.Criteria;
import com.cd2cd.domain.gen.ProProjectDatabaseRelCriteria;
import com.cd2cd.mapper.ProDatabaseMapper;
import com.cd2cd.mapper.ProFieldMapper;
import com.cd2cd.mapper.ProFileMapper;
import com.cd2cd.mapper.ProFunArgMapper;
import com.cd2cd.mapper.ProFunMapper;
import com.cd2cd.mapper.ProModuleMapper;
import com.cd2cd.mapper.ProProjectDatabaseRelMapper;
import com.cd2cd.mapper.ProProjectMapper;
import com.cd2cd.mapper.ProTableMapper;
import com.cd2cd.service.ProProjectService;
import com.cd2cd.util.BeanUtil;
import com.cd2cd.util.ProjectGenUtil;
import com.cd2cd.vo.BaseRes;
import com.cd2cd.vo.DataPageWrapper;
import com.cd2cd.vo.ProModuleVo;
import com.cd2cd.vo.ProProjectVo;

@Service
public class ProProjectServiceImpl implements ProProjectService {
	
	private final static Logger LOG = LoggerFactory.getLogger(ProProjectServiceImpl.class);
	
	@Autowired
	private ProProjectMapper proProjectMapper;

	@Autowired
	private ProDatabaseMapper proDatabaseMapper;
	
	@Autowired
	private ProProjectDatabaseRelMapper proProjectDatabaseRelMapper;

	@Autowired
	private ProModuleMapper proModuleMapper;

	@Autowired
	private ProTableMapper proTableMapper;
	
	@Autowired
	private ProFileMapper proFileMapper;
	
	@Autowired
	private ProFieldMapper proFieldMapper;
	
	@Autowired
	private ProFunMapper funMapper;
	
	@Autowired
	private ProFunArgMapper funArgMapper;
	
	@Override
	public BaseRes<DataPageWrapper<ProProjectVo>> list(Integer currPage, Integer pageSize, ProProjectVo proProjectVo) {

		BaseRes<DataPageWrapper<ProProjectVo>> res = BeanUtil.genDataPageRes(currPage, pageSize);

		ProProjectCriteria example = new ProProjectCriteria();
		int mysqlLength = pageSize;
		int mysqlOffset = (currPage - 1) * mysqlLength;

		example.setMysqlLength(mysqlLength);
		example.setMysqlOffset(mysqlOffset);

		Criteria mCriteria = example.createCriteria();
		if (StringUtils.isNotEmpty(proProjectVo.getName())) {
			mCriteria.andNameLike(proProjectVo.getName() + "%");
		}

		long totalCount = proProjectMapper.countByExample(example);
		res.getData().setTotalCount(totalCount);
		if (totalCount > 0) {
			List<ProProject> proProjectList = proProjectMapper.selectByExample(example);
			List<ProProjectVo> rows = BeanUtil.voConvertList(proProjectList, ProProjectVo.class);
			res.getData().setRows(rows);
		}
		return res;
	}

	@Override
	public ProProjectVo detail(Long id) {
		ProProject mProProject = proProjectMapper.selectByPrimaryKey(id);
		if (mProProject != null) {
			ProProjectVo mProProjectVo = BeanUtil.voConvert(mProProject, ProProjectVo.class);

			List<Long> dbIds = new ArrayList<Long>();

			// query project ref db list
			ProProjectDatabaseRelCriteria mProProjectDatabaseRelCriteria = new ProProjectDatabaseRelCriteria();
			mProProjectDatabaseRelCriteria.createCriteria().andProjectIdEqualTo(mProProjectVo.getId());
			List<ProProjectDatabaseRel> list = proProjectDatabaseRelMapper.selectByExample(mProProjectDatabaseRelCriteria);
			for (int i = 0; i < list.size(); i++) {
				dbIds.add(list.get(i).getDatabaseId());
			}

			mProProjectVo.setDbIds(dbIds);
			return mProProjectVo;
		}
		return null;
	}

	@Override
	public boolean del(Long projectId) {
		// delete project to db ref
		ProProjectDatabaseRelCriteria mProProjectDatabaseRelCriteria = new ProProjectDatabaseRelCriteria();
		mProProjectDatabaseRelCriteria.createCriteria().andProjectIdEqualTo(projectId);
		proProjectDatabaseRelMapper.deleteByExample(mProProjectDatabaseRelCriteria);

		// delete module

		// delete project file

		return proProjectMapper.deleteByPrimaryKey(projectId) > 0;
	}

	@Override
	public ServiceCode add(ProProjectVo proProjectVo) {
		ProProject proProject = BeanUtil.voConvert(proProjectVo, ProProject.class);
		proProject.setCreateTime(new Date());
		proProject.setUpdateTime(new Date());

		proProjectMapper.insertSelective(proProject);

		// insert project and database
		if (null != proProjectVo.getDbIds()) {
			updateProjectAndDatabaseRel(proProject.getId(), proProjectVo.getDbIds());
		}

		return ServiceCode.SUCCESS;
	}

	@Override
	public ServiceCode modify(ProProjectVo proProjectVo) {
		ProProject proProject = BeanUtil.voConvert(proProjectVo, ProProject.class);
		proProject.setUpdateTime(new Date());
		proProjectMapper.updateByPrimaryKeySelective(proProject);

		if (null != proProjectVo.getDbIds()) {
			updateProjectAndDatabaseRel(proProject.getId(), proProjectVo.getDbIds());
		}

		return ServiceCode.SUCCESS;
	}

	private void updateProjectAndDatabaseRel(Long projectId, List<Long> dbIds) {

		// remove rel
		ProProjectDatabaseRelCriteria mProProjectDatabaseRelCriteria = new ProProjectDatabaseRelCriteria();
		mProProjectDatabaseRelCriteria.createCriteria().andProjectIdEqualTo(projectId);
		proProjectDatabaseRelMapper.deleteByExample(mProProjectDatabaseRelCriteria);

		// add rel
		for (Long dbId : dbIds) {
			ProProjectDatabaseRel mProProjectDatabaseRel = new ProProjectDatabaseRel();
			mProProjectDatabaseRel.setCreateTime(new Date());
			mProProjectDatabaseRel.setUpdateTime(new Date());
			mProProjectDatabaseRel.setProjectId(projectId);
			mProProjectDatabaseRel.setDatabaseId(dbId);
			proProjectDatabaseRelMapper.insert(mProProjectDatabaseRel);
		}
	}

	@Override
	public BaseRes<String> genProject(Long id) {

		ProProject proProject = proProjectMapper.selectByPrimaryKey(id);

		BaseRes<String> res = new BaseRes<String>();

		try {
			// db list
			ProProjectDatabaseRelCriteria ppdrc = new ProProjectDatabaseRelCriteria();
			ppdrc.createCriteria().andProjectIdEqualTo(id);
			List<ProProjectDatabaseRel> proDbs = proProjectDatabaseRelMapper.selectByExample(ppdrc);
			
			List<Long> dbIds = new ArrayList<Long>();
			for(ProProjectDatabaseRel pdr: proDbs) {
				dbIds.add(pdr.getDatabaseId());
			}
			
			// 创建util
			ProjectGenUtil projectGenUtil = new ProjectGenUtil(proProject);
			
			// 初始被创建项目
			projectGenUtil.genProjectBase();
			
			if( ! CollectionUtils.isEmpty(dbIds)) {
				ProDatabaseCriteria pdc = new ProDatabaseCriteria();
				pdc.createCriteria().andIdIn(dbIds);
				List<ProDatabase> dababases = proDatabaseMapper.selectByExample(pdc);
				
				if( dababases.size() > 0 ) {
					ProDatabase database = dababases.get(0);
					
					List<ProTable> tables = proTableMapper.selectTableAndColumnByDbId(Arrays.asList(database.getId()));
					
					/** 
					 * 暂时支持一个数据库 
					 * mapper/entity
					 */
					projectGenUtil.initH2Database(tables, database);
					projectGenUtil.genJavaFromDb(tables, database);
				}
			}
			
			/**
			 * 生成Controller 控制器 
			 */
			ProFileCriteria mProFileCriteria = new ProFileCriteria();
			mProFileCriteria.createCriteria()
			.andFileTypeEqualTo(FileTypeEnum.controller.name())
			.andProjectIdEqualTo(proProject.getId());
			List<ProFile> controllerList = proFileMapper.selectFileAndModule(mProFileCriteria);
			
			// 加载方法列表
			for(ProFile file : controllerList) {
				ProFunCriteria mProFunCriteria = new ProFunCriteria();
				mProFunCriteria.createCriteria().andCidEqualTo(file.getId());
				List<ProFun> funs = funMapper.selectByExample(mProFunCriteria);
				
				/**
				 * 1、加载参数
				 * 2、方法返回值类加入到file的import中
				 * 3、将参数的类类型加入到 file import中
				 */
				for(ProFun fun : funs) {
					List<ProFunArg> args = funArgMapper.fetchFunArgsByFunId(fun.getId());
					fun.setArgs(args);
					
					
					List<Long> voIds = new ArrayList<Long>();
					
					/**
					 * 将参数的类类型加入到 file的import中
					 */
					if( ! org.springframework.util.CollectionUtils.isEmpty(args)) {
						FileIdsAndType argFrt = projectGenUtil.getArgTypes(args);
						file.getImportTypes().addAll(argFrt.getTypePaths());
						
						voIds.addAll(argFrt.getTypeIds());
					}
					/**
					 * 方法返回值类加入到file的import中
					 */
					if(null != fun.getResVoId()) {
						LOG.info("returnVoJson={}", fun.getReturnVo());
						FileIdsAndType frt = projectGenUtil.getFunReturnType(fun.getReturnVo());
						file.getImportTypes().addAll(frt.getTypePaths());
						
						voIds.addAll(frt.getTypeIds());
						voIds.add(fun.getResVoId());
						
						// 导入import
						importTypeToFile(voIds, file, projectGenUtil);
					}
				}
				
				file.setFuns(funs);
			}
			
			projectGenUtil.genController(proProject, controllerList);
			
			/**
			 * 生成 Vo验证规则 - 加载不能的验证规则
			 */
			ProFileCriteria voCriteria = new ProFileCriteria();
			voCriteria.createCriteria()
			.andFileTypeEqualTo(FileTypeEnum.vo.name())
			.andProjectIdEqualTo(proProject.getId());
			List<ProFile> voList = proFileMapper.selectFileAndModule(voCriteria);
			for(ProFile file : voList) {
				// TODO field list
				ProFieldCriteria fieldCriteria = new ProFieldCriteria();
				fieldCriteria.createCriteria().andFileIdEqualTo(file.getId());
				
				List<ProField> fields = proFieldMapper.selectByExample(fieldCriteria);
				file.setFields(fields);
				
				FileIdsAndType frt = projectGenUtil.getFieldTypes(fields);
				file.getImportTypes().addAll(frt.getTypePaths());
				
				// 导入import
				importTypeToFile(frt.getTypeIds(), file, projectGenUtil);
				
			}
			
			projectGenUtil.genVo(proProject, voList);
			
			res.setServiceCode(ServiceCode.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			res.setServiceCode(ServiceCode.FAILED);
			res.setMsg(e.getMessage());
		}
		return res;

		// replace package name from project file, folder

	}

	private void importTypeToFile(List<Long> voIds, ProFile file, ProjectGenUtil projectGenUtil) {
		// 提取
		if( ! org.springframework.util.CollectionUtils.isEmpty(voIds)) {
			ProFileCriteria fileCriteria = new ProFileCriteria();
			fileCriteria.createCriteria().andIdIn(voIds);
			List<ProFile> fileList = proFileMapper.selectFileAndModule(fileCriteria);
			
			LOG.info("fileList={}", fileList.size());
			
			Set<String> fileTypes = projectGenUtil.getFileTypes(fileList);
			file.getImportTypes().addAll(fileTypes);
		}
	}
	
	@Override
	public BaseRes<DataPageWrapper<ProModuleVo>> moduleList(Integer currPage, Integer pageSize, ProModuleVo proModuleVo) {

		BaseRes<DataPageWrapper<ProModuleVo>> res = BeanUtil.genDataPageRes(currPage, pageSize);

		ProModuleCriteria example = new ProModuleCriteria();
		int mysqlLength = pageSize;
		int mysqlOffset = (currPage - 1) * mysqlLength;

		example.setMysqlLength(mysqlLength);
		example.setMysqlOffset(mysqlOffset);

		ProModuleCriteria.Criteria mCriteria = example.createCriteria();
		if (StringUtils.isNotEmpty(proModuleVo.getName())) {
			mCriteria.andNameLike(proModuleVo.getName() + "%");
		}

		long totalCount = proModuleMapper.countByExample(example);
		res.getData().setTotalCount(totalCount);
		if (totalCount > 0) {
			List<ProModule> proModuleList = proModuleMapper.selectByExample(example);
			List<ProModuleVo> rows = BeanUtil.voConvertList(proModuleList, ProModuleVo.class);
			res.getData().setRows(rows);
		}
		return res;
	}

	@Override
	public ProModuleVo moduleDetail(Long id) {
		ProModule mProModule = proModuleMapper.selectByPrimaryKey(id);
		if (mProModule != null) {
			ProModuleVo mProModuleVo = BeanUtil.voConvert(mProModule, ProModuleVo.class);
			return mProModuleVo;
		}
		return null;
	}

	@Override
	public boolean delModule(Long id) {

		// remove file or module
		
		return 0 < proModuleMapper.deleteByPrimaryKey(id);
	}

	@Override
	public ServiceCode addModule(ProModuleVo proModuleVo) {

		proModuleVo.setCreateTime(new Date());
		proModuleVo.setUpdateTime(new Date());

		return 0 < proModuleMapper.insertSelective(proModuleVo) ? ServiceCode.SUCCESS : ServiceCode.FAILED;
	}

	@Override
	public ServiceCode modifyModule(ProModuleVo proModuleVo) {

		proModuleVo.setUpdateTime(new Date());
		return 0 < proModuleMapper.updateByPrimaryKeySelective(proModuleVo) ? ServiceCode.SUCCESS : ServiceCode.FAILED;
	}

	@Override
	public BaseRes<List<ProProjectVo>> projectList() {
		
		BaseRes<List<ProProjectVo>> res = new BaseRes<List<ProProjectVo>>();
		List<ProProject> proProjectList = proProjectMapper.selectByExample(null);
		List<ProProjectVo> rows = new ArrayList<ProProjectVo>();
		for(ProProject project: proProjectList) {
			ProProjectVo mProProjectVo = new ProProjectVo();
			mProProjectVo.setId(project.getId());
			mProProjectVo.setName(project.getName());
			rows.add(mProProjectVo);
		}
		res.setServiceCode(ServiceCode.SUCCESS);
		res.setData(rows);
		return res;
		
	}

	@Override
	public BaseRes<List<ProModuleVo>> projectModuleList(Long projectId) {
		BaseRes<List<ProModuleVo>> res = new BaseRes<List<ProModuleVo>>();
		ProModuleCriteria mProModuleCriteria = new ProModuleCriteria();
		mProModuleCriteria.createCriteria().andProjectIdEqualTo(projectId);
		List<ProModule> proProjectList = proModuleMapper.selectByExample(mProModuleCriteria);
		List<ProModuleVo> rows = new ArrayList<ProModuleVo>();
		for(ProModule module: proProjectList) {
			ProModuleVo mProModuleVo = new ProModuleVo();
			mProModuleVo.setId(module.getId());
			mProModuleVo.setName(module.getName());
			mProModuleVo.setShowName(module.getShowName());
			mProModuleVo.setDescription(module.getDescription());
			rows.add(mProModuleVo);
		}
		res.setServiceCode(ServiceCode.SUCCESS);
		res.setData(rows);
		return res;
	}

}
