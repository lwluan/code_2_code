/** 底部工具栏 */
define([ 'text!' + ctx + '/html/project/file/vo-file.html' ], function(
		template) {

	var data = {
		formData : {},
		returnTypeDrodown: {
			values: [ { key : 'void', label : '无返回' },
			          { key : 'page', label : '页面' }, 
			          { key : 'baseType', label : '基本数据类型' }, 
			          { key : 'vo', label : '对象类型' }],
        	selected: { key : 'vo', label : '对象类型' },
		},
		returnTypeValDrodown: {
			values: [],
        	selected: {},
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

			// show self panel in page
			popShowPanel : function() {
				$(this.$el).modal('show');
			},
			
			changeReturnTypeFunc: function(type) {
				turnTypeValDrodown.values = [{key:'', label:''}]
				
			}
		},
		created : function() {

		},
		mounted : function() {
			
		}
	}

	return component;
})