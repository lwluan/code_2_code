package com.cd2cd.util.project;

import com.alibaba.fastjson.JSONArray;
import com.cd2cd.dom.java.FileIdsAndType;
import com.cd2cd.dom.java.TypeEnum;
import com.cd2cd.domain.*;
import com.cd2cd.domain.gen.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SimpleProjectGenerate extends ProjectGenerate {

    private static Logger log = LoggerFactory.getLogger(SimpleProjectGenerate.class);

    @Override
    protected void genProjectBase() throws Exception {
        if (!new File(localPath).exists()) {
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

        String groupIdName = groupId.replaceAll("\\.", "/").replaceAll("-", "_");
        String artifactIdName = artifactId.replaceAll("\\.", "/").replaceAll("-", "_");

        String mainProjectName = artifactIdName + "_main";
        String parentProjectName = artifactIdName + "_parent";

        // 项目模板地址
        String projectPath = localPath + "/" + code_path;
        String targetRootPath = localPath + "/" + artifactIdName;
        reNameFile(projectPath, targetRootPath);
        projectPath = targetRootPath;

        String projectMainPath = projectPath + "/" + code_path + "_main";
        String targetMainPath = projectPath + "/" + mainProjectName;
        reNameFile(projectMainPath, targetMainPath);
        projectMainPath = targetMainPath;

        String projectParentPath = projectPath + "/" + code_path + "_parent";
        String targetParentPath = projectPath + "/" + parentProjectName;
        reNameFile(projectParentPath, targetParentPath);
        projectParentPath = targetParentPath;

        // 重命名项目包名
        String projectMainSrcGroupIdPath = targetMainPath + "/src/main/java/com/cd2cd";
        String targetMainSrcGroupIdPath = targetMainPath + "/src/main/java/" + groupIdName;

        // rename group id
        reNameFile(projectMainSrcGroupIdPath, targetMainSrcGroupIdPath);
        projectMainSrcGroupIdPath = targetMainSrcGroupIdPath;

        // rename artifact id
        String projectMainSrcArtifactIdPath = projectMainSrcGroupIdPath + "/admin";
        String targetMainSrcArtifactIdPaht = projectMainSrcGroupIdPath + "/" + artifactIdName;

        reNameFile(projectMainSrcArtifactIdPath, targetMainSrcArtifactIdPaht);
        projectMainSrcArtifactIdPath = targetMainSrcArtifactIdPaht;

        reNameFolder();
        replacePomXml();
        replaceJavaAndMapperXml();
    }

    private void replaceJavaAndMapperXml() throws Exception {

        String groupIdName = groupId.replaceAll("\\.", "/").replaceAll("-", "_");
        String artifactIdName = artifactId.replaceAll("\\.", "/").replaceAll("-", "_");
        String projectPath = localPath + "/" + artifactIdName;

        List<File> files = new ArrayList<File>();
        listAllFile(projectPath, files);

        String packageSrc = "package com.cd2cd.admin";
        String packageDesc = "package " + groupId + "." + artifactIdName;
        String importSrc = "import com.cd2cd.admin.";
        String importDesc = "import " + groupId + "." + artifactIdName + ".";

//		LOG.info("files-size={}", files.size());
        for (File f : files) {

//			LOG.info("f-name={}", f.getAbsoluteFile());

            if (f.getPath().endsWith(".java")) {
                String content = IOUtils.toString(new FileInputStream(f), "utf-8");
                content = content.replaceAll(packageSrc, packageDesc).replaceAll(importSrc, importDesc);
                IOUtils.write(content, new FileOutputStream(f), "utf-8");
            } else if (f.getPath().endsWith(".properties")) {

                List<String> lines = IOUtils.readLines(new FileInputStream(f), "utf-8");
                StringBuffer content = new StringBuffer();
                for (String line : lines) {

                    if (line.indexOf("mybatis.mapper-locations") > -1) {
                        line = "mybatis.mapper-locations=classpath:" + groupIdName + "/" + artifactIdName + "/**/*Mapper.xml";
                    } else if(line.indexOf("mybatis.type-aliases-package") > -1 ) {
                        line = "mybatis.type-aliases-package=" + groupId + "." + artifactIdName + ".domain";
                    } else if(line.indexOf("server.context-path") > -1 ) {
                        line = "server.context-path=" + contextPath;
                    }
                    content.append(line);
                    content.append(NEW_LINE);
                }
                IOUtils.write(content.toString(), new FileOutputStream(f), "utf-8");
            } else if (f.getPath().endsWith("Mapper.xml")) {
                String content = IOUtils.toString(new FileInputStream(f), "utf-8");
                content = content.replaceAll("com.cd2cd.admin", groupId + "." + artifactIdName);
                IOUtils.write(content, new FileOutputStream(f), "utf-8");
            } else if (f.getPath().endsWith(".html")) {
                String content = IOUtils.toString(new FileInputStream(f), "utf-8");
                content = content.replaceAll("/code_template", contextPath);
                IOUtils.write(content, new FileOutputStream(f), "utf-8");
            }

        }
    }

    private void replacePomXml() throws Exception {

        // - = - = - = - = - 内容替换 - = - = - = - = -
        String artifactIdName = artifactId.replaceAll("\\.", "/").replaceAll("-", "_");

        String mainProjectName = artifactIdName + "_main";
        String parentProjectName = artifactIdName + "_parent";

        // 项目模板地址
        String projectPath = localPath + "/" + artifactIdName;
        String projectMainPath = projectPath + "/" + mainProjectName;
        String projectParentPath = projectPath + "/" + parentProjectName;

        // root pom.xml
        String pomFilePath = projectPath + "/pom.xml";

        // pom.xml
        Document document = getDocumentByFilePath(pomFilePath);
        Element rootElement = document.getDocumentElement();

        getChildsByTagName(rootElement, "groupId").get(0).setTextContent(groupId);
        getChildsByTagName(rootElement, "artifactId").get(0).setTextContent(artifactIdName);
        getChildsByTagName(rootElement, "version").get(0).setTextContent(version);
        getChildsByTagName(rootElement, "name").get(0).setTextContent(artifactIdName);

        Node modulesNode = getChildsByTagName(rootElement, "modules").get(0);
        cleanNodeChilds(modulesNode);

        Node moduleNode = document.createElement("module");
        moduleNode.setTextContent(mainProjectName);
        modulesNode.appendChild(moduleNode);

        moduleNode = document.createElement("module");
        moduleNode.setTextContent(parentProjectName);
        modulesNode.appendChild(moduleNode);

        saveDocument(document, pomFilePath);

        // main pom.xml
        pomFilePath = projectMainPath + "/pom.xml";
        document = getDocumentByFilePath(pomFilePath);
        rootElement = document.getDocumentElement();

        Node parentNode = getChildsByTagName(rootElement, "parent").get(0);
        getChildsByTagName(parentNode, "groupId").get(0).setTextContent(groupId);
        getChildsByTagName(parentNode, "artifactId").get(0).setTextContent(parentProjectName);
        getChildsByTagName(parentNode, "version").get(0).setTextContent(version);
        getChildsByTagName(parentNode, "relativePath").get(0).setTextContent("../" + parentProjectName);

        getChildsByTagName(rootElement, "artifactId").get(0).setTextContent(mainProjectName);
        getChildsByTagName(rootElement, "name").get(0).setTextContent(mainProjectName);
        Node propertiesNode = getChildsByTagName(rootElement, "properties").get(0);
        getChildsByTagName(propertiesNode, "start-class").get(0).setTextContent(
                groupId + "." + artifactIdName + ".AppMainStarter");

        Node buildNode = getChildsByTagName(rootElement, "build").get(0);
        getChildsByTagName(buildNode, "finalName").get(0).setTextContent(mainProjectName);

        saveDocument(document, pomFilePath);

        // parent pom.xml
        pomFilePath = projectParentPath + "/pom.xml";
        document = getDocumentByFilePath(pomFilePath);
        rootElement = document.getDocumentElement();

        getChildsByTagName(rootElement, "groupId").get(0).setTextContent(groupId);
        getChildsByTagName(rootElement, "artifactId").get(0).setTextContent(parentProjectName);
        getChildsByTagName(rootElement, "version").get(0).setTextContent(version);


        saveDocument(document, pomFilePath);
    }

    private void reNameFolder() {

        String groupIdName = groupId.replaceAll("\\.", "/").replaceAll("-", "_");
        String artifactIdName = artifactId.replaceAll("\\.", "/").replaceAll("-", "_");

        String mainProjectName = artifactIdName + "_main";
        String parentProjectName = artifactIdName + "_parent";

        // 项目模板地址
        String projectPath = localPath + "/" + code_path;
        String targetRootPath = localPath + "/" + artifactIdName;
        reNameFile(projectPath, targetRootPath);
        projectPath = targetRootPath;

        String projectMainPath = projectPath + "/" + code_path + "_main";
        String targetMainPath = projectPath + "/" + mainProjectName;
        reNameFile(projectMainPath, targetMainPath);

        String projectParentPath = projectPath + "/" + code_path + "_parent";
        String targetParentPath = projectPath + "/" + parentProjectName;
        reNameFile(projectParentPath, targetParentPath);

        // rename group id
        String projectMainSrcGroupIdPath = targetMainPath + "/src/main/java/com/cd2cd";
        String targetMainSrcGroupIdPath = targetMainPath + "/src/main/java/" + groupIdName;
        reNameFile(projectMainSrcGroupIdPath, targetMainSrcGroupIdPath);
        projectMainSrcGroupIdPath = targetMainSrcGroupIdPath;

        // rename artifact id
        String projectMainSrcArtifactIdPath = projectMainSrcGroupIdPath + "/admin";
        String targetMainSrcArtifactIdPaht = projectMainSrcGroupIdPath + "/" + artifactIdName;
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
                localPath = localPath + artifactId + "/" + artifactId + "_main/";

                initH2Database(tables, database);
                genJavaFromDb(tables, database, localPath, localPath, localPath);
            }
        }
    }

    @Override
    protected void genControllerAndService() throws Exception {
        ProFileCriteria mProFileCriteria = new ProFileCriteria();
        mProFileCriteria.createCriteria()
                .andFileTypeEqualTo(TypeEnum.FileTypeEnum.controller.name())
                .andProjectIdEqualTo(this.project.getId());
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
                    FileIdsAndType argFrt = getArgTypes(args);
                    file.getImportTypes().addAll(argFrt.getTypePaths());

                    voIds.addAll(argFrt.getTypeIds());
                }
                /**
                 * 方法返回值类加入到file的import中
                 */
                if(null != fun.getResVoId()) {
                    LOG.info("returnVoJson={}", fun.getReturnVo());
                    FileIdsAndType frt = getFunReturnType(fun.getReturnVo());
                    file.getImportTypes().addAll(frt.getTypePaths());

                    voIds.addAll(frt.getTypeIds());
                    voIds.add(fun.getResVoId());

                    // 导入import
                    importTypeToFile(voIds, file);
                }
            }

            file.setFuns(funs);
        }
        genController(controllerList);
        genService(controllerList);
    }

    @Override
    protected void getVo() throws Exception {
        /**
         * 生成 Vo验证规则 - 加载不能的验证规则
         */
        ProFileCriteria voCriteria = new ProFileCriteria();
        voCriteria.createCriteria()
                .andFileTypeEqualTo(TypeEnum.FileTypeEnum.vo.name())
                .andProjectIdEqualTo(this.project.getId());
        List<ProFile> voList = proFileMapper.selectFileAndModule(voCriteria);
        for(ProFile file : voList) {
            ProFieldCriteria fieldCriteria = new ProFieldCriteria();
            fieldCriteria.setOrderByClause("id");
            fieldCriteria.createCriteria().andFileIdEqualTo(file.getId());

            List<ProField> fields = proFieldMapper.selectByExample(fieldCriteria);

            file.setFields(fields);

            FileIdsAndType frt = getFieldTypes(fields);
            file.getImportTypes().addAll(frt.getTypePaths());

            // 导入import
            importTypeToFile(frt.getTypeIds(), file);

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
                        .andArgTypeEqualTo(TypeEnum.FunArgType.vo.name())
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
                    .andArgTypeEqualTo(TypeEnum.FunArgType.vo.name());
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
        toDogenVo(voList);
    }
}
