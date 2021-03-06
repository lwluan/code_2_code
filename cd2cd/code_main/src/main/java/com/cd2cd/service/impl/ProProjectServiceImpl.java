package com.cd2cd.service.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.cd2cd.domain.*;
import com.cd2cd.domain.gen.*;
import com.cd2cd.mapper.*;
import com.cd2cd.util.SpringContextUtil;
import com.cd2cd.util.project.MicroProjectGenerate;
import com.cd2cd.util.project.ProjectGenerate;
import com.cd2cd.util.project.SimpleProjectGenerate;
import com.cd2cd.vo.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cd2cd.comm.ServiceCode;
import com.cd2cd.dom.java.FileIdsAndType;
import com.cd2cd.dom.java.TypeEnum.FileTypeEnum;
import com.cd2cd.dom.java.TypeEnum.FunArgType;
import com.cd2cd.domain.gen.ProProjectCriteria.Criteria;
import com.cd2cd.service.ProProjectService;
import com.cd2cd.util.BeanUtil;
import com.cd2cd.util.ProjectGenUtil;

import javax.annotation.Resource;


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
	private ProTableColumnMapper tableColumnMapper;
	
	@Autowired
	private ProFileMapper proFileMapper;
	
	@Autowired
	private ProFieldMapper proFieldMapper;
	
	@Autowired
	private ProFunMapper funMapper;
	
	@Autowired
	private ProFunArgMapper funArgMapper;
	
	@Autowired
	private CommValidateMapper commValidateMapper;

	@Resource
	private ProMicroServiceMapper microServiceMapper;
	
	@Override
	public BaseRes<DataPageWrapper<ProProjectVo>> list(Integer currPage, Integer pageSize, ProProjectVo proProjectVo) {

		BaseRes<DataPageWrapper<ProProjectVo>> res = new BaseRes<>();
		res.setData(new DataPageWrapper<>());
		res.getData().setCurrPage(currPage);
		res.getData().setPageSize(pageSize);

		ProProjectCriteria example = new ProProjectCriteria();
		int mysqlLength = pageSize;
		int mysqlOffset = (currPage - 1) * mysqlLength;

		example.setMysqlLength(mysqlLength);
		example.setMysqlOffset(mysqlOffset);

		Criteria mCriteria = example.createCriteria();
		mCriteria.andDelFlagEqualTo(0);
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
		proProject.setIgnoreTables("");

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
	public BaseRes<String> genProject(ProProjectVo proProjectVo) {


		ProjectGenerate proGen = null;

		if("micro".equalsIgnoreCase(proProjectVo.getProType())) {
			proGen = SpringContextUtil.getBean(MicroProjectGenerate.class);
		} else if("simple".equalsIgnoreCase(proProjectVo.getProType())) {
			proGen = SpringContextUtil.getBean(SimpleProjectGenerate.class);
		}

		if(null != proGen) {
			try {
				return proGen.genProject(proProjectVo);
			} catch (Exception e) {
				e.printStackTrace();
				BaseRes<String> res = new BaseRes<>();
				res.setServiceCode(ServiceCode.FAILED);
				res.setMsg(e.getMessage());
			}
		}

		return new BaseRes<>(ServiceCode.SUCCESS);
//
//		Long id = proProjectVo.getId();
//
//		LOG.info("id={}", id);
//		ProProject proProject = proProjectMapper.selectByPrimaryKey(id);
//
//		LOG.info("proProject={}", JSONObject.toJSONString(proProject));
//
//		// 使用本传入地址生成
//		if(StringUtils.isNotEmpty(proProjectVo.getLocalPath())) {
//			proProject.setLocalPath(proProjectVo.getLocalPath());
//		}
//
//		BaseRes<String> res = new BaseRes<String>();
//
//		try {
//			Map<String, String> commValidMap = getValidAndClassPath(proProject);
//			// 创建util
//			ProjectGenUtil projectGenUtil = new ProjectGenUtil(proProject);
//
//			/**
//			 * 初始被创建项目
//			 */
//			projectGenUtil.genProjectBase();
//			LOG.info("projectGenUtil.genProjectBase() ...");
//
//			/**
//			 * 生成 domain
//			 */
//			genDomain(projectGenUtil, proProject);
//			LOG.info("genDomain(projectGenUtil, proProject) ...");
//
//			/**
//			 * 生成Controller 控制器 和 Service
//			 */
//			genControllerAndService(projectGenUtil, proProject, commValidMap);
//			LOG.info("genControllerAndService(projectGenUtil, proProject, commValidMap) ...");
//
//			/**
//			 * 生成 vo 类
//			 */
//			getVo(projectGenUtil, proProject, commValidMap);
//			LOG.info("getVo(projectGenUtil, proProject, commValidMap) ...");
//
//			res.setServiceCode(ServiceCode.SUCCESS);
//		} catch (Exception e) {
//			e.printStackTrace();
//			res.setServiceCode(ServiceCode.FAILED);
//			res.setMsg(e.getMessage());
//		}
//		return res;
		// replace package name from project file, folder

	}

	private void genDomain(ProjectGenUtil projectGenUtil, ProProject proProject) throws SQLException {
		// db list
		ProProjectDatabaseRelCriteria ppdrc = new ProProjectDatabaseRelCriteria();
		ppdrc.createCriteria().andProjectIdEqualTo(proProject.getId());
		List<ProProjectDatabaseRel> proDbs = proProjectDatabaseRelMapper.selectByExample(ppdrc);

		List<Long> dbIds = new ArrayList<Long>();
		for (ProProjectDatabaseRel pdr : proDbs) {
			dbIds.add(pdr.getDatabaseId());
		}
		if (!CollectionUtils.isEmpty(dbIds)) {
			ProDatabaseCriteria pdc = new ProDatabaseCriteria();
			pdc.createCriteria().andIdIn(dbIds);
			List<ProDatabase> dababases = proDatabaseMapper.selectByExample(pdc);

			if (dababases.size() > 0) {
				ProDatabase database = dababases.get(0);

				List<ProTable> tables = proTableMapper.selectTableAndColumnByDbId(Arrays.asList(database.getId()));

				String ignoreTables = proProject.getIgnoreTables(); // 忽略生成表
				if(StringUtils.isNotEmpty(ignoreTables)) {
					tables = tables.stream().filter(table -> ignoreTables.indexOf("\"" + table.getId() + "\"") < 0).collect(Collectors.toList());
				}
				
				/**
				 * 暂时支持一个数据库 mapper/entity
				 */
				projectGenUtil.initH2Database(tables, database);
				projectGenUtil.genJavaFromDb(tables, database);
			}
		}
	}
	
	private Map<String, String> getValidAndClassPath(ProProject proProject) {
		// 查出所有valid
		CommValidateCriteria mCommValidateCriteria = new CommValidateCriteria();
		mCommValidateCriteria.createCriteria().andProIdIsNull();
		mCommValidateCriteria.or().andProIdEqualTo(proProject.getId());
		List<CommValidate> validObjs = commValidateMapper.selectByExample(mCommValidateCriteria);
		
		Map<String, String> map = new HashMap<>();
		for(CommValidate cv: validObjs){
			map.put(cv.getName(), cv.getClassPath());
		}
		
		return map;
	}
	
	private void genControllerAndService(ProjectGenUtil projectGenUtil, ProProject proProject, Map<String, String> commValidMap) throws Exception {
		ProFileCriteria mProFileCriteria = new ProFileCriteria();
		mProFileCriteria.createCriteria()
		.andFileTypeEqualTo(FileTypeEnum.controller.name())
		.andProjectIdEqualTo(proProject.getId());
		mProFileCriteria.setOrderByClause("id");
		List<ProFile> controllerList = proFileMapper.selectFileAndModule(mProFileCriteria);
		
		// 加载方法列表
		for(ProFile file : controllerList) {
			ProFunCriteria mProFunCriteria = new ProFunCriteria();
			mProFunCriteria.createCriteria().andCidEqualTo(file.getId());
			mProFunCriteria.setOrderByClause("id");
			List<ProFun> funs = funMapper.selectByExample(mProFunCriteria);
			
			/**
			 * 1、加载参数
			 * 2、方法返回值类加入到file的import中
			 * 3、将参数的类类型加入到 file import中
			 */
			for(ProFun fun : funs) {
				List<ProFunArg> args = funArgMapper.fetchFunArgsByFunId(fun.getId());
				fun.setArgs(args);
				
				
				List<Long> voIds = new ArrayList<>();
				
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
		projectGenUtil.genController(proProject, controllerList, commValidMap);
		projectGenUtil.genService(proProject, controllerList);
	}
	
	private void getVo(ProjectGenUtil projectGenUtil, ProProject proProject, Map<String, String> commValidMap) throws FileNotFoundException, IOException {
		
		/**
		 * 生成 Vo验证规则 - 加载不能的验证规则
		 */
		ProFileCriteria voCriteria = new ProFileCriteria();
		voCriteria.createCriteria()
		.andFileTypeEqualTo(FileTypeEnum.vo.name())
		.andProjectIdEqualTo(proProject.getId());
		List<ProFile> voList = proFileMapper.selectFileAndModule(voCriteria);
		for(ProFile file : voList) {
			ProFieldCriteria fieldCriteria = new ProFieldCriteria();
			fieldCriteria.setOrderByClause("id");
			fieldCriteria.createCriteria().andFileIdEqualTo(file.getId());
			
			List<ProField> fields = proFieldMapper.selectByExample(fieldCriteria);
			
			file.setFields(fields);
			
			FileIdsAndType frt = projectGenUtil.getFieldTypes(fields);
			file.getImportTypes().addAll(frt.getTypePaths());
			
			// 导入import
			importTypeToFile(frt.getTypeIds(), file, projectGenUtil);
			
			List<ProField> validateMethods = new ArrayList<>();
			file.setValidateMethods(validateMethods);
			
			// table validate
			if( null != file.getSuperId()) {
				// 添加table 需要验证的 方法 validateMethods
				ProTableColumnCriteria criteria = new ProTableColumnCriteria();
				criteria.createCriteria()
				.andTableIdEqualTo(file.getSuperId());
				List<ProTableColumn> columns = tableColumnMapper.selectByExample(criteria);
				
				Set<String> columnSet = new HashSet<>();
				for(ProTableColumn c: columns) {
					columnSet.add(c.getName());
				}
				
				// 找出当前vo 所有的
				ProFunArgCriteria argCriteria = new ProFunArgCriteria();
				argCriteria.createCriteria()
				.andArgTypeEqualTo(FunArgType.vo.name())
				.andArgTypeIdEqualTo(file.getId());
				List<ProFunArg> args = funArgMapper.selectByExample(argCriteria);
				
				if(CollectionUtils.isEmpty(args)) {
					continue;
				}
				
				List<Long> pids = new ArrayList<>();
				for(ProFunArg arg: args) {
					pids.add(arg.getId());
				}
				
				argCriteria.clear();
				argCriteria.createCriteria()
				.andPidIn(pids);
				args = funArgMapper.selectByExample(argCriteria);
				Map<String, ProFunArg> argMap = new HashMap<String, ProFunArg>();
				for(ProFunArg arg: args) {
					argMap.put(arg.getName(), arg);
				}
			
				// 获取 表格和参数列表的 交集
				columnSet.retainAll(argMap.keySet());
				for(String ss: columnSet) {
					ProFunArg arg = argMap.get(ss);
					ProField mProField = new ProField();
					mProField.setName(arg.getName());
					mProField.setDataType(arg.getArgTypeName());
					validateMethods.add(mProField);
				}
			}
		}
		
		// 字段有哪些验证， 验证有哪些验证组
		for(ProFile file : voList) {
			
			ProFunArgCriteria mProFunArgCriteria = new ProFunArgCriteria();
			mProFunArgCriteria.createCriteria()
			.andArgTypeIdEqualTo(file.getId())
			.andArgTypeEqualTo(FunArgType.vo.name());
			mProFunArgCriteria.setOrderByClause("id");
			List<ProFunArg> voArgs = funArgMapper.selectByExample(mProFunArgCriteria);
			
			// property, valid
			final Map<String, Map<String, Set<String>>> propertyValid = file.getPropertyValid() != null ? file.getPropertyValid() : new HashMap<>();
			file.setPropertyValid(propertyValid);
			
			voArgs.forEach(arg->{
				
				Long funId = arg.getFunId();
				ProFun fun = funMapper.selectByPrimaryKey(funId);
				ProFile controller = proFileMapper.selectByPrimaryKey(fun.getCid());

				System.out.println("controller=" + controller + ", fun=" + fun);

				// controller + fun
				String validateGroupName = controller.getName() +"_" + fun.getFunName();
				
				ProFunArgCriteria funArgCriteria = new ProFunArgCriteria();
				funArgCriteria.createCriteria()
				.andPidEqualTo(arg.getId())
				.andValidIsNotNull();
				List<ProFunArg> childArgs = funArgMapper.selectByExample(funArgCriteria);
				
				childArgs.forEach(carg -> {
					
					String pName = carg.getName();
					
					// valid,group
					Map<String, Set<String>> validGroup = propertyValid.get(pName);
					if(validGroup == null) {
						validGroup = new HashMap<>();
						propertyValid.put(pName, validGroup);
					}
					
					String validStr = carg.getValid();
					if(StringUtils.isNotBlank(validStr)) {
						JSONArray arr = JSONArray.parseArray(validStr);
						
						for(int i=0; i<arr.size(); i++) {
							String key = arr.getString(i);
							
							Set<String> groupSet = validGroup.get(key);
							if(null == groupSet) {
								groupSet = new HashSet<>();
								validGroup.put(key, groupSet);
							}
							groupSet.add(validateGroupName);
						}
					}
				});
			});
		}
		
		projectGenUtil.genVo(proProject, voList, commValidMap);
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

		BaseRes<DataPageWrapper<ProModuleVo>> res = new BaseRes<>();
		res.setData(new DataPageWrapper<ProModuleVo>());
		res.getData().setCurrPage(currPage);
		res.getData().setPageSize(pageSize);

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
		ProProjectCriteria proProjectCriteria = new ProProjectCriteria();
		proProjectCriteria.createCriteria().andDelFlagEqualTo(0);
		List<ProProject> proProjectList = proProjectMapper.selectByExample(proProjectCriteria);
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
		List<ProModule> proProjectList = proModuleMapper.projectModuleList(projectId);
		List<ProModuleVo> rows = BeanUtil.voConvertListIgnore(proProjectList, ProModuleVo.class, "createTime", "updateTime");
		return new BaseRes<>(rows, ServiceCode.SUCCESS);
	}

	/** -----微服务 --*/

	@Override
	public BaseRes<List<ProMicroServiceVo>> projectMicroServiceList(Long projectId) {
		ProMicroServiceCriteria example = new ProMicroServiceCriteria();
		example.createCriteria().andDelFlagEqualTo(0).andProjectIdEqualTo(projectId);
		List<ProMicroService> list = microServiceMapper.selectByExample(example);
		List<ProMicroServiceVo> voList = BeanUtil.voConvertListIgnore(list, ProMicroServiceVo.class);
		return new BaseRes<>(voList, ServiceCode.SUCCESS);
	}

	@Override
	public BaseRes<String> delMicroService(Long id) {
		ProMicroService record = new ProMicroService();
		record.setId(id);
		record.setDelFlag(1);
		record.setUpdateTime(new Date());
		microServiceMapper.updateByPrimaryKeySelective(record);
		return new BaseRes<>(ServiceCode.SUCCESS);
	}

	@Override
	public BaseRes<String> addMicroService(ProMicroServiceVo proMicroServiceVo) {
		Date d = new Date();
		proMicroServiceVo.setUpdateTime(d);
		proMicroServiceVo.setCreateTime(d);
		microServiceMapper.insertSelective(proMicroServiceVo);
		return new BaseRes<>(ServiceCode.SUCCESS);
	}

	@Override
	public BaseRes<String> modifyMicroService(ProMicroServiceVo proMicroServiceVo) {
		proMicroServiceVo.setUpdateTime(new Date());
		microServiceMapper.updateByPrimaryKeySelective(proMicroServiceVo);
		return new BaseRes<>(ServiceCode.SUCCESS);
	}

}
