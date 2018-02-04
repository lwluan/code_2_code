define([ 'text!' + ctx + '/html/component/list-column-option.html', 'rcss!'+ctx+'/css/zTreeStyle/zTreeStyle', ctx+'/js/lib/jquery.ztree.all-3.5.min.js' ], function(
		template) {

	/**
	 * 事件 change_page
	 */

	var component = {
		props : ['tableId'],
		template : template,
		data : function() {
			return {
				zTree:{},
				columns:[]
			};
		},
		methods : {
			loadTree: function(nodes) {
				var setting = {
                    check: { enable: true },
                    data: { simpleData: { idKey:'id', pIdKey:'pid', enable: true } },
                    callback: { 
                    	onClick: function (e, treeId, treeNode, clickFlag) { 
                    		zTree.checkNode(treeNode, !treeNode.checked, true); 
                    	} 
                    } 
                };
                var zTree = $.fn.zTree.init($("#column-tree"), setting, nodes);
                this.zTree = zTree;
			},
			completed: function() {
				var _nodes = this.zTree.getCheckedNodes(true);
				var tableEl = $(this.$el).parents('table');
				$(_nodes).each(function(){
					tableEl.removeClass('hide--' + this['index']);
				});
				
				// 隐藏未选中
				_nodes = this.zTree.getCheckedNodes(false);
				$(_nodes).each(function(){
					tableEl.addClass('hide--' + this['index']);
				});
				
				this.close();
			},
			close: function() {
				$(this.$el).find('.table-column-list').addClass('hide');
			}
			,open: function() {
				$(this.$el).find('.table-column-list').removeClass('hide');
			}
		},
		created : function() {
			
		},
		mounted : function() {
			
			var data = [];
			var _tr = $(this.$el).parents('tr');
			_tr.find('th').each(function(i){
				var text = $(this).text();
				var index = i + 1;
				if( ! $(this).hasClass('ignore') ) {
					var _checked = ! $(this).hasClass('no-show');
					data.push({name: text, index: index, checked: _checked});
				}
			});
			this.columns = data;
			this.loadTree(data);
			this.completed();
		},
	}

	return component;
})