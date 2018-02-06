define([ 'text!' + ctx + '/html/component/form-down-menu.html' ], function(
		template) {

	
	/**
	 * colWidth:(1~12)
	 * 
	 */
	var component = {
		props : [ 'value', 'name', 'label', 'tip', 'colWidth', 'selected', 'values' ],
		template : template,
		data : function() {
			return { 
				colWidthClass: '', // col-sm-12
				selectedObj: {}
			};
		},
		methods : {
			selectItem : function(value) {
				this.$emit('changeSelected', value)
			}
		},
		created : function() {
			this.selectedObj = this.selected;
		}, mounted: function() {
			if(this.colWidth) {
				this.colWidthClass = 'col-sm-' + this.colWidth;
			}
			
		}, value : function(val) {
			
			this.selectedObj = val;
			this.$emit('changeSelected', val);
		}
	}

	return component;
})