/** 底部工具栏 */
define(['text!'+ctx+'/html/project/project-info.html'], function( template ) {

	var initFormValidate = function() {
        /** 表单验证 **/
        var formValidate = $('#formValidate').validate({
            rules:{
                name:{ required:true },
                description:{ required:true },
                groupId:{ required:true },
                artifactId:{ required:true },
                packageType:{ required:true },
                version:{ required:true},
                contextPath: {required:true },
            },
            messages:{
            	name:{ required: "项目名称不可为空" },
            	description:{ required: "项目简介不可为空" },
            	groupId:{ required: "groupId 不可为空" },
            	artifactId:{ required: "artifactID 不可为空" },
            	packageType:{ required:'结构类型 不可为空' },
            	version:{ required: '项目版本不正确' },
            	contextPath:{ required: "访问路径不可为空" },
            }
        });

        return formValidate;
    }
	
    var data = { 
    	formData: {},
    	formValidate: {},
    	allProjectDbList: [],
    	packageTypes: {
    		values: [{label: '标准', key: 'standard'}, {label: '模块', key: 'module'}],
        	selected: 'standard',
    	}
    };
    
    var component = {
        template: template,
        data: function(){ 
        	var _data = {}
        	$.extend(true, _data, data);
            return _data;
        },
        methods: {
        	showProjectInfo: function() {
        		$(this.$el).modal('show');
        		this.formValidate.resetForm();
        		var that = this;
            	accessHttp({
                    url: buildUrl('/proProject/detail/' + projectId),
                    success: function (res) {
                    	that.formData = res.data;
                    	that.packageTypes.selected = data.formData.packageType;
                    	
                    	that.formData.dbList = res.data.dbIds;
                    	
                    	// 数据库列表
                    	accessHttp({
                            url: buildUrl('/database/databaseList'),
                            success: function (res) {
                            	var dbs = res.data;
                            	
                            	var allProjectDbList = [];
                            	for(var i=0; i<dbs.length; i++) {
                            		allProjectDbList.push({key: dbs[i].id, label: dbs[i].dbName});
                            	}
                            	
                            	that.allProjectDbList = allProjectDbList;
                            }
                        });
                    }
                });
        	},
        	// 提交
            subFormData: function() {

                if( $('#formValidate').valid() ) {

                	console.info(JSON.stringify(this.formData));
                	
                	return;
                    var postData = this.formData;
                    let _url = '/proProject/add';
                    _url = postData.id ? '/proProject/modify' : _url;

                    var that = this;
                    accessHttp({
                        url: buildUrl(_url),
                        contentType: 'application/json; charset=utf-8',
                        data: JSON.stringify(postData),
                        type: 'post',
                        success: function (res) {
                        	$(this.$el).modal('hide');
                            that.$emit('completed');
                        }
                    });
                }
            }, 
            loadAllDbList: function() {
            	
            }
        
        }, created: function() {
        	
        	
        	
        }, mounted: function() {
        	this.formValidate = initFormValidate();
        }
    }

    return component;
})