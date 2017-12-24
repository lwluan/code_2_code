define([ 'text!' + ctx + '/html/proDatabase/db-table.html'], function(template) {

	
	var data = {
			dbTableList: [{},{},{},{},{}]
	};

	var component = {
		template : template,
		data : function() {
			return data;
		},
		methods : {
			// tableDetail
		},
		mounted : function() {
			
		}
	}
	
	return component;
});