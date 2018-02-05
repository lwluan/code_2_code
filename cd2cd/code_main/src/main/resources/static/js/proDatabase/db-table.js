define([ 'text!' + ctx + '/html/proDatabase/db-table.html'], function(template) {

	var initFormValidate = function() {
        /** 表单验证 * */
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
				var that = this;
				setTimeout(function(){
					that.formValidate.resetForm();
				});
				
				this.entityForm = params;
				
				var id = params.id;
				this.tabColumns = [];
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
                
			}, delTable: function() {
				var that = this;
				popModal('确认提示', '确定是否删除？', null, function() {
                    $('#pop_box').modal('hide');
                    accessHttp({
                        url: buildUrl('/database/delTable?id=' + that.entityForm.id),
                        success: function (res) {
                        	var newEntity = { databaseId: that.entityForm.databaseId };
            				that.entityForm = newEntity;
                        	that.$emit('completed');
                        }
                    });
                });
			}, 
			// 表行操作
			addTableRow: function() {
				var hasEmpty = true;
				for(var i=0; i<this.tabColumns.length; i++) {
					var tc = this.tabColumns[i];
					if( ! tc.id ) {
						hasEmpty = false;
						break;
					}
				}
				if( hasEmpty ) {
					this.tabColumns.push({tableId:this.entityForm.id, allowNull: true});
				}
			}, delTableRow: function(col, index) {
				var that = this;
                popModal('确认提示', '确定是否删除？', null, function() {
                    $('#pop_box').modal('hide');
                    accessHttp({
                        url: buildUrl('/database/delTableColumn?id=' + col.id),
                        success: function (res) {
                        	that.tabColumns.splice(index, 1);
                        }
                    });
                });
			}, saveTableRow: function(column, index) {
				var that = this;
				var _url = '/database/addTableColumn';
                _url = column.id ? '/database/modifyTableColumn' : _url;
                accessHttp({
                    url: buildUrl(_url),
                    contentType: 'application/json; charset=utf-8',
                    data: JSON.stringify(column),
                    type: 'post',
                    success: function (res) {
                    	if( res.data ) {
                    		column.id = res.data.id;
                    	}
                    	column['__changed'] = false;
                    	Vue.set(that.tabColumns, index, column);
                    }
                });
			}, changeValue: function(column, index) {
				column['__changed'] = true;
				Vue.set(this.tabColumns, index, column);
			}
		}, mounted : function() {
			this.formValidate = initFormValidate();
		}
	}
	
	return component;
});