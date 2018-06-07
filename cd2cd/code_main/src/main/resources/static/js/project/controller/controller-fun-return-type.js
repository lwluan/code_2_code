/** 底部工具栏 */
define([ 'text!' + ctx + '/html/project/controller/controller-fun-return-type.html' ], function(
		template) {

	var data = {
		formData : { resType: 'vo' },
		returnTypeDrodown: {
			values: [ { key : 'void', label : 'void 无返回' },
			          { key : 'vo', label : 'Vo 对象类型' },
			          { key : 'page', label : 'Page 页面' }, 
			          { key : 'file', label : 'File 下载文件' },
			          { key : 'img', label : 'Img 图片流' },
			          { key : 'string', label : 'String' }, 
			          ],
        	selected: { key : 'vo', label : '对象类型' },
		},
		voListDrodown: {
			values: [{key: '', label: '请选择'}],
        	selected: {key: '', label: '请选择'},
		},
		pageListDrodown: {
			values: [{key: '', label: '请选择'}],
        	selected: {key: '', label: '请选择'},
		},
		funIndex: 0,
		funObj: {},
		callBack: '',
		
		allVoListDic: {} // key: id, val: obj
	};

	var component = {
		template : template,
		components: {
            'T-vo-choose': createComponent('/js/project/T-vo-choose.js'),
        },
		props : [ 'fileObj' ],
		data : function() {
			var _data = {}
			$.extend(true, _data, data);
			return _data;
		},
		methods : {

			// show self panel in page
			popShowPanel : function(fun, index, cb) {
				this.callBack = cb;
				this.funIndex = index;
				
				
				var that = this;
				
				
				$(this.$el).modal('show');
				
				// init page data and vo data
				RestData.fetchAllVoByProjectId(projectId, 'vo', function(res){
					
					var list = res.data;
					var vos = [{key: '', label: '请选择'}];
					for( var i in list ) {
						var v = list[i];
						v.key = v.id;
						v.label = v.name;
						vos.push(v);
						
						that.allVoListDic[''+v.id] = v; 
					}
					that.voListDrodown.values = vos;
					
					
					RestData.fetchAllPageByProjectId(projectId, function(res){
						
						var list = res.data;
						var vos = [{key: '', label: '请选择'}];
						for( var i in list ) {
							var v = list[i];
							vos.push({key: v.id, label: v.name });
						}
						that.pageListDrodown.values = vos;
						
						// 初始化数据
						that.formData = $.extend(true, that.formData, fun);
						if( fun.resType ) {
							that.formData.resType = fun.resType;
						}
					});
					
					
				});
				
				
			},
			
			changeReturnTypeFunc: function(type) {
				
				var that = this;
				
				// void / page / baseType / vo 
				if( type == 'string' ) {
					
				} else if( type ==  'page' || type ==  'vo' ) {
					
				}
				
			},
			
			doSetValueCompleted: function() {
				
				
				var rt = this.formData.resType;
				var returnShow = '';
				if( rt == 'vo' || rt == 'page' ) {
					
					
					if( rt == 'page' ) {
						returnShow = returnShow + this.pageListDrodown.selected.label +  ' - ';
					}
					
					// formData.resVoId
					if( this.formData.resVoId ) {
						returnShow = returnShow + this.voListDrodown.selected.label;
					}
					/**
					 * BaseRes<Prodect> show this
					 */
					
				}
				
				// TODO set return show content
				console.info('funIndex=' + this.funIndex + ' - returnShow=' + returnShow);
				
				this.formData.resType = rt;
				this.formData.returnShow = returnShow;
				
				this.callBack(this.formData);
				this.$emit('completed', this.funIndex, this.formData);
				$(this.$el).modal('hide');
			}
		},
		created : function() {

		},
		mounted : function() {
			
		}
	}

	return component;
})