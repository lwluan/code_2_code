/** 底部工具栏 */
define([ 'text!' + ctx + '/html/project/file/return-type.html' ], function(
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
        	selected: {key: '', label: '请选择'},
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
				console.info('returnType=' + type);
				var that = this;
				// 获取对应类型的数据
				
				// clear all 
				this.returnTypeValDrodown.values = [];
				this.returnTypeValDrodown.selected = {};
				
				// void / page / baseType / vo 
				if( type == 'baseType' ) {
					this.returnTypeValDrodown.values = BASE_TYPES;
					
					this.returnTypeValDrodown.selected = {key:'String', label:'String'};
				} else if( type ==  'page' ) {
					
				} else if( type ==  'vo' ) {
					RestData.fetchAllVoByProjectId(projectId, 'vo', function(res){
						
						var list = res.data;
						var vos = [];
						for( var i in list ) {
							var v = list[i];
							vos.push({key: v.id, label: v.name });
						}
						that.returnTypeValDrodown.values = vos;
						
					});
				}
				// returnTypeValDrodown.values = [{key:'', label:''}]
				
			}
		},
		created : function() {

		},
		mounted : function() {
			this.changeReturnTypeFunc(this.returnTypeDrodown.selected.key);
		}
	}

	return component;
})