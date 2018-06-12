/** 底部工具栏 */
define([ 'text!' + ctx + '/html/project/controller/controller-fun-return-type.html' ], function(
		template) {

	var data = {
		formData : { resType: 'vo', returnVo:{"name":"","id":0,"collectionType":"","paradigm":""} },
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
		chooseReturnVo: {},
		allVoListDic: {}, // key: id, val: obj
		changeTVoValue: 0
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
					var allVoListDic = {};
					var vos = [{key: '', label: '请选择'}];
					for( var i in list ) {
						var v = list[i];
						v.key = v.id;
						v.label = v.name;
						vos.push(v);
						
						allVoListDic[''+v.id] = v; 
					}
					that.allVoListDic = allVoListDic;
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
						
						// returnVo 显示处理
						var rVoStr = fun.returnVo;
						that.formData.returnVo = eval('('+rVoStr+')');
						
						if( fun.resType ) {
							that.formData.resType = fun.resType;
						}
						
						
						if( that.formData.resVoId ) {
							
							console.info(JSON.stringify(that.formData.returnVo));
							
							that.t_vo_choose_completed(that.formData.returnVo, that.formData.resType);
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
			
			t_vo_choose_completed: function(vo) {
				
				// collectionType
				var ct = vo.collectionType;
				ct = ct ? ct : 'single';
				var retVo = {name: vo.name, id: vo.id, collectionType: ct, paradigm: vo.paradigm };
				
				
				// vo.next
				var rName = vo.name;
				var tmp = vo;
				var endStr = '';
				while( tmp.next ) {
					tmp = tmp.next;
					retVo.next = {name: tmp.name, id: tmp.id, collectionType: tmp.collectionType, paradigm: tmp.paradigm };
					
					if( tmp.collectionType != 'single' ) {
						rName = rName + '<' + tmp.collectionType ;
						endStr = endStr + '>';
					}
					rName = rName + '<' + tmp.name;
					endStr = endStr + '>';
				}
				
				rName = rName + endStr;
				this.chooseReturnVo = {rName: rName, retVo: JSON.stringify(retVo) };
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
						returnShow = returnShow + this.chooseReturnVo.rName;
						this.formData.returnVo = this.chooseReturnVo.retVo;
					}
					/**
					 * BaseRes<Prodect> show this
					 */
				}
				
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
			
		}, computed: {
			returnVoName: function() {
				
			}
		}
	}

	return component;
})