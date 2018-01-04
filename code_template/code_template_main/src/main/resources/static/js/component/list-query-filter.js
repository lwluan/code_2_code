define([ 'text!' + ctx + '/html/component/list-query-filter.html' ], function(template) {

	/**
	var data = {
		conditionSet : [ {
			type : 'group-button',
			name : 'status',
			label : '状态',
			defaultVal : 'all',
			changedVal : '',
			values : [ {
				key : 'all',
				label : '全部'
			}, {
				key : 'enable',
				label : '启用'
			}, {
				key : 'disable',
				label : '禁用'
			} ]
		}, {
			type : 'select',
			name : 'status1',
			label : '状态',
			defaultVal : 'all',
			changedVal : 'all',
			values : [ {
				key : 'all',
				label : '全部'
			}, {
				key : 'enable',
				label : '启用'
			}, {
				key : 'disable',
				label : '禁用'
			} ]
		} ]
	};
	*/
	var component = {
		props : [ 'conditionSet' ],
		template : template,
		data : function() {
			return {};
		},
		methods : {
			queryComplete: function() {
				var queryObj = {};
				for( var i=0; i<this.conditionSet.length; i++ ) {
					var condition = this.conditionSet[i];
					queryObj[condition.name] = condition.changedVal;
				}
				this.$emit('query-complete', queryObj);
			}, 
			queryReset: function() {
				for( var i=0; i<this.conditionSet.length; i++ ) {
					var condition = this.conditionSet[i];
					condition.changedVal = condition.defaultVal;
				}
			}
		},
		created : function() {
			
		},
		mounted : function() {

			var filterPanel = $('.right-filter-panel');
			var width = filterPanel.width() + 5;
			filterPanel.css({
				'right' : -width
			});
			filterPanel.removeClass('fade');
		}
	}

	return component;
})