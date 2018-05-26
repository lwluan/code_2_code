var RestData = {
	
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