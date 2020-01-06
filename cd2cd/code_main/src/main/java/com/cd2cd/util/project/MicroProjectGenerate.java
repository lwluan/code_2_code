package com.cd2cd.util.project;

import com.alibaba.fastjson.JSONArray;
import com.cd2cd.dom.java.CodeUtils;
import com.cd2cd.dom.java.FileIdsAndType;
import com.cd2cd.dom.java.TypeEnum;
import com.cd2cd.domain.*;
import com.cd2cd.domain.gen.*;
import com.cd2cd.domain.gen.ProFileCriteria;
import com.cd2cd.mapper.ProMicroServiceMapper;
import com.cd2cd.util.GenFileByFtl;
import com.cd2cd.util.StringUtil;
import com.google.common.collect.ImmutableSet;
import freemarker.template.TemplateException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.mybatis.generator.api.dom.java.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.annotation.Resource;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MicroProjectGenerate extends ProjectGenerate {

    private static Logger log = LoggerFactory.getLogger(MicroProjectGenerate.class);
    protected static String code_path = "code-microservice";
    String feignClientPrev = "feign-qualifier";
    String feignSStr = feignClientPrev+":";
    String feignEStr = "#feign-client-qualifier-end";
    String zuulSStr = "#zuul.routes-s";
    String zuulEStr = "#zuul.routes-e";

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
        String proPath = localPath + "/" + artifactId;
        if ( ! new File(proPath).exists()) {
            copyFolder(src, dest);
        }
        // 2、项目替换
        log.info("replaceProject ...");
        replaceProject();

        // 生成-服务项目
        genMicoServiceProject();
    }

    private void genMicoServiceProject() throws Exception {
        ProMicroServiceCriteria mProMicroServiceCriteria = new ProMicroServiceCriteria();
        mProMicroServiceCriteria.setOrderByClause(" id ");
        mProMicroServiceCriteria.createCriteria()
                .andProjectIdEqualTo(this.project.getId())
                .andDelFlagEqualTo(0);
        List<ProMicroService>  micros = microServiceMapper.selectByExample(mProMicroServiceCriteria);


        String targetPath = localPath + "/" + artifactId;

        log.info("micro-size={}", micros.size());


        for(int i=0; i<micros.size(); i++){
            ProMicroService micro = micros.get(i);

            String microArtifactId = micro.getArtifactId();

            /**
             * 1、服务文件夹
             * 2、pom.xml 文件
             * 3、StartApp.java: 注解文件
             * 4、root pom.xml添加 module + api-pro
             * 5、resources/application.yml: port
             * 6、cloud模块添加各服务
             * 7、
             */

            String microArtifactIdName = microArtifactId.replaceAll("-", "_");
            String microProPath = targetPath + "/" + micro.getArtifactId();
            new File(microProPath).mkdirs();

            // 2、micro-pom.xml
            createMicroPomFile(microProPath, microArtifactId);

            // 3、e_commerce_order ECommerceOrderApplication.java
            genStarterAppClass(microProPath, microArtifactId, microArtifactIdName, micro);

            // 4、module of micro add to root pom.xml
            addModuleOfMicroToRootPom(targetPath, microArtifactId);

            // 5、add resources/application.yml or micro
            addApplicationYmlFileToMicroRes(targetPath, microArtifactId, i+1);

            // 6、micro for depend to pom of cloud module
            addMicroToCloudForDepend(targetPath, micro);

            // 7、gen micro client
            genMicroClientConfig(targetPath, microArtifactId, micros);

            // api project update
            if(micro.getApiProject() == 1) {
                updateMicroZuulConfig(targetPath, microArtifactId, micros);
            }

        }

        // micro-cloud
        String microArtifactId = this.artifactId+"-cloud";
        genMicroClientConfig(targetPath, microArtifactId, micros);

    }

    private void updateMicroZuulConfig(String targetPath, String microArtifactId, List<ProMicroService> micros) throws Exception {
        StringBuilder zuul = new StringBuilder();
        zuul.append(zuulSStr).append("\n");
        for(ProMicroService micro: micros) {
            if(micro.getApiProject() == 0) {
                zuul.append("    "+micro.getArtifactId()+":").append("\n");;
                zuul.append("      path: /"+micro.getArtifactId()+"/**").append("\n");;
                zuul.append("      serviceId: micro-test-cloud").append("\n");;
            }
        }
        zuul.append(zuulEStr);
        String resourcePath = targetPath + "/" + microArtifactId + "/src/main/resources/";
        File appYmlFileDev = new File(resourcePath+"/application-dev.yml");

        updateMicroConfig(zuulSStr, zuulEStr, appYmlFileDev, zuul.toString());
    }

    private void genMicroClientConfig(String targetPath, String microArtifactId, List<ProMicroService> micros) throws Exception {

        // 生成 controller - client Feign-client
        // FeignClient name(microName+moduleName+ControllerName) qualifier
        StringBuilder dev = new StringBuilder();
        StringBuilder prod = new StringBuilder();

        dev.append(feignSStr).append("\n");
        prod.append(feignSStr).append("\n");

        // controller list

        for(ProMicroService micro: micros) {

            // all controller

            List<ProFile> controllerList = proFileMapper.selectFileAndModuleByMicro(micro.getId(), TypeEnum.FileTypeEnum.controller.name());
            log.info("micro-name={}, microId={}, cons-size={}", micro.getArtifactId(), micro.getId(), controllerList.size());
            if(controllerList.size() < 1) {
                continue;
            }

            // 加载方法列表
            for (ProFile file : controllerList) {
                String moduleName = "";
                if(file.getModule() != null) {
                    moduleName = "." + file.getModule().getName();
                }

                String qualifier = getQualifier(file.getName(), moduleName, file.getMicroService().getArtifactId());
                dev.append("  ").append(qualifier).append(": ").append(this.artifactId).append("-cloud\n");
                prod.append("  ").append(qualifier).append(": ").append(micro.getArtifactId()).append("\n");
            }
//            e-commerce-order: e-commerce-cloud
        }

        dev.append(feignEStr);
        prod.append(feignEStr);

        String resourcePath = targetPath + "/" + microArtifactId + "/src/main/resources/";

        File appYmlFileDev = new File(resourcePath+"/application-dev.yml");
        File appYmlFileProd = new File(resourcePath+"/application-prod.yml");

        updateMicroConfig(feignSStr, feignEStr, appYmlFileDev, dev.toString());
        updateMicroConfig(feignSStr, feignEStr, appYmlFileProd, prod.toString());

    }

    private void updateMicroConfig(String sStr, String eStr, File f, String str) throws Exception {
        String txt = IOUtils.toString(new FileInputStream(f), "utf-8");
        String orgStr = txt.substring(txt.indexOf(sStr), txt.indexOf(eStr)+eStr.length());
        orgStr = orgStr.replaceAll("\\*", "\\\\*");
        txt = txt.replaceAll(orgStr, str);
        writeFile(txt, f);
    }

    private void addMicroToCloudForDepend(String targetPath, ProMicroService micro) throws Exception {
        String cloudProPath = targetPath + "/" + this.artifactId+"-cloud";
        String pomFile = cloudProPath + "/pom.xml";

        Document document = getDocumentByFilePath(pomFile);
        Element rootElement = document.getDocumentElement();

        Node dependenciesNode = rootElement.getElementsByTagName("dependencies").item(0);
        NodeList childNodes = dependenciesNode.getChildNodes();

        Node nowDependency = null;

        boolean toRunFor = true;
        BREAK_LABEL:
        {
            if (toRunFor) {
                for (int i = 0; i < childNodes.getLength(); i++) {
                    Node dependency = childNodes.item(i);
                    NodeList deps = dependency.getChildNodes();
                    for (int j = 0; j < deps.getLength(); j++) {
                        if ("artifactId".equals(deps.item(j).getNodeName()) && micro.getArtifactId().equals(deps.item(j).getTextContent())) {
                            nowDependency = dependency;
                            toRunFor = false;
                            break BREAK_LABEL;
                        }
                    }
                }
            }

            if (nowDependency == null) {
                nowDependency = document.createElement("dependency");
                dependenciesNode.appendChild(nowDependency);
            }
        }

        if(micro.getApiProject() == 1) {
            nowDependency.getParentNode().removeChild(nowDependency);
        } else {
            cleanNodeChilds(nowDependency);
            Node groupIdNode = document.createElement("groupId");
            groupIdNode.setTextContent(this.groupId);
            nowDependency.appendChild(groupIdNode);

            Node artifactIdNode = document.createElement("artifactId");
            artifactIdNode.setTextContent(micro.getArtifactId());
            nowDependency.appendChild(artifactIdNode);

            Node versionNode = document.createElement("version");
            versionNode.setTextContent(micro.getVersion());
            nowDependency.appendChild(versionNode);
        }
        saveDocument(document, pomFile);
    }

    private void addApplicationYmlFileToMicroRes(String targetPath, String microArtifactId, int index) throws Exception {

        String resourcePath = targetPath + "/" + microArtifactId + "/src/main/resources/";

        File appYmlFile = new File(resourcePath+"/application.yml");
        File appYmlFileDev = new File(resourcePath+"/application-dev.yml");
        File appYmlFileProd = new File(resourcePath+"/application-prod.yml");

        // application.yml
        // application-dev.yml
        // application-prod.yml

        // 获取最大的port值
        int cloudPort = 8090;

        /**
         * port
         * name
         * host
         */
        if( ! appYmlFile.exists()) {
            // first add file
            StringBuilder content = new StringBuilder();
            content.append("spring:\n");
            content.append("  profiles:\n");
            content.append("    active: dev\n");

            writeFile(content.toString(), appYmlFile);

            int port = cloudPort + index;

            StringBuilder appContent = new StringBuilder();
            appContent.append("spring:\n");
            appContent.append("  application:\n");
            appContent.append("    name: ").append(microArtifactId).append("\n");
            appContent.append("\n");

            appContent.append("server:\n");
            appContent.append("  port: ").append(port).append("\n");
            appContent.append("\n");

            appContent.append("eureka:\n");
            appContent.append("  instance:\n");
            appContent.append("    hostname: 127.0.0.1\n");
            appContent.append("  client:\n");
            appContent.append("    registerWithEureka: true\n");
            appContent.append("    fetchRegistry: true\n");
            appContent.append("    serviceUrl: \n");
            appContent.append("      defaultZone: http://${eureka.instance.hostname}:8090/eureka/\n");
            appContent.append("\n");

            // client
            appContent.append(feignSStr).append("\n");
            appContent.append(feignEStr).append("\n");

            writeFile(appContent.toString(), appYmlFileProd);

            // zuul - dev
            appContent.append(zuulSStr).append("\n");
            appContent.append(zuulEStr).append("\n\n");

            writeFile(appContent.toString(), appYmlFileDev);
        }
    }

    private void addModuleOfMicroToRootPom(String targetPath, String microArtifactId) throws Exception {
        String pomFilePath = targetPath + "/pom.xml";
        Document document = getDocumentByFilePath(pomFilePath);
        Element rootElement = document.getDocumentElement();

        Node modulesNode = getChildsByTagName(rootElement, "modules").get(0);

        boolean noModule = true;
        NodeList nodeList = modulesNode.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            String moduleName = nodeList.item(i).getTextContent();
            if(microArtifactId.equals(moduleName)) {
                noModule = false;
                break;
            }
        }

        if(noModule) {
            Node moduleNode = document.createElement("module");
            moduleNode.setTextContent(microArtifactId);
            modulesNode.appendChild(moduleNode);
            saveDocument(document, pomFilePath);
        }
    }

    private void createMicroPomFile(String microProPath, String microArtifactId) throws Exception {
        String microPom = microProPath + "/pom.xml";
        File microPomFile = new File(microPom);
        if( ! microPomFile.exists()) {
            ByteArrayOutputStream bai = new ByteArrayOutputStream();
            Map<String, Object> dataObj = new HashMap<String, Object>();

            dataObj.put("groupId", this.groupId);
            dataObj.put("artifactId", artifactId);
            dataObj.put("microArtifactId", microArtifactId);

            GenFileByFtl mGenFileByFtl = new GenFileByFtl();
            mGenFileByFtl.fetchResourcesFromJar("micro.pom.xml", dataObj, bai);
            IOUtils.write(bai.toByteArray(), new FileOutputStream(microPomFile));
        }
    }

    private void genStarterAppClass(String microProPath, String microArtifactId, String microArtifactIdName, ProMicroService micro) throws Exception {
        String applicationClassName = StringUtil.getJavaTableName(microArtifactId) + "Application"; // e_commerce_order to ECommerceOrder

        String applicationClassPath = (microProPath+"/src/main/java/"+this.groupId+"."+microArtifactIdName).replaceAll("\\.", "/");
        applicationClassPath += "/"+applicationClassName+".java";

        File starterFile = new File(applicationClassPath);
        if( ! starterFile.exists()) {
            TopLevelClass appClass = new TopLevelClass(this.groupId + "." + microArtifactIdName + "." + applicationClassName);
            appClass.setVisibility(JavaVisibility.PUBLIC);

            if(micro.getApiProject() == 1) {
                appClass.addImportedType("org.springframework.cloud.netflix.zuul.EnableZuulProxy");
                appClass.addAnnotation("@EnableZuulProxy");
            }
            appClass.addAnnotation("@EnableFeignClients(basePackages = \"" + this.groupId + "\")");
            appClass.addAnnotation("@EnableDiscoveryClient");
            appClass.addAnnotation("@SpringBootApplication(scanBasePackages = \"" + this.groupId + "\")");

            appClass.addImportedType("org.springframework.boot.SpringApplication");
            appClass.addImportedType("org.springframework.boot.autoconfigure.SpringBootApplication");
            appClass.addImportedType("org.springframework.cloud.client.discovery.EnableDiscoveryClient");
            appClass.addImportedType("org.springframework.cloud.netflix.eureka.server.EnableEurekaServer");
            appClass.addImportedType("org.springframework.cloud.openfeign.EnableFeignClients");
            appClass.addImportedType("org.springframework.web.bind.annotation.GetMapping");
            appClass.addImportedType("org.springframework.web.bind.annotation.RestController");



            Method mainMethod = new Method();
            mainMethod.setVisibility(JavaVisibility.PUBLIC);
            mainMethod.setStatic(true);
            mainMethod.setName("main");

            Parameter parameter = new Parameter(new FullyQualifiedJavaType("String[]"), "args");
            mainMethod.addParameter(parameter);
            appClass.addMethod(mainMethod);

            mainMethod.addBodyLine("SpringApplication.run("+applicationClassName+".class, args);");

            String classTxt = appClass.getFormattedContent();
            writeFile(classTxt, starterFile);
        }
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
        String orgKey1 = "code-microservice";
        String descKey = artifactId.replaceAll("-", "_");

        String targetMPath = proPath;
        if(StringUtils.isNotBlank(mStr)) {
            targetMPath += "/" + artifactId + mStr;
        }

        Set<String> exts = ImmutableSet.of("java", "xml", "yml");
        List<File> files = new ArrayList<>();
        listAllFile(targetMPath, files, exts);

        for (File f : files) {
            String content = IOUtils.toString(new FileInputStream(f), "utf-8");
            content = content.replaceAll(orgGroupId, descGroupId).replaceAll(orgKey, descKey).replaceAll(orgKey1, artifactId);
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

            List<ProFile> controllerList = proFileMapper.selectFileAndModuleByMicro(micro.getId(), TypeEnum.FileTypeEnum.controller.name());

            log.info("micro-name={}, microId={}, cons-size={}", micro.getArtifactId(), micro.getId(), controllerList.size());

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

            if(micro.getApiProject() == 0) {
                genMicroFeignClient(controllerList, micro);
            }
        }
    }

    private String getQualifier(String fileName, String moduleName, String microArtifactId) {
        String className = fileName.replaceAll("Controller", "Client").replaceAll("Rest", "Client");
        String qualifier = microArtifactId+moduleName+"."+className;
        qualifier = qualifier.replaceAll("\\.", "-");
        return qualifier;
    }
    private void genMicroFeignClient(List<ProFile> controllerList, ProMicroService micro) throws Exception {

        // -common/src/main/java/com.test.test_order.module.Client.java
        String microArtifactId = micro.getArtifactId();
        String microArtifactIdName = microArtifactId.replaceAll("-", "_");
        String clientPkg = this.groupId+"."+microArtifactIdName+".client";
        String classPath = this.localPath+"/"+ this.artifactId + "/"+ this.artifactId + "-common/src/main/java/"+clientPkg;
        classPath = classPath.replaceAll("\\.", "/"); // common

        for(ProFile file : controllerList) {

            String moduleName = "";
            String moduePath = "";
            if(file.getModule() != null) {
                moduleName = "." + file.getModule().getName();
                moduePath = "/" + file.getModule().getName();;
            }

            String className = file.getName().replaceAll("Controller", "Client").replaceAll("Rest", "Client");
            String qualifier = getQualifier(file.getName(), moduleName, microArtifactId);

            String genClassName = clientPkg+moduleName+".gen.Super"+className;
            String childClassName = clientPkg+moduleName+"."+className;

            String genClassPath = classPath + moduePath + "/gen/Super"+className + ".java";
            String childClassPath = classPath + moduePath + "/"+className + ".java";

            File genClassFile = new File(genClassPath);
            File childClassFile = new File(childClassPath);

            // extends gen
            Interface clientInterface = new Interface(genClassName);
            clientInterface.setVisibility(JavaVisibility.PUBLIC);
            clientInterface.addImportedType(new FullyQualifiedJavaType(RequestMapping.class.getName()));
            clientInterface.addImportedType(new FullyQualifiedJavaType(Controller.class.getName()));
            clientInterface.addImportedType(new FullyQualifiedJavaType(RequestBody.class.getName()));
            clientInterface.addImportedType(new FullyQualifiedJavaType(RequestParam.class.getName()));
            clientInterface.addImportedType(new FullyQualifiedJavaType(RequestMethod.class.getName()));



            LOG.info("file.getImportTypes()={}", String.join(",", file.getImportTypes()));
            Map<String, String> importTypePath = new HashMap<>();
            for(String importedType : file.getImportTypes()) {
                clientInterface.addImportedType(new FullyQualifiedJavaType(importedType));
                importTypePath.put(importedType.substring(importedType.lastIndexOf(".")+1), importedType);
            }

            String rootPath = "";
            if(StringUtils.isNotBlank(file.getReqPath())) {
                rootPath = file.getReqPath();
            }

            // method
            for(ProFun fun : file.getFuns()) {

                //  是否生成客户端
                if( ! "true".equalsIgnoreCase(fun.getGenClient())) {
                    continue;
                }

                Method m = new Method(fun.getFunName());

                m.addJavaDocLine("/**");
                m.addJavaDocLine(" * "+fun.getName());
                m.addJavaDocLine(" * "+fun.getComment());

                /**
                 * 方法注解
                 */
//                m.addAnnotation("@ResponseBody");
                String reqPath = rootPath + "/" + fun.getReqPath();
                reqPath = reqPath.replaceAll("//", "/");

                m.addAnnotation("@RequestMapping(value=\""+ reqPath +"\", method=RequestMethod."+fun.getReqMethod()+")");

                /**
                 * 方法参数
                 */
                List<ProFunArg> args = fun.getArgs();
                for(ProFunArg arg : args) {

                    String paramName = arg.getName();
                    paramName = paramName.replace("{", "").replace("}", "");
                    Parameter mp = new Parameter(new FullyQualifiedJavaType(arg.getArgTypeName()), paramName);

                    m.addJavaDocLine(" * @param "+paramName);

                    /**
                     * add (Valid RequestBody Validated) if arg type is Vo<br>
                     * base Valid if arg type of base
                     */
                    if(TypeEnum.FunArgType.vo.name().equals(arg.getArgType())) {
                        if(HttpMethod.POST.name().equalsIgnoreCase(fun.getReqMethod()) ||
                                HttpMethod.PUT.name().equalsIgnoreCase(fun.getReqMethod()) ||
                                HttpMethod.DELETE.name().equalsIgnoreCase(fun.getReqMethod())) {
                            mp.addAnnotation("@RequestBody");
                        }
                    } else if(TypeEnum.FunArgType.base.name().equals(arg.getArgType())
                        // && StringUtils.isNotEmpty(arg.getValid())
                    ) {

                        /**
                         * rest full直持 例: /aap/{id} @PathVariable("id") String id
                         */
                        if(arg.getName().startsWith("{")) {
                            mp.addAnnotation(String.format("@PathVariable(\"%s\")", paramName));
                            clientInterface.addImportedType(new FullyQualifiedJavaType(PathVariable.class.getName()));
                        } else {
                            mp.addAnnotation(String.format("@RequestParam(\"%s\")", paramName));
                            clientInterface.addImportedType(new FullyQualifiedJavaType(PathVariable.class.getName()));
                        }
                    }
                    m.addParameter(mp);
                }

                m.addJavaDocLine(" */ ");
                /**
                 * 方法返回值
                 */
                if(StringUtils.isNotBlank(fun.getReturnShow())) {
                    LOG.info("returnShow={}", fun.getReturnShow());
                    m.setReturnType(new FullyQualifiedJavaType(fun.getReturnShow()));
                }
                clientInterface.addMethod(m);
            }

            // child class
            if( ! childClassFile.exists()) {
                Interface childInterface = new Interface(childClassName);
                childInterface.setVisibility(JavaVisibility.PUBLIC);
                childInterface.addAnnotation("@FeignClient(name=\"${"+feignClientPrev+"."+qualifier+"}\", qualifier=\""+qualifier+"\")");

                childInterface.addSuperInterface(new FullyQualifiedJavaType("Super"+className));

                //
                childInterface.addImportedType(new FullyQualifiedJavaType(genClassName));
                childInterface.addImportedType(new FullyQualifiedJavaType("org.springframework.cloud.openfeign.FeignClient"));
                writeFile(childInterface.getFormattedContent(), childClassFile);
            }

            // write file
            writeFile(clientInterface.getFormattedContent(), genClassFile);
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

                log.info("*********** vo-name={}", file.getName());

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
