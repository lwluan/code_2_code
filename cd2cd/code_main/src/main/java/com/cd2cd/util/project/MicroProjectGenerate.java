package com.cd2cd.util.project;

import com.alibaba.fastjson.JSONArray;
import com.cd2cd.dom.java.FileIdsAndType;
import com.cd2cd.dom.java.TypeEnum;
import com.cd2cd.domain.*;
import com.cd2cd.domain.gen.*;
import com.cd2cd.domain.gen.ProFileCriteria;
import com.cd2cd.mapper.ProMicroServiceMapper;
import com.google.common.collect.ImmutableSet;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MicroProjectGenerate extends ProjectGenerate {

    private static Logger log = LoggerFactory.getLogger(MicroProjectGenerate.class);
    protected static String code_path = "code-microservice";

    @Resource
    private ProMicroServiceMapper microServiceMapper;

    @Override
    protected void genProjectBase() throws Exception {
        if ( ! new File(localPath).exists()) {
            new File(localPath).mkdirs();
        }

        String _path = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "../../../../" + code_path;
        _path = _path.replace("file:", "");
        String tpPath = getRealPath(_path);

        log.info("_path={}, tpPath={}", _path, tpPath);

        // 1、项目复制
        File src = new File(tpPath);
        File dest = new File(localPath + "/" + code_path);
        if (!new File(localPath + "/" + artifactIdName).exists()) {
            copyFolder(src, dest);
        }
        // 2、项目替换
        log.info("replaceProject ...");
        replaceProject();
    }

    /**
     * 替换文件内容
     * @throws Exception
     */
    private void replaceProject() throws Exception {

        /**
         * 重命名文件夹
         * code-microservice-cloud
         * code-microservice-common
         * code-microservice-parent
         * code-microservice-repository
         * code-microservice-webui
         * */
        String groupIdName = groupId.replaceAll("\\.", "/").replaceAll("-", "_");
        String artifactIdName = artifactId.replaceAll("\\.", "/").replaceAll("-", "_");

        // project
        String proPath = localPath + "/" + code_path;
        String targetPath = localPath + "/" + artifactId;
        reNameFile(proPath, targetPath);
        proPath = targetPath;

        // module
        String[] modules = new String[]{"-cloud", "-common", "-repository", "-parent"};
        for(String mStr: modules) {
            reNameProject(mStr, proPath, groupIdName);
            replacePomXml(mStr, proPath);
            replaceJavaAndMapperXml(mStr, proPath);
        }

        // root project
        replacePomXml("", proPath);
    }

    private void replacePomXml(String mStr, String proPath) throws Exception {

        String mProName = artifactId;
        String orgProName = code_path;
        String targetMPath = proPath;
        if(StringUtils.isNotBlank(mStr)) {
            mProName += mStr;
            targetMPath += "/" + mProName;
            orgProName += mStr;
        }

        String pomFile = targetMPath + "/pom.xml";

        log.info("pomFile={}, orgProName={}", pomFile, orgProName);

        // root parent module
        String txt = IOUtils.toString(new FileInputStream(new File(pomFile)), "utf-8");
        txt = txt.replaceAll(code_path, artifactId);

        // groupId
        txt = txt.replaceAll("com.boyu", groupId);

        // 替换版本号
        // TODO

        IOUtils.write(txt, new FileOutputStream(pomFile), "utf-8");
    }

    private void replaceJavaAndMapperXml(String mStr, String proPath) throws Exception {

        String orgGroupId = "com.boyu";
        String descGroupId = groupId;

        String orgKey = "code_microservice";
        String descKey = artifactId.replaceAll("-", "_");

        String targetMPath = proPath;
        if(StringUtils.isNotBlank(mStr)) {
            targetMPath += "/" + artifactId + mStr;
        }

        Set<String> exts = ImmutableSet.of("java", "xml");
        List<File> files = new ArrayList<>();
        listAllFile(targetMPath, files, exts);

        for (File f : files) {
            String content = IOUtils.toString(new FileInputStream(f), "utf-8");
            content = content.replaceAll(orgGroupId, descGroupId).replaceAll(orgKey, descKey);
            IOUtils.write(content, new FileOutputStream(f), "utf-8");
        }
    }

    private void reNameProject(String mStr, String proPath, String groupIdName) {

        // module
        String orgProName = code_path + mStr;
        String mProName = artifactId + mStr;
        String orgProNamePkg = orgProName.replaceAll("\\.", "/").replaceAll("-", "_");
        String mProNamePkg = mProName.replaceAll("\\.", "/").replaceAll("-", "_");
        String mMainPath = proPath + "/" + code_path + mStr;
        String targetMPath = proPath + "/" + mProName;
        reNameFile(mMainPath, targetMPath);

        // 重命名项目包名
        String projectMainSrcGroupIdPath = targetMPath + "/src/main/java/com/boyu";
        String targetMainSrcGroupIdPath = targetMPath + "/src/main/java/" + groupIdName;

        // rename group id
        reNameFile(projectMainSrcGroupIdPath, targetMainSrcGroupIdPath);
        projectMainSrcGroupIdPath = targetMainSrcGroupIdPath;

        // rename artifact id *** code_microservice_cloud
        String projectMainSrcArtifactIdPath = projectMainSrcGroupIdPath + "/"+ orgProNamePkg; // 原来 code_microservice_cloud
        String targetMainSrcArtifactIdPaht = projectMainSrcGroupIdPath + "/" + mProNamePkg;
        reNameFile(projectMainSrcArtifactIdPath, targetMainSrcArtifactIdPaht);
    }

    @Override
    protected void genDomain() throws Exception {
        // db list
        ProProjectDatabaseRelCriteria ppdrc = new ProProjectDatabaseRelCriteria();
        ppdrc.createCriteria().andProjectIdEqualTo(this.project.getId());
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

                String ignoreTables = this.project.getIgnoreTables(); // 忽略生成表
                if(StringUtils.isNotEmpty(ignoreTables)) {
                    tables = tables.stream().filter(table -> ignoreTables.indexOf("\"" + table.getId() + "\"") < 0).collect(Collectors.toList());
                }

                /**
                 * 暂时支持一个数据库 mapper/entity
                 */

                String localPath = project.getLocalPath();
                localPath = localPath.endsWith("/") ? localPath : localPath + "/";
                localPath = localPath + artifactId + "/";
                String mapperPath = localPath + artifactId + "-repository/";
                String domainPath = localPath + artifactId + "-common/";

                initH2Database(tables, database);

                String javaModelPath = domainPath;
                String sqlMapPath = mapperPath;

                genJavaFromDb(tables, database, javaModelPath, sqlMapPath, true);

                // 生成数据源 DataSource --- e-commerce-repository/
                /**
                 * 服务绑定数据源；
                 */

            }
        }
    }

    @Override
    protected void genControllerAndService() throws Exception {
        /**
         * 一个项目中有多个；微服务
         */

        ProMicroServiceCriteria mProMicroServiceCriteria = new ProMicroServiceCriteria();
        mProMicroServiceCriteria.createCriteria()
                .andProjectIdEqualTo(this.project.getId())
        .andDelFlagEqualTo(0);
        List<ProMicroService>  micros = microServiceMapper.selectByExample(mProMicroServiceCriteria);

        log.info("micro-size={}", micros.size());

        for(ProMicroService micro :micros){

            log.info("micro-name={}", micro.getArtifactId());
            List<ProFile> controllerList = proFileMapper.selectFileAndModuleByMicro(micro.getId(), TypeEnum.FileTypeEnum.controller.name());

            log.info("micro-name={}, cons-size={}", micro.getArtifactId(), controllerList.size());

            if(controllerList.size() < 1) {
                continue;
            }

            // 加载方法列表
            for (ProFile file : controllerList) {
                ProFunCriteria mProFunCriteria = new ProFunCriteria();
                mProFunCriteria.createCriteria()
                        .andCidEqualTo(file.getId())
                .andDelFalgEqualTo(0);
                mProFunCriteria.setOrderByClause("id");
                List<ProFun> funs = funMapper.selectByExample(mProFunCriteria);

                /**
                 * 1、加载参数
                 * 2、方法返回值类加入到file的import中
                 * 3、将参数的类类型加入到 file import中
                 */
                for (ProFun fun : funs) {
                    List<ProFunArg> args = funArgMapper.fetchFunArgsByFunId(fun.getId());
                    fun.setArgs(args);


                    List<Long> voIds = new ArrayList<>();

                    /**
                     * 将参数的类类型加入到 file的import中
                     */
                    if (!org.springframework.util.CollectionUtils.isEmpty(args)) {
                        FileIdsAndType argFrt = getArgTypes(args);
                        file.getImportTypes().addAll(argFrt.getTypePaths());

                        voIds.addAll(argFrt.getTypeIds());
                    }
                    /**
                     * 方法返回值类加入到file的import中
                     */
                    if (null != fun.getResVoId()) {
                        LOG.info("returnVoJson={}", fun.getReturnVo());
                        FileIdsAndType frt = getFunReturnType(fun.getReturnVo());
                        file.getImportTypes().addAll(frt.getTypePaths());

                        voIds.addAll(frt.getTypeIds());
                        voIds.add(fun.getResVoId());

                        // 导入import
                        importTypeToFile(voIds, file, micro);
                    }
                }
                file.setFuns(funs);
            }
            genController(controllerList, micro);
            genService(controllerList, micro);
        }
    }

    @Override
    protected void getVo() throws Exception {

        /**
         * VO属于某个 服务Controller中； common中
         */

        ProMicroServiceCriteria mProMicroServiceCriteria = new ProMicroServiceCriteria();
        mProMicroServiceCriteria.createCriteria()
                .andProjectIdEqualTo(this.project.getId())
                .andDelFlagEqualTo(0);
        List<ProMicroService>  micros = microServiceMapper.selectByExample(mProMicroServiceCriteria);

        log.info("micro-size={}", micros.size());

        for(ProMicroService micro :micros) {

            // micro + module + vo
            List<ProFile> voList = proFileMapper.selectFileAndModuleByMicro(micro.getId(), TypeEnum.FileTypeEnum.vo.name());
            for (ProFile file : voList) {
                ProFieldCriteria fieldCriteria = new ProFieldCriteria();
                fieldCriteria.setOrderByClause("id");
                fieldCriteria.createCriteria().andFileIdEqualTo(file.getId());

                List<ProField> fields = proFieldMapper.selectByExample(fieldCriteria);

                file.setFields(fields);

                FileIdsAndType frt = getFieldTypes(fields);
                file.getImportTypes().addAll(frt.getTypePaths());

                // 导入import
                importTypeToFile(frt.getTypeIds(), file, micro);

                List<ProField> validateMethods = new ArrayList<>();
                file.setValidateMethods(validateMethods);

                // table validate
                if (null != file.getSuperId()) {
                    // 添加table 需要验证的 方法 validateMethods
                    ProTableColumnCriteria criteria = new ProTableColumnCriteria();
                    criteria.createCriteria()
                            .andTableIdEqualTo(file.getSuperId());
                    List<ProTableColumn> columns = tableColumnMapper.selectByExample(criteria);

                    Set<String> columnSet = new HashSet<>();
                    for (ProTableColumn c : columns) {
                        columnSet.add(c.getName());
                    }

                    // 找出当前vo 所有的
                    ProFunArgCriteria argCriteria = new ProFunArgCriteria();
                    argCriteria.createCriteria()
                            .andArgTypeEqualTo(TypeEnum.FunArgType.vo.name())
                            .andArgTypeIdEqualTo(file.getId());
                    List<ProFunArg> args = funArgMapper.selectByExample(argCriteria);

                    if (CollectionUtils.isEmpty(args)) {
                        continue;
                    }

                    List<Long> pids = new ArrayList<>();
                    for (ProFunArg arg : args) {
                        pids.add(arg.getId());
                    }

                    argCriteria.clear();
                    argCriteria.createCriteria()
                            .andPidIn(pids);
                    args = funArgMapper.selectByExample(argCriteria);
                    Map<String, ProFunArg> argMap = new HashMap<String, ProFunArg>();
                    for (ProFunArg arg : args) {
                        argMap.put(arg.getName(), arg);
                    }

                    // 获取 表格和参数列表的 交集
                    columnSet.retainAll(argMap.keySet());
                    for (String ss : columnSet) {
                        ProFunArg arg = argMap.get(ss);
                        ProField mProField = new ProField();
                        mProField.setName(arg.getName());
                        mProField.setDataType(arg.getArgTypeName());
                        validateMethods.add(mProField);
                    }
                }
            }

            // 字段有哪些验证， 验证有哪些验证组
            for (ProFile file : voList) {

                ProFunArgCriteria mProFunArgCriteria = new ProFunArgCriteria();
                mProFunArgCriteria.createCriteria()
                        .andArgTypeIdEqualTo(file.getId())
                        .andArgTypeEqualTo(TypeEnum.FunArgType.vo.name());
                mProFunArgCriteria.setOrderByClause("id");
                List<ProFunArg> voArgs = funArgMapper.selectByExample(mProFunArgCriteria);

                // property, valid
                final Map<String, Map<String, Set<String>>> propertyValid = file.getPropertyValid() != null ? file.getPropertyValid() : new HashMap<>();
                file.setPropertyValid(propertyValid);

                voArgs.forEach(arg -> {

                    Long funId = arg.getFunId();
                    ProFun fun = funMapper.selectByPrimaryKey(funId);
                    ProFile controller = proFileMapper.selectByPrimaryKey(fun.getCid());

                    System.out.println("controller=" + controller + ", fun=" + fun);

                    // controller + fun
                    String validateGroupName = controller.getName() + "_" + fun.getFunName();

                    ProFunArgCriteria funArgCriteria = new ProFunArgCriteria();
                    funArgCriteria.createCriteria()
                            .andPidEqualTo(arg.getId())
                            .andValidIsNotNull();
                    List<ProFunArg> childArgs = funArgMapper.selectByExample(funArgCriteria);

                    childArgs.forEach(carg -> {

                        String pName = carg.getName();

                        // valid,group
                        Map<String, Set<String>> validGroup = propertyValid.get(pName);
                        if (validGroup == null) {
                            validGroup = new HashMap<>();
                            propertyValid.put(pName, validGroup);
                        }

                        String validStr = carg.getValid();
                        if (StringUtils.isNotBlank(validStr)) {
                            JSONArray arr = JSONArray.parseArray(validStr);

                            for (int i = 0; i < arr.size(); i++) {
                                String key = arr.getString(i);

                                Set<String> groupSet = validGroup.get(key);
                                if (null == groupSet) {
                                    groupSet = new HashSet<>();
                                    validGroup.put(key, groupSet);
                                }
                                groupSet.add(validateGroupName);
                            }
                        }
                    });
                });
            }
            toDogenVo(voList, micro);
        }
    }
}
