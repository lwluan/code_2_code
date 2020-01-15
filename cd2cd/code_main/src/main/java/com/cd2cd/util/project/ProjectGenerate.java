package com.cd2cd.util.project;

import com.alibaba.fastjson.JSONObject;
import com.cd2cd.comm.ServiceCode;
import com.cd2cd.comm.exceptions.ServiceBusinessException;
import com.cd2cd.dom.java.*;
import com.cd2cd.domain.*;
import com.cd2cd.domain.gen.*;
import com.cd2cd.domain.gen.CommValidateCriteria;
import com.cd2cd.mapper.*;
import com.cd2cd.util.GenFileByFtl;
import com.cd2cd.util.ProjectGenUtil;
import com.cd2cd.util.StringUtil;
import com.cd2cd.util.mbg.h2.H2DatabaseUtil;
import com.cd2cd.vo.BaseRes;
import com.cd2cd.vo.ProProjectVo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.Sets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.api.dom.OutputUtilities;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.annotation.Resource;
import javax.annotation.Title;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.sql.SQLException;
import java.util.*;

/**
 * 项目生成类
 */
public abstract class ProjectGenerate {

    private Logger log = LoggerFactory.getLogger(ProjectGenerate.class);

    protected static String code_path = "code_template";
    protected static final Logger LOG = LoggerFactory.getLogger(ProjectGenUtil.class);
    protected static final String H2_DB_PATH = "./h2db";
    protected static final String H2_DB_PASSWORD = "h2";
    protected static final String H2_DB_USER = "123456";
    protected static String NEW_LINE = System.getProperty("line.separator");

    protected static Set<String> IGNORE_VO_GEN = Sets.newHashSet("BaseRes", "BaseReq");
    protected static Set<String> _n = Sets.newHashSet(".DS_Store", ".project", ".settings", ".idea", "target", "node_modules");

    protected ProProject project;
    protected String contextPath;			// 项目访问地址
    protected String artifactId;			// test_main； e-commerce-customer; file path
    protected String artifactIdName;		// artifactId.replaceAll("\\.", "/").replaceAll("-", "_"); // e_commerce_customer
    protected String groupId;				// 包名 com.test
    protected String name;				    // 项目; crm-admin中文/英文 名称
    protected String packageType;  		    // 包结构类型：standard、module
    protected String version;				// 项目版本号
    protected String description;			// 项目描述
    protected String localPath;			    // 本地生成路径
    protected String basePkgname;			// groupId + artifactIdName

    @Resource
    protected ProProjectMapper proProjectMapper;

    @Resource
    protected CommValidateMapper commValidateMapper;

    @Resource
    protected ProTableMapper proTableMapper;

    @Resource
    protected ProProjectDatabaseRelMapper proProjectDatabaseRelMapper;

    @Resource
    protected ProDatabaseMapper proDatabaseMapper;

    @Resource
    protected ProFunMapper funMapper;

    @Resource
    protected ProFunArgMapper funArgMapper;

    @Resource
    protected ProFileMapper proFileMapper;

    @Resource
    protected ProFieldMapper proFieldMapper;

    @Resource
    protected ProTableColumnMapper tableColumnMapper;

    protected Map<String, String> commValidMap;

    public BaseRes<String> genProject(ProProjectVo projectVo) throws Exception {

        Long id = projectVo.getId();

        log.info("id={}", id);
        ProProject proProject = proProjectMapper.selectByPrimaryKey(id);

        log.info("proProject={}", JSONObject.toJSONString(proProject));

        // 使用本传入地址生成
        if(StringUtils.isNotEmpty(projectVo.getLocalPath())) {
            proProject.setLocalPath(projectVo.getLocalPath());
        }

        this.project = proProject;
        contextPath = project.getContextPath();
        artifactId = project.getArtifactId();
        groupId = project.getGroupId();
        name = project.getName();
        packageType = project.getPackageType();
        version = project.getVersion();
        description = project.getDescription();
        localPath = project.getLocalPath();
        artifactIdName = artifactId.replaceAll("\\.", "/").replaceAll("-", "_");

        basePkgname = groupId + "." + artifactId.replaceAll("-", "_");


        commValidMap = getValidAndClassPath(proProject);

        genProjectBase();

        genDomain();

        genControllerAndService();

        getVo();

        return new BaseRes<>(ServiceCode.SUCCESS);
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

    /**
     * 将相对路径替换成决对路径
     * @param path
     * @return
     */
    protected String getRealPath(String path) {
        int n = path.indexOf("../");
        if (n > -1) {
            String eStr = path.substring(n + 3, path.length());
            String sStr = path.substring(0, n - 1);
            sStr = sStr.substring(0, sStr.lastIndexOf("/") + 1);
            String p = sStr + eStr;
            path = p;
            if (path.indexOf("../") > -1) {
                path = getRealPath(path);
            }
        }
        return path;
    }

    /**
     * 复制一个目录及其子目录、文件到另外一个目录
     */
    protected void copyFolder(File src, File dest) throws IOException {
        if(_n.contains(src.getName())) {
            return;
        }
        if (src.isDirectory()) {
            if (!dest.exists()) {
                if( ! dest.mkdir()) {
                    throw new ServiceBusinessException(ServiceCode.DIR_CONNOT_WRITE);
                }
            }
            String files[] = src.list();
            for (String file : files) {
                File srcFile = new File(src, file);
                File destFile = new File(dest, file);
                // 递归复制
                copyFolder(srcFile, destFile);
            }
        } else {
            InputStream in = new FileInputStream(src);
            OutputStream out = new FileOutputStream(dest);
            IOUtils.copy(in, out);
        }
    }

    protected void reNameFile(String f1, String f2) {
        File file1 = new File(f1);
        File file2 = new File(f2);

        if (file1.exists() && !file2.exists()) {
            file1.renameTo(file2);
        }
    }

    protected Document getDocumentByFilePath(String filePath) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(false);
        factory.setIgnoringComments(true);
        factory.setIgnoringElementContentWhitespace(false);
        factory.setCoalescing(false);
        factory.setExpandEntityReferences(true);

        DocumentBuilder builder = factory.newDocumentBuilder();
        InputStream iis = new FileInputStream(new File(filePath));
        Document document = builder.parse(iis);
        return document;
    }

    protected void saveDocument(Document document, String filePath) throws Exception {
        String contentStr = toStringFromDoc(document);
        IOUtils.write(contentStr, new FileOutputStream(filePath), "utf-8");
    }

    protected String toStringFromDoc(Document document) {
        String result = null;

        if (document != null) {
            StringWriter strWtr = new StringWriter();
            StreamResult strResult = new StreamResult(strWtr);
            TransformerFactory tfac = TransformerFactory.newInstance();
            try {
                javax.xml.transform.Transformer t = tfac.newTransformer();
                t.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                t.setOutputProperty(OutputKeys.INDENT, "yes");
                t.setOutputProperty(OutputKeys.METHOD, "xml"); // xml, html,
                // text
                t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
                t.transform(new DOMSource(document.getDocumentElement()), strResult);
            } catch (Exception e) {
                System.err.println("XML.toString(Document): " + e);
            }
            result = strResult.getWriter().toString();
            try {
                strWtr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    protected List<Node> getChildsByTagName(Node node, String tagName) {
        NodeList nodeList = node.getChildNodes();
        List<Node> nodes = new ArrayList<Node>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            if (tagName.equals(nodeList.item(i).getNodeName())) {
                nodes.add(nodeList.item(i));
            }
        }
        return nodes;
    }

    protected void cleanNodeChilds(Node node) {
        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            node.removeChild(nodeList.item(0));
            i--;
        }
    }

    protected void listAllFile(String filePath, List<File> files, Set<String> exts) {
        File file = new File(filePath);

        // 不替目录下的文件
        if(_n.contains(file.getName())) {
            return;
        }
        File[] files2 = file.listFiles(file1 -> {
            if (file1.isDirectory()) {
                return true;
            } else {

                String name = file1.getName();
                int endIdx = name.lastIndexOf(".");
                if(endIdx > -1) {
                    String ext = name.substring(endIdx+1);
                    if(exts.contains(ext)) {
                        return true;
                    }
                }
                return false;
            }
        });

        if (files2 != null) {
            for (File fi : files2) {
                if (fi.isDirectory()) {
                    listAllFile(fi.getAbsolutePath(), files, exts);
                }
                if (fi.isFile()) {
                    files.add(fi);
                }
            }
        }
    }

    protected void initH2Database(List<ProTable> tables, ProDatabase database) throws SQLException {
        String dbName = database.getDbName();
        String user = H2_DB_USER;
        String password = H2_DB_PASSWORD;

        H2DatabaseUtil nH2DatabaseUtil = new H2DatabaseUtil(H2_DB_PATH, dbName, user, password);

        for(ProTable t: tables) {
            String dropTable = "DROP TABLE IF EXISTS " + t.getName();
            nH2DatabaseUtil.exeSchema(dropTable);


            StringBuilder createTab = new StringBuilder();
            createTab.append("CREATE TABLE IF NOT EXISTS ");
            createTab.append(t.getName());
            createTab.append("(");
            List<ProTableColumn> columns = t.getColumns();
            List<String> _columnAndType = new ArrayList<String>();

            String primaryKeyColumn = null;
            for(ProTableColumn c: columns) {

                /**
                 * float(6,3) 会出错，直接生成时，为float
                 */
                String mysqlType = c.getMysqlType();
                if(mysqlType.toLowerCase().startsWith("float")) {
                    mysqlType = "float";
                }
                _columnAndType.add(c.getName() + " " + mysqlType);
                if("PRI".equalsIgnoreCase(c.getKeyType())) {
                    primaryKeyColumn = c.getName();
                }
            }
            if(StringUtils.isNotBlank(primaryKeyColumn)) {
                _columnAndType.add("PRIMARY KEY (`"+primaryKeyColumn+"`)");
            }
            createTab.append(Strings.join(_columnAndType, ','));
            createTab.append(")");

            System.out.println("createTab=" + createTab);
            nH2DatabaseUtil.exeSchema(createTab.toString());
        }
    }

    protected void genJavaFromDb(List<ProTable> tables, ProDatabase database, String javaModelPath, String sqlMapPath, boolean doDbName) {
        try {

            // 添加 db 名称
            String dbName = database.getDbName().replaceAll("-", "_");
            String sqlMap = groupId + "." + artifactIdName + (doDbName?"."+dbName:"") + ".mapper";
            String javaModel = groupId + "." + artifactIdName + (doDbName?"."+dbName:"") + ".domain";;

            log.info("\nsqlMap={}, \njavaModel={}, \nsqlMapPath={} \n javaModelPath={}", sqlMap, javaModel, sqlMapPath, javaModelPath);
            // h2
            String connectionURL = "jdbc:h2:"+H2_DB_PATH+"/" + database.getDbName();

            ByteArrayOutputStream bai = new ByteArrayOutputStream();
            GenFileByFtl mGenFileByFtl = new GenFileByFtl();
            Map<String, Object> dataObj = new HashMap<String, Object>();

            dataObj.put("userId", H2_DB_USER);
            dataObj.put("password", H2_DB_PASSWORD);

//			dataObj.put("driverClass", "com.mysql.jdbc.Driver");
            dataObj.put("driverClass", "org.h2.Driver");
            dataObj.put("connectionURL", connectionURL);
            dataObj.put("javaModel", javaModel);
            dataObj.put("sqlMap", sqlMap);
            dataObj.put("javaModelPath", javaModelPath);
            dataObj.put("sqlMapPath", sqlMapPath);


            /**
             * 自动生成设置 primary key
             * 1、auto_increment
             * 2、单个 PRI
             * 3、多个不设置
             */
            tables.stream().forEach(tab->{
                List<ProTableColumn> columns = tab.getColumns();
                String extraColumn = null;
                String priColumn = null;
                int priNum = 0;
                for(ProTableColumn c: columns) {
                    if("auto_increment".equalsIgnoreCase(c.getExtra())) {
                        extraColumn = c.getName();
                        break;
                    } else if ("PRI".equalsIgnoreCase(c.getKeyType())){
                        priColumn = c.getName();
                        priNum ++;
                    }
                }
                if(null != extraColumn) {
                    tab.setIdentityPrimaryKey(extraColumn);
                } else if(priNum == 1 && priColumn != null) {
                    tab.setIdentityPrimaryKey(priColumn);
                }
            });

            dataObj.put("tables", tables);

            mGenFileByFtl.fetchResourcesFromJar("gen_tables_template.xml.ftl", dataObj, bai);


            List<String> warnings = new ArrayList<String>();
            ConfigurationParser cp = new ConfigurationParser(warnings);

            Configuration config = cp.parseConfiguration(new ByteArrayInputStream(bai.toByteArray()));
            DefaultShellCallback shellCallback = new DefaultShellCallback(true);


            MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, shellCallback, warnings);
            Set<String> ids = new HashSet<String>();
            ids.add("tables");

            myBatisGenerator.generate(null, ids);
        } catch (Exception e) {
            LOG.error("error={}", e.getMessage(), e);
        }
    }

    /**
     * 获取参数中的，import type 和vo id
     * @param args
     * @return
     */
    protected FileIdsAndType getArgTypes(List<ProFunArg> args) {
        FileIdsAndType fit = new FileIdsAndType();
        parseArgType(args, fit);
        return fit;
    }

    private void parseArgType(List<ProFunArg> args, FileIdsAndType fit) {
        for(ProFunArg arg : args) {
            if(null != arg.getArgTypeId() && 0 < arg.getArgTypeId()) {
                fit.getTypeIds().add(arg.getArgTypeId());
            }
            String type = getCollectionClassPathByName(arg.getCollectionType());
            if(type != null) fit.getTypePaths().add(type);

            if( ! CollectionUtils.isEmpty(arg.getChildren())) {
                parseArgType(arg.getChildren(), fit);
            }
        }
    }

    private String getCollectionClassPathByName(String collectionType) {
        if(TypeEnum.CollectionType.list.name().equalsIgnoreCase(collectionType)) {
            return List.class.getName();
        } else if(TypeEnum.CollectionType.map.name().equalsIgnoreCase(collectionType)) {
            return Map.class.getName();
        } else if(TypeEnum.CollectionType.set.name().equalsIgnoreCase(collectionType)) {
            return Set.class.getName();
        }
        return null;
    }

    protected FileIdsAndType getFunReturnType(String returnVoJson) {
        // {"name":"BaseRes","id":3,"collectionType":"single","paradigm":"yes","next":{"name":"TestVO","id":6,"collectionType":"map","paradigm":"no"}}
        FileIdsAndType frt = new FileIdsAndType();
        if(StringUtils.isNotBlank(returnVoJson)) {
            JSONObject obj = JSONObject.parseObject(returnVoJson);
            parseFunReturnType(frt, obj);
        }
        return frt;
    }

    private void parseFunReturnType(FileIdsAndType frt, JSONObject obj) {
        String collectionType = obj.getString("collectionType");
        Long objId = obj.getLong("id");

        frt.getTypeIds().add(objId);

        String type = getCollectionClassPathByName(collectionType);
        if(type != null) frt.getTypePaths().add(type);

        if(obj.containsKey("next")) {
            parseFunReturnType(frt, obj.getJSONObject("next"));
        }
    }

    protected void importTypeToFile(List<Long> voIds, ProFile file, ProMicroService micro) {
        // 提取
        if( ! org.springframework.util.CollectionUtils.isEmpty(voIds)) {
            ProFileCriteria fileCriteria = new ProFileCriteria();
            fileCriteria.createCriteria().andIdIn(voIds);
            List<ProFile> fileList = proFileMapper.selectFileAndModule(fileCriteria);

            LOG.info("fileList={}", fileList.size());


            Set<String> fileTypes = getFileTypes(fileList, micro);
            file.getImportTypes().addAll(fileTypes);
        }
    }

    /**
     * 返回类文件路径, 根据module 和包结构类型
     * @param fileList
     * @return
     */
    protected Set<String> getFileTypes(List<ProFile> fileList, ProMicroService micro) {
        Set<String> types = new HashSet<>();

        // 添加微服务 micro - 生成到 common项目中

        for(ProFile f : fileList) {

            // f.getFileType() controller|service|vo|dao|domain
            String moduleName = "";
            if(TypeEnum.ProjectModulTypeEnum.module.name().equals(packageType) && f.getModule() != null) {
                moduleName = "." + f.getModule().getName();
            }
            String pkgName = basePkgname + moduleName + "."+f.getFileType()+"." + f.getName();

            if(micro !=null) {
                // --common/micro/module/vo
                // --common/micro/vo
                // vo 的microArtifactId中
                String microStr = "";
                if(f.getMicroService() != null) {
                    microStr = "." + f.getMicroService().getArtifactId();
                }
                pkgName = this.groupId + microStr + moduleName + "."+f.getFileType()+"." + f.getName();
                pkgName = pkgName.replaceAll("-", "_");
            }

            types.add(pkgName);
        }
        return types;
    }

    protected void genController(List<ProFile> controllerList, ProMicroService micro) throws Exception {

        // 跟据 micro生成位置
        String fgp = this.project.getClassRootPath(micro);
        String fcp = this.project.getClassRootPkg(micro);
        String packageType = this.project.getPackageType();

        Map<Long, Method> methodMap = new HashMap<>();

        for(ProFile file : controllerList) {

            String moduleName = null;
            ProModule module = file.getModule();
            if(module != null) {
                moduleName = module.getName();
            }

            String pkgName = "controller";
            String className = file.getName();
            ClassFile mClassFile = new ClassFile(fgp, fcp, moduleName, packageType, pkgName, className);

            String fileGenPath = mClassFile.getFileGenPath();
            TopLevelClass topClass = mClassFile.getType();

            topClass.setVisibility(JavaVisibility.PUBLIC);
            if(StringUtils.isNotBlank(file.getComment())) {
                topClass.addFileCommentLine("/** "+NEW_LINE + file.getComment() + NEW_LINE+" **/");
            }

            topClass.addAnnotation("@Controller");
            if(StringUtils.isNotBlank(file.getReqPath())) {
                topClass.addAnnotation("@RequestMapping(\""+file.getReqPath()+"\")");
            }

            /**
             *  import class
             */
            topClass.addImportedType(RequestMapping.class.getName());
            topClass.addImportedType(Controller.class.getName());
            topClass.addImportedType(ResponseBody.class.getName());
            topClass.addImportedType(RequestBody.class.getName());
            topClass.addImportedType(Validated.class.getName());
            topClass.addImportedType(Title.class.getName());
            topClass.addImportedType(RequestMethod.class.getName());


            LOG.info("file.getImportTypes()={}", String.join(",", file.getImportTypes()));
            Map<String, String> importTypePath = new HashMap<>();
            for(String importedType : file.getImportTypes()) {
                topClass.addImportedType(importedType);
                importTypePath.put(importedType.substring(importedType.lastIndexOf(".")+1), importedType);
            }

            methodMap.clear();
            /**
             * 生成方法，参数，返回值
             **/
            for(ProFun fun : file.getFuns()) {
                Method m = new Method(fun.getFunName());
                methodMap.put(fun.getId(), m);

                m.setVisibility(JavaVisibility.PUBLIC);

                String validGroupName = String.format("%s_%s", file.getName(), fun.getFunName());



                m.addJavaDocLine("/**");
                m.addJavaDocLine(" * "+ CodeUtils.getFunIdenStr(fun.getId()+""));
                m.addJavaDocLine(" * "+fun.getName());
                m.addJavaDocLine(" * "+fun.getComment());

                /**
                 * 方法注解
                 */
                m.addAnnotation("@Title(\""+fun.getName()+"\")");
                m.addAnnotation("@ResponseBody");
                m.addAnnotation("@RequestMapping(value=\""+fun.getReqPath()+"\", method=RequestMethod."+fun.getReqMethod()+")");

                /**
                 * 方法参数
                 */
                List<ProFunArg> args = fun.getArgs();
                boolean isPostAndReqBody = false;
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
                            isPostAndReqBody = true;
                        }
                        if(checkVoHasValid(arg)) {
                            mp.addAnnotation(String.format("@Validated(%s.class)", validGroupName));
                            // com.aaa.test.vo.TestVo.Controller_fun.class
                            String vgPath = importTypePath.get(arg.getArgTypeName());
                            if(StringUtils.isNotEmpty(vgPath)) {
                                vgPath = vgPath.substring(0, vgPath.lastIndexOf(".")) + ".gen.Super" + arg.getArgTypeName();
                                vgPath = String.format("%s.%s", vgPath, validGroupName);
                                topClass.addImportedType(vgPath);
                            }
                        }
                    } else if(TypeEnum.FunArgType.base.name().equals(arg.getArgType())
                        // && StringUtils.isNotEmpty(arg.getValid())
                    ) {

                        /**
                         * rest full直持 例: /aap/{id} @PathVariable("id") String id
                         */
                        if(arg.getName().startsWith("{")) {
                            mp.addAnnotation(String.format("@PathVariable(\"%s\")", paramName));
                            topClass.addImportedType(PathVariable.class.getName());
                        }

                        /** TODO 无法在controller fun 参数在验证, 添加拦截器验证，验证方法上
                         // 单独验证
                         JSONArray arr = JSONArray.parseArray(arg.getValid());

                         for(int i=0; i<arr.size(); i++) {
                         String key = arr.getString(i);
                         mp.addAnnotation(String.format("@%s", key));
                         String vName = key.substring(0, key.indexOf("("));
                         String vCPath = commValidMap.get(vName);
                         topClass.addImportedType(vCPath);
                         }
                         **/
                    }

                    m.addParameter(mp);
                }

                /**
                 * 需要数据验证时，添加 BindingResult bindingResult
                 */
                if(args.size() > 0 && isPostAndReqBody) {
                    Parameter mp = new Parameter(new FullyQualifiedJavaType(BindingResult.class.getName()), "bindingResult");
                    m.addParameter(mp);
                    topClass.addImportedType(BindingResult.class.getName());
                }


                /**
                 * todo
                 */
                m.addBodyLine("// TODO ");

                // function end
                m.addJavaDocLine("**/");

                /**
                 * 方法返回值
                 */
                if(StringUtils.isNotBlank(fun.getReturnShow())) {
                    LOG.info("returnShow={}", fun.getReturnShow());
                    m.setReturnType(new FullyQualifiedJavaType(fun.getReturnShow()));

                    /**
                     * 默认返回 null
                     */
                    m.addBodyLine("return null;");
                }

                topClass.addMethod(m);
            }

            File genFile = new File(fileGenPath);
            if( ! genFile.getParentFile().exists()) {
                genFile.getParentFile().mkdirs();
            }

            if( ! genFile.exists()) {
                IOUtils.write(topClass.getFormattedContent(), new FileOutputStream(new File(fileGenPath)), "utf-8");
            } else {
                String classTxt = IOUtils.toString(new FileInputStream(genFile), "utf-8");

                // 添加 import; 1先获取原来import,2再判断加入
                Set<String> newImports = OutputUtilities.calculateImports(topClass.getImportedTypes());
                classTxt = CodeUtils.updateClassImport(classTxt, newImports);

                // 更新类 root path


                // 更新类注释


                String preFunIden = null;
                for(Long funId: methodMap.keySet()) {

                    String iden = CodeUtils.getFunIdenStr(funId+"");
                    Method m = methodMap.get(funId);
                    if(classTxt.indexOf(iden) > -1) {
                        // update fun
                        String funHeader = CodeUtils.getFunHeaderByIdentify(classTxt, iden);

                        m.getBodyLines().clear();

                        String mStr = m.getFormattedContent(1, false, topClass);
                        mStr  = mStr.replace("abstract ", "");
                        mStr = mStr.substring(mStr.indexOf(iden) + iden.length());

                        mStr = mStr.substring(0, mStr.length() -1) + " {" + NEW_LINE;
                        classTxt = classTxt.replace(funHeader, mStr);
                    } else {
                        String mStr = m.getFormattedContent(1, false, topClass);

                        // 类当中无方法存在时，直接添加到类的底部
                        if(preFunIden == null) {
                            int lastIndex = classTxt.lastIndexOf("}");
                            String sTmpStr = classTxt.substring(0, lastIndex);
                            String eTmpStr = NEW_LINE+"}";

                            classTxt = sTmpStr + mStr + eTmpStr;
                        } else {
                            // add fun
                            String funHeader = CodeUtils.getFunHeaderByIdentify(classTxt, preFunIden);

                            String sTmpStr = classTxt.substring(0, classTxt.indexOf(funHeader) + funHeader.length());
                            String eTmpStr = classTxt.substring(classTxt.indexOf(funHeader) + funHeader.length());

                            int funEndIndex = CodeUtils.findFunEndIndex(eTmpStr) + 1;
                            String preFunBody = eTmpStr.substring(0, funEndIndex);
                            eTmpStr = eTmpStr.substring(funEndIndex, eTmpStr.length());
                            classTxt = sTmpStr + preFunBody + NEW_LINE + mStr + eTmpStr;
                        }
                    }

                    preFunIden = iden;
                }

                // 回写内容
                IOUtils.write(classTxt, new FileOutputStream(new File(fileGenPath)), "utf-8");
            }
        }
    }

    /** 生成 Service
     * @throws IOException
     * @throws FileNotFoundException
     *
     */
    protected void genService(List<ProFile> controllerList, ProMicroService micro) throws FileNotFoundException, IOException {

        for(ProFile file : controllerList) {

            String moduleName = null;
            ProModule module = file.getModule();
            if(module != null) {
                moduleName = module.getName();
            }

            String fgp = project.getClassRootPath(micro);
            String fcp = project.getClassRootPkg(micro);
            String packageType = project.getPackageType();
            String pkgName = "service";
            String className = file.getName();
            ClassFile classFile = new ClassFile(fgp, fcp, moduleName, packageType, pkgName, className);

            GenServiceHelper mGenServiceHelper = new GenServiceHelper(classFile, file);
            mGenServiceHelper.genCode();

        }

    }

    protected boolean checkVoHasValid(ProFunArg arg) {
        if(arg.getChildren() != null) {
            for(ProFunArg a: arg.getChildren()) {
                if(StringUtils.isNotEmpty(a.getValid())) {
                    return true;
                }
            }
        }
        return false;
    }

    protected FileIdsAndType getFieldTypes(List<ProField> fields) {
        FileIdsAndType fit = new FileIdsAndType();
        for(ProField field : fields) {
            if(TypeEnum.FieldDataType.vo.name().equalsIgnoreCase(field.getDataType())) {
                fit.getTypeIds().add(Long.valueOf(field.getTypeKey()));
            }
            String type = getCollectionClassPathByName(field.getCollectionType());
            if(type != null) fit.getTypePaths().add(type);
        }
        return fit;
    }

    /**
     * 生成 vo类
     * @param voList
     * @throws FileNotFoundException
     * @throws IOException
     */
    protected void toDogenVo(List<ProFile> voList, ProMicroService micro) throws Exception {
        for(ProFile file : voList) {

            // ignore vo BaseRes BaseReq
            if(IGNORE_VO_GEN.contains(file.getName())) continue;

            ProModule module = file.getModule();
            String fileGenPath = (localPath + "/" + artifactId + "/"+artifactId+"_main/src/main/java/" + groupId + "." + artifactId).replaceAll("\\.", "/");
            String filePkg = groupId + "." + artifactId;

            // 是否有模块名称
            String modulePathName = "";
            String modulePkgName = "";
            if( TypeEnum.ProjectModulTypeEnum.module.name().equals(packageType) ) {
                if (null != module && StringUtils.isNotBlank(module.getName())) {
                    modulePathName = "/" + module.getName();
                    modulePkgName = "." + module.getName();
                }
            }

            String fileTargetPath = fileGenPath + modulePathName + "/vo/gen/Super" + file.getName() + ".java";
            String fileClassPath = filePkg + modulePkgName + ".vo.gen.Super" + file.getName();

            String childTargetPath = fileGenPath + modulePathName+ "/vo/" + file.getName() + ".java";
            String childClassPath = filePkg + modulePkgName + ".vo." + file.getName();

            // -common/com.*/-module-/vo
            // 微服务
            if(micro != null) {
                String microArti = micro.getArtifactId();
                String proName = artifactId+"-common";
                String microArtiPkg = microArti.replaceAll("\\.", "/").replaceAll("-", "_");
                fileGenPath = (localPath + "/" + artifactId + "/"+proName+"/src/main/java/" + groupId + "." + microArtiPkg).replaceAll("\\.", "/");
                filePkg = groupId + "." + microArtiPkg;

                fileTargetPath = fileGenPath + modulePathName + "/vo/gen/Super" + file.getName() + ".java";
                fileClassPath = filePkg + modulePkgName + ".vo.gen.Super" + file.getName();


                childTargetPath = fileGenPath + modulePathName + "/vo/" + file.getName() + ".java";
                childClassPath = filePkg + modulePkgName + ".vo." + file.getName();

            }

            /**
             * 范型
             */
            if("yes".equalsIgnoreCase(file.getParadigm())) {
                fileClassPath += "<T>";
            }

            FullyQualifiedJavaType fileType = new FullyQualifiedJavaType(fileClassPath);
            TopLevelClass topClass = new TopLevelClass(fileType);
            topClass.setVisibility(JavaVisibility.PUBLIC);
            if(StringUtils.isNotBlank(file.getComment())) {
                topClass.addFileCommentLine("/** "+NEW_LINE + file.getComment() + NEW_LINE+" **/");
            }
            /**
             * 导入vo import
             */
            for(String importedType : file.getImportTypes()) {
                topClass.addImportedType(importedType);
            }

            /**
             * set super class
             */
            if(StringUtils.isNotBlank(file.getSuperName())) {
                String superClass = StringUtil.getJavaTableName(file.getSuperName());

                String superTypePath = filePkg + ".domain." +superClass;
                if(micro != null) {
                    // TODO artifactId+dbName+domain

                    Long superId = file.getSuperId();
                    // select db_name from pro_database where id = (select database_id from pro_table where id=266);
                    String dbName = proDatabaseMapper.selectDbNameByTableId(superId);
                    superTypePath = (groupId + "." + artifactId+"."+dbName+".domain.").replaceAll("-", "_") +superClass;
                }

                topClass.addImportedType(superTypePath);
                topClass.setSuperClass(superClass);
            }

            /**
             * validate inner class
             */
            Set<String> innerValidClass = new HashSet<>();

            /**
             * vo生成
             */
            List<ProField> fields = file.getFields();
            for(ProField field: fields) {

                Field f = new Field();
                f.setVisibility(JavaVisibility.PRIVATE);
                f.setName(field.getName());

                if(StringUtils.isNotEmpty(field.getComment())) {
                    f.addJavaDocLine("/**" +field.getComment() + " */");
                }

                // DataType is Date import java.util.date
                if("Date".equals(field.getTypePath())) {
                    topClass.addImportedType("java.util.Date");
                }

                String typeStr = CodeUtils.typeByCollectionType(field.getTypePath(), field.getCollectionType());

                // set valid
                Map<String, Set<String>> valids = file.getPropertyValid().get(field.getName());

                if(null != valids) {
                    Set<String> keys = valids.keySet();
                    for(String v: keys) {

                        Set<String> groupSet = valids.get(v);
                        innerValidClass.addAll(groupSet);

                        // annotiation group
                        String annotation = getFieldValidAnnotation(v, groupSet);
                        f.addAnnotation(annotation);

                        // gen group interface

                        // import valid class
                        String vName = v.substring(0, v.indexOf("("));
                        String vCPath = commValidMap.get(vName);
                        topClass.addImportedType(vCPath);
                    }
                }

                FullyQualifiedJavaType type = new FullyQualifiedJavaType(typeStr);
                f.setType(type);
                topClass.addField(f);


                /**
                 * 添加 set get 方法
                 */
                Method getM = new Method("get" + StringUtil.firstUpCase(field.getName()));
                getM.setVisibility(JavaVisibility.PUBLIC);
                getM.setReturnType(type);
                getM.addBodyLine("return this." + field.getName() + ";");
                topClass.addMethod(getM);

                // set
                Method setM = new Method("set" + StringUtil.firstUpCase(field.getName()));
                setM.setVisibility(JavaVisibility.PUBLIC);
                setM.addBodyLine("this." + field.getName() + " = "+field.getName()+";");
                Parameter parameter = new Parameter(type, field.getName());
                setM.addParameter(parameter);
                topClass.addMethod(setM);
            }

            /**
             * 设置get 方法使用于 table的验证使用
             **/
            List<ProField> validateMethods = file.getValidateMethods();
            for(ProField vm:validateMethods) {
                Map<String, Set<String>> valids = file.getPropertyValid().get(vm.getName());
                if(null != valids) {

                    Method getM = new Method("get" + StringUtil.getJavaTableName(vm.getName()));
                    getM.setVisibility(JavaVisibility.PUBLIC);
                    FullyQualifiedJavaType type = new FullyQualifiedJavaType(vm.getDataType());
                    getM.setReturnType(type);
                    getM.addBodyLine("return super.get" + StringUtil.getJavaTableName(vm.getName()) + "();");

                    getM.addAnnotation("@Override");

                    // import type
                    if(vm.getDataType().indexOf(".") > -1) {
                        topClass.addImportedType(vm.getDataType());
                    }

                    Set<String> keys = valids.keySet();
                    for(String v: keys) {

                        Set<String> groupSet = valids.get(v);
                        innerValidClass.addAll(groupSet);

                        // annotiation group
                        String annotation = getFieldValidAnnotation(v, groupSet);
                        getM.addAnnotation(annotation);

                        // gen group interface

                        // import valid class
                        String vName = v.substring(0, v.indexOf("("));
                        String vCPath = commValidMap.get(vName);
                        topClass.addImportedType(vCPath);
                    }
                    topClass.addMethod(getM);
                }
            }

            File genFile = new File(fileTargetPath);
            if( ! genFile.getParentFile().exists()) {
                new File(fileTargetPath).getParentFile().mkdirs();
            }

            // inner valid class - innerValidClass
            String inStr = getValidInnerClass(innerValidClass);
            String clStr = topClass.getFormattedContent();
            clStr = clStr.substring(0, clStr.lastIndexOf("}")) + NEW_LINE + inStr + NEW_LINE+"}";

            IOUtils.write(clStr, new FileOutputStream(new File(fileTargetPath)), "utf-8");

            File childClass = new File(childTargetPath);
            if( ! childClass.exists()) {
                // inner extend
                String t = "";
                if("yes".equalsIgnoreCase(file.getParadigm())) {
                    t += "<T>";
                }

                //
                FullyQualifiedJavaType childType = new FullyQualifiedJavaType(childClassPath + t);

                TopLevelClass childTypeClass = new TopLevelClass(childType);
                childTypeClass.addImportedType(fileClassPath);
                childTypeClass.addImportedType(JsonInclude.class.getName());

                childTypeClass.addAnnotation("@JsonInclude(JsonInclude.Include.NON_NULL)");

                // 判断是否为T
                childTypeClass.setSuperClass("Super" + file.getName() + t);

                childTypeClass.setVisibility(JavaVisibility.PUBLIC);
                childTypeClass.addFileCommentLine("/** "+NEW_LINE + file.getComment() + NEW_LINE+" **/");
                IOUtils.write(childTypeClass.getFormattedContent(), new FileOutputStream(childClass), "utf-8");
            }
        }
    }

    protected String getValidInnerClass(Set<String> innerValidClass) {
        StringBuilder ins = new StringBuilder();
        for(String iC: innerValidClass) {
            ins.append(String.format("\tpublic interface %s{} "+NEW_LINE, iC));
        }
        return ins.toString();
    }

    protected String getFieldValidAnnotation(String annotation, Set<String> groupSet) {
        Set<String> gs = new HashSet<>();
        for(String g: groupSet) {
            gs.add(g+".class");
        }

        boolean hasValue = ! annotation.endsWith("()");
        StringBuilder sb = new StringBuilder("@");
        sb.append(annotation.substring(0, annotation.indexOf("(")+1));
        sb.append("groups={");
        sb.append(Strings.join(gs, ','));
        sb.append("}");
        if(hasValue) sb.append(", ");
        sb.append(annotation.substring(annotation.indexOf("(")+1, annotation.length()));
        return sb.toString();
    }

    protected void writeFile(String content, File file) throws Exception {
        if( ! file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        IOUtils.write(content, new FileOutputStream(file), "utf-8");
    }


    protected abstract void genProjectBase() throws Exception;

    protected abstract void genDomain() throws Exception;

    protected abstract void genControllerAndService() throws Exception;

    protected abstract void getVo() throws Exception;

}
