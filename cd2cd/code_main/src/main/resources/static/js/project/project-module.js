/** 底部工具栏 */
define(['text!'+ctx+'/html/project/project-module.html'], function( template ) {

	var initFormValidate = function() {
        /** 表单验证 **/
        var formValidate = $('#formModuleValidate').validate({
            rules:{
//            	projectId:{ required:true, min:1 },
            	name:{ required:true },
            	showName:{ required:true },
            	description:{ required:true },
            },
            messages:{
            	projectId:{ min: "项目不可为空" },
            	name:{ required: "程序名称不可为空" },
            	showName:{ required: "显示名称不可为空" },
            	description:{ required: "模块描述不可为空" },
            }
        });

        return formValidate;
    }
	
	var data = {
		entityForm : {},
		projectList: [],
		formValidate: {},
		projectDropdown : {
			values: [{key: '0', label: '请选择项目'}],
			selected: {key: '0', label: '请选择项目'}
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
        	showProjectModule: function(moduleId) {
        		$(this.$el).modal('show');
        		
        		this.formValidate.resetForm();
        		var that = this;
        		that.entityForm = { projectId: projectId };
        		
        		if( moduleId ) {
        		
	            	// query project module
	            	accessHttp({
	                    url: buildUrl('/proProject/moduleDetail/' + moduleId),
	                    success: function (res) {
	                    	that.entityForm = res.data;
	                    }
	                });
        		}
        	},
        	
        	// 提交
            subFormData: function() {
            	
	        	if( $('#formModuleValidate').valid() ) {
	
	        		console.info(JSON.stringify(this.entityForm));
		        	
	                var postData = this.entityForm;
	                let _url = '/proProject/addModule';
	                _url = postData.id ? '/proProject/modifyModule' : _url;
	
	                var that = this;
	                accessHttp({
	                    url: buildUrl(_url),
	                    contentType: 'application/json; charset=utf-8',
	                    data: JSON.stringify(postData),
	                    type: 'post',
	                    success: function (res) {
	                    	$(that.$el).modal('hide');
	                        that.$emit('completed');
	                    }
	                });
	            }
            }
        }, created: function() {
        	
        }, mounted: function() {
        	this.formValidate = initFormValidate();
        }
    }

    return component;
})