define([ 'text!' + ctx + '/html/project/file/vo-file.html' ], function(template) {

	var data = {
		formData : {},
		columns: [],
		fields: [],
		table: {}
	};

	var component = {
		template : template,
		props : [ 'fileObj' ],
		data : function() {
			var _data = {}
			$.extend(true, _data, data);
			return _data;
		},
		methods : {

			/**
			 * fetch file info
			 * vo-field\
			 * super-field\ table-column
			 * 
			 */
			// fileObj

			loadVoFileInfo : function() {
				var that = this;
				var fileObj = this.fileObj;
				
				if (fileObj.fileId && fileObj.fileId > 0) {

					var fileId = fileObj.fileId;
					var url = '/project/fetchFileInfo?fileId=' + fileId;
					accessHttp({
						url : buildUrl(url),
						success : function(res) {

							var fields = res.data.fields;
							var table = res.data.table;
							var columns = table.columns;
							
							that.fields = fields;
							that.columns = columns;
							that.table = table;
							
						}
					});

				}

			},
			
			addFieldToFile: function() {
				var fileObj = this.fileObj;
				var hasEmpty = true;
				for(var i=0; i<this.fields.length; i++) {
					var tc = this.fields[i];
					if( ! tc.id ) {
						hasEmpty = false;
						break;
					}
				}
				if( hasEmpty ) {
					this.fields.push( { fileId: fileObj.fileId } );
				}
			},

			
			/**
			 * save file to project
			 * save or update 
			 */
			saveFieldToFile: function(f, index) {
				
				/**
				 * comment 字段备注
				 * fileId 属于文件ID
				 * name 名称：英文显示，如：username
				 * dataType 类类型:基本数据类型：base，自定义对象：vo，范型：T
				 * typePath 如:String、com.user.UserVo
				 * collectionType 集合类型:单值：single，列表：list，集合：set，Map：map
				 */
				var that = this;
				var _url = '/project/saveFieldToFile';
				if( f.id ) {
					_url = '/project/updateFieldToFile'
				}
				
				accessHttp({
                    url: buildUrl(_url),
                    contentType: 'application/json; charset=utf-8',
                    data: JSON.stringify(f),
                    type: 'post',
                    success: function (res) {
                    	var f = res.data;
                    	Vue.set(that.fields, index, f);
                    	f['__changed'] = false;
                    }
                });
				
				
			}, delFieldFromFile: function(f, index) {
				var that = this;
                popModal('确认提示', '确定是否删除？', null, function() {
                    $('#pop_box').modal('hide');
                    if( ! f.id ) {
                    	that.fields.splice(index, 1);
                    	return;
                    }
                    
                    accessHttp({
                        url: buildUrl('/project/delFieldFromFile?id=' + f.id),
                        success: function (res) {
                        	that.fields.splice(index, 1);
                        }
                    });
                });
			}, fieldChangeValue: function(f, index) {
				f['__changed'] = true;
				Vue.set(this.fields, index, f);
			}
			
			
		},
		created : function() {

		},
		mounted : function() {
			this.loadVoFileInfo();
		}
	}

	return component;
});