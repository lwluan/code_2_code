define([ 'text!' + ctx + '/html/proDatabase/database.html' ], function(template) {

	var initFormValidate = function() {
        /** 表单验证 **/
        var formValidate = $('#dbFormValidate').validate({
            rules:{
            	dbName:{ required:true },
            	username:{ required:true },
            	password: {required:true },
            	hostname: {required:true },
            	port: {required:true },
            },
            messages:{
            	dbName:{ required: '数据库不可为空' },
            	username:{ required: '用户名不可为空' },
            	password: {required: '密码不可为空' },
            	hostname: {required: '地址不可为空' },
            	port: {required: '端口不可为空' },
            }
        });

        return formValidate;
    }
	
	var data = {
		entityForm: {},
		formValidate: {}
	};

	var component = {
		template : template,
		data : function() {
			var _data = {}
        	$.extend(true, _data, data);
            return _data;
		}, methods : {
			showDetail: function(id) {
				this.formValidate.resetForm();
				var that = this;
				if( id ) {
					var _url = '/database/dbDetail?id=' + id;
	                accessHttp({
	                    url: buildUrl(_url),
	                    type: 'get',
	                    success: function (res) {
	                        that.entityForm = res.data;
	                    }
	                });
				} else {
					that.entityForm = {};
				}
			}, subFormData: function() {
                if( $('#dbFormValidate').valid() ) {
                	var postData = this.entityForm;

                    var _url = '/database/addDb';
                    _url = postData.id ? '/database/modifyDb' : _url;
                    
                    var that = this;
                    accessHttp({
                        url: buildUrl(_url),
                        contentType: 'application/json; charset=utf-8',
                        data: JSON.stringify(postData),
                        type: 'post',
                        success: function (res) {
                        	if( ! postData.id ) {
                        		that.entityForm = {};
                        		that.$emit('completed');
                        	}
                        }
                    });
                }
			}, delRowData: function (id) {
                var that = this;
                popModal('确认提示', '确定是否删除？', null, function() {
                    $('#pop_box').modal('hide');
                    accessHttp({
                        url: buildUrl('/database/delDb?id=' + id),
                        success: function (res) {
                        	that.entityForm = {};
                        	that.$emit('completed');
                        }
                    });
                });

            },
            
            /** 数据库中的数据同步到项目中 **/
            asyncDbToProFromDbBase: function(id) {
            	var that = this;
            	accessHttp({
                    url: buildUrl('/database/asyncDbToProFromDbBase?id=' + id),
                    success: function (res) {
                    	that.$emit('completed');
                    	console.info('同步成功！');
                    }
                });
            },
            asyncDbToDbBaseFromPro: function(id) {
            	var that = this;
            	accessHttp({
                    url: buildUrl('/database/asyncDbToDbBaseFromPro?id=' + id),
                    success: function (res) {
                    	that.$emit('completed');
                    	console.info('同步成功！');
                    }
                });
            }
            
            
		}, mounted: function() {
            this.formValidate = initFormValidate();
        }
	}
	
	return component;
});