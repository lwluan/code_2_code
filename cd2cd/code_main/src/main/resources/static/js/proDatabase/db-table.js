define([ 'text!' + ctx + '/html/proDatabase/db-table.html'], function(template) {

	var initFormValidate = function() {
        /** 表单验证 **/
        var formValidate = $('#dbFormValidate').validate({
            rules:{
            	table_name:{ required:true },
            	table_comment:{ required:true },
            },
            messages:{
            	table_name:{ required: '数据库不可为空' },
            	table_comment:{ required: '用户名不可为空' },
            }
        });

        return formValidate;
    }
	
	var data = {
		entityForm: {},
		formValidate: {},
		tabColumns: []
	};

	var component = {
		template : template,
		data : function() {
			return data;
		},
		methods : {
			showDetail: function(params) {
				this.formValidate.resetForm();
				this.entityForm = params;
				var that = this;
				var id = params.id;
				if( id ) {
					var _url = '/database/tableDetail?id=' + id;
	                accessHttp({
	                    url: buildUrl(_url),
	                    success: function (res) {
	                        
	                        var _columns = res.data.columns;
	                        delete res.data.columns;
	                        that.entityForm = res.data;
	                        that.tabColumns = _columns;
	                        
	                    }
	                });
				} 
			}, subTableForm: function() {
                if( $('#tableFormValidate').valid() ) {
                	var postData = this.entityForm;

                    var _url = '/database/addTable';
                    _url = postData.id ? '/database/modifyTable' : _url;
                    
                    var that = this;
                    accessHttp({
                        url: buildUrl(_url),
                        contentType: 'application/json; charset=utf-8',
                        data: JSON.stringify(postData),
                        type: 'post',
                        success: function (res) {
                        	if( ! postData.id ) {
                        		that.entityForm = res.data;
                        	}
                        	that.$emit('completed');
                        }
                    });
                }
			}, addTableRow: function() {
				var hasEmpty = true;
				for(var i=0; i<this.tabColumns.length; i++) {
					var tc = this.tabColumns[i];
					if( ! tc.id ) {
						hasEmpty = false;
						break;
					}
				}
				if( hasEmpty ) {
					this.tabColumns.push({});
				}
			}
		}, mounted : function() {
			this.formValidate = initFormValidate();
		}
	}
	
	return component;
});