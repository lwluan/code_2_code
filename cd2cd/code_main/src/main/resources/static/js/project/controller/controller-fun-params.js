/** 底部工具栏 */
define([ 'text!' + ctx + '/html/project/controller/controller-fun-params.html' ], function(
		template) {

	var data = {
		formData : {},
		voInfo: {}
	};

	var component = {
		template : template,
		components: {
            'controller-fun-params-row': createComponent('/js/project/controller/controller-fun-params-row.js'),
        },
		props : [ 'fileObj' ],
		data : function() {
			var _data = {}
			$.extend(true, _data, data);
			return _data;
		},
		methods : {
			
			showControllerFunParamsList: function(fun, index, cb) {
				
				$(this.$el).modal('show');
				var that = this;
				
				// query parmas list and property list if it is vo
				console.info(JSON.stringify(fun));
				
				if( fun.resVoId ) {
					
					RestData.fetchFileWithFieldByVoId(fun.resVoId, function(res) {
						that.voInfo = res.data;
					});
					
				}
				console.info('showControllerFunParamsList-> index=' + index);
				
			}
			
		},
		created : function() {

		},
		mounted : function() {
			
		}, computed: {
			
		}
	}

	return component;
})