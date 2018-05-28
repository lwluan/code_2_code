var RestData = {
		
		updateOrSaveFunction: function(fun, cb) {
			var _url = '/project/addFunction';
			if( fun.id ) {
				_url = '/project/modifyFunction'
			}
			accessHttp({
                url: buildUrl(_url),
                contentType: 'application/json; charset=utf-8',
                data: JSON.stringify(fun),
                type: 'post',
                success: function (res) {
                	
                	bootoast({ message: '更新成功！', type: 'success', 
                        position:'right-bottom', timeout: 1 });
                	
                	if( cb ) {
	                	cb.call(this, res);
	                }
                }
            });
		},
		
		
		deleteFunctionByFunId: function(funId, cb) {
			accessHttp({
                url: buildUrl('/project/deleteFunctionByFunId?funId=' + funId),
                success: function (res) {
                	cb.call(this, res);
                }
            });
		},
		
		fetchFunsByFileId: function(fileId, cb) {
			var url = '/project/fetchFunsByFileId?fileId=' + fileId;
			accessHttp({
				url : buildUrl(url),
				success : function(res) {
					cb.call(this, res);
				}
			});
		},
		
		delFildById: function(fileId) {
			
			// 确认 confirm
			popModal('确认提示', '确定是否删除？', null, function() {
				var url = '/project/delFildById?fileId=' + fileId;
				accessHttp({
					url : buildUrl(url),
					success : function(res) {
						bootoast({ 
			                message: '删除成功！', 
			                type: 'success', 
			                position:'right-bottom', 
			                timeout: 1
			              }); 
					}
				});
			});
			
		},
		
		saveFileInfo: function(formData) {
			var _url = '/project/modifyFileInfo';
			accessHttp({
                url: buildUrl(_url),
                contentType: 'application/json; charset=utf-8',
                data: JSON.stringify(formData),
                type: 'post',
                success: function (res) {
                	
                	bootoast({ 
                        message: '修改成功！', 
                        type: 'success', 
                        position:'right-bottom', 
                        timeout: 1
                      }); 
                }
            });
		},
		
		fetchFileInfo: function(fileId, cb) {
			var url = '/project/fetchFileInfo?fileId=' + fileId;
			accessHttp({
				url : buildUrl(url),
				success : function(res) {
					cb.call(this, res);
				}
			});
		},
		
		fetchAllVoByProjectId: function(projectId, classType, cb) {
			var url = '/project/fetchFileByClassType?projectId=' + projectId + '&fileType=' + classType;
			accessHttp({
				url : buildUrl(url),
				success : function(res) {
					cb.call(this, res);
				}
			});
		},
		
		fetchAllTablesByProject: function(projectId, cb) {
			
			var url = '/project/fetchAllTablesByProject?projectId=' + projectId;
			accessHttp({
				url : buildUrl(url),
				success : function(res) {
					cb.call(this, res);
				}
			});
			
		},
		
		fetchTableHasColumnsByTableId: function(tableId, cb) {
			var url = '/project/fetchTableHasColumnsByTableId?tableId=' + tableId;
			accessHttp({
				url : buildUrl(url),
				success : function(res) {
					cb.call(this, res);
				}
			});
		}
		
} 