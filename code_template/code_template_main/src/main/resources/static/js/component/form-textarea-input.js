define([ 'text!' + ctx + '/html/component/form-textarea-input.html' ], function(
		template) {

	var component = {
		props : [ 'value', 'name', 'label', 'tip', 'rows', 'cols', 'colWidth' ],
		template : template,
		data : function() {
			return { rowsNum: 2, colsNum: 40, colWidth: 12, colWidthClass: 'col-sm-12' };
		},
		methods : {
			updateValue : function(value) {
				this.$emit('input', value)
			}
		},
		created : function() {
		
		}, mounted: function() {
			if(this.rows) {
				this.rowsNum = this.rows;
			}
			if(this.cols) {
				this.colsNum = this.cols;
			}
			if(this.colWidth) {
				this.colWidthClass = 'col-sm-' + this.colWidth;
			}
		}
	}

	return component;
})