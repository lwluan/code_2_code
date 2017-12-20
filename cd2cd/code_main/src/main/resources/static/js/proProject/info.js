define(['text!'+ctx+'/html/proProject/info.html'], function( template ) {

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

    /**
     * 对外通知事件
     * completed
     */
    var data = {
        formData: {},
        formValidate: {},
        groupButton:{ 
        	values: [{label: '标准', key: 'standard'}, {label: '模块', key: 'module'}],
        	selected: 'standard',
        	changeSelected: function(val) {
        		this.selected = val;
        		data.formData.packageType = val;
        	}
        }
    };

    var component = {
        template: template,
        data: function(){ return data; },
        methods: {

            // 显示详情
            showInfoPanel: function (formData) {

                this.formValidate.resetForm();

                this.formData = formData;
                $('#addEntityModal').modal('show');

                var id = formData.id;
                id = id ? id : 0;

                accessHttp({
                    url: buildUrl('/proProject/detail/' + id),
                    success: function (res) {
                        data.formData = res.data;
                        data.groupButton.selected = data.formData.packageType;
                    }
                });

            },

            // 提交
            subFormData: function() {

                if( $('#formValidate').valid() ) {

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
                            $('#addEntityModal').modal('hide');
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
});