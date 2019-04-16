/** 底部工具栏 */
define([ 'text!' + ctx + '/html/project/controller/controller-fun-params-row.html' ], function(
		template) {

	var data = {
		formData : {
			
		}
	};

	var component = {
		template : template,
		components: {
            
        },
		props : [ 'fieldInfo' ],
		data : function() {
			var _data = {}
			$.extend(true, _data, data);
			return _data;
		},
		methods : {
			
		},
		created : function() {

		},
		mounted : function() {
			
		}, computed: {
			
		}
	}

	return component;
})