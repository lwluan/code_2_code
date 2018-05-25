define([ 'text!' + ctx + '/html/project/file/vo-file.html' ], function(template) {

	var data = {
		formData : {},
		columns: [],
		fields: [],
		domainClass: [],
		table: {},
		typePath: {
			base: BASE_TYPES,
			vo:[],
			T:[{key: 'T', label: 'T' }]
		}
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

			changeSuperDomain: function() {
				var that = this;
				RestData.fetchTableHasColumnsByTableId(this.formData.superId, function(res) {
					var columns = res.data.columns;
					var table = res.data;
					
					that.columns = columns;
					that.table = table;
				});
					
			},
			/**
			 * base vo T
			 */
			changeDataType: function(dataType) {
				
				var that = this;
				if( 'vo' == dataType ) {
					
					// fetch all vo
					RestData.fetchAllVoByProjectId(projectId, 'vo', function(res) {
						var vos = res.data;
						if( vos ) {
							var voList = [];
							for( var i=0; i<vos.length; i++ ) {
								voList.push({key: vos[i].id, label: vos[i].name });
							}
							that.typePath.vo = voList;
						}
					});
				}
				
			},
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
							if( table ) {
								var columns = table.columns;
								that.columns = columns;
							}
							
							if( fields && fields.length > 0 ) {
								for( var i=0; i<fields.length; i++ ) {
									fields[i].typeOption = {key: fields[i].typeKey, label: fields[i].typePath};
								}
							}
							
							that.fields = fields;
							that.table = table;
							
							var formData = {
									id: res.data.id,
									name: res.data.name,
									comment: res.data.comment,
							};
							
							if( res.data.table ) {
								formData.extendsName = res.data.table.name;
								formData.superId = res.data.superId;
								formData.paradigm = res.data.paradigm;
							}
							
							that.formData = formData;
							setTimeout(function(){setTextareaStyle($('#fileComment')[0])}, 1000);
						}
					});

					// fetch tables from db of project
					RestData.fetchAllTablesByProject(projectId, function(res) {
						
						that.domainClass = res.data;
						
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
					this.fields.push( { fileId: fileObj.fileId, collectionType: 'single', dataType: 'base' } );
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
				
				f.typeKey = f.typeOption.key;
				f.typePath = f.typeOption.label;
				
				accessHttp({
                    url: buildUrl(_url),
                    contentType: 'application/json; charset=utf-8',
                    data: JSON.stringify(f),
                    type: 'post',
                    success: function (res) {
                    	var f = res.data;

                    	f.typeOption = {key: f.typeKey, label: f.typePath};
                    	
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
			},
			
			/**
			 * update data of file info
			 */
			saveFileInfo: function() {
				
				var _url = '/project/modifyFileInfo';
				accessHttp({
                    url: buildUrl(_url),
                    contentType: 'application/json; charset=utf-8',
                    data: JSON.stringify(this.formData),
                    type: 'post',
                    success: function (res) {
                    	
                    	bootoast({ 
                            message: '修改成功！', 
                            type: 'success', 
                            position:'right-bottom', 
                            timeout:2 
                          }); 
                    }
                });
			}
			
		},
		created : function() {

		},
		mounted : function() {
			makeExpandingArea($('#fileComment')[0]);
			this.loadVoFileInfo();
			this.changeDataType('vo');			
		}
	}

	return component;
});