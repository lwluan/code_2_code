package com.cd2cd.vo;

import com.cd2cd.domain.ProMicroService;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProMicroServiceVo extends ProMicroService {

    @NotNull(groups = {UpdateMicroService.class})
    @Override
    public Long getId() {
        return super.getId();
    }

    @NotNull(groups = {AddMicroService.class, UpdateMicroService.class})
    @Override
    public String getName() {
        return super.getName();
    }

    @NotNull(groups = {AddMicroService.class, UpdateMicroService.class})
    @Override
    public String getArtifactId() {
        return super.getArtifactId();
    }

    @NotNull(groups = {AddMicroService.class, UpdateMicroService.class})
    @Override
    public String getGroupId() {
        return super.getGroupId();
    }

    @NotNull(groups = {AddMicroService.class, UpdateMicroService.class})
    @Override
    public String getVersion() {
        return super.getVersion();
    }

    @NotNull(groups = {AddMicroService.class, UpdateMicroService.class})
    @Override
    public String getContextPath() {
        return super.getContextPath();
    }

    public interface AddMicroService{}
    public interface UpdateMicroService{}
}
