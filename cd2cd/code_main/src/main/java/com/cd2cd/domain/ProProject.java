package com.cd2cd.domain;

import com.cd2cd.domain.gen.SuperProProject;

import java.io.Serializable;

public class ProProject extends SuperProProject implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * - /Users/lwl/java_sources/project_name/project_name_main/src/main/java/com.company.project_name_main
     * @return
     */
    public String getClassRootPath(ProMicroService micro) {
    	String localPath = getLocalPath();
    	String rootPro = getArtifactId();
    	String artifactId = rootPro.replaceAll("\\.", "/").replaceAll("-", "_");
    	
    	String groupId = getGroupId();

    	if(null != micro) {
            artifactId = micro.getArtifactId().replaceAll("\\.", "/").replaceAll("-", "_");
            return String.format("%s/%s/%s/src/main/java/%s/%s/", localPath, rootPro, micro.getArtifactId(), groupId, artifactId).replaceAll("\\.", "/");
        } else {
            return String.format("%s/%s/%s_main/src/main/java/%s/%s/", localPath, rootPro, artifactId, groupId, artifactId).replaceAll("\\.", "/");
        }
    }
    
    /**
     * com.company.project_name
     * @return
     */
    public String getClassRootPkg(ProMicroService micro) {
        String artifactId = getArtifactId();
        if(null != micro) {
            artifactId = micro.getArtifactId();
        }
    	return getGroupId() + "." + artifactId.replaceAll("-", "_");
    }
}