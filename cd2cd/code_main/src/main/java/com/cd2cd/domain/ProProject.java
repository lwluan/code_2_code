package com.cd2cd.domain;

import com.cd2cd.domain.gen.SuperProProject;

import java.io.Serializable;

public class ProProject extends SuperProProject implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * - /Users/lwl/java_sources/project_name/project_name_main/src/main/java/com.company.project_name_main
     * @return
     */
    public String getClassRootPath() {
    	String localPath = getLocalPath();
    	String artifactId = getArtifactId().replaceAll("\\.", "/").replaceAll("-", "_");
    	
    	String groupId = getGroupId();
    	
    	String classRootPath = String.format("%s/%s/%s_main/src/main/java/%s/%s/", localPath, artifactId, artifactId, groupId, artifactId).replaceAll("\\.", "/");
    	return classRootPath;
    }
    
    /**
     * com.company.project_name
     * @return
     */
    public String getClassRootPkg() {
    	return getGroupId() + "." + getArtifactId().replaceAll("-", "_");
    }
}