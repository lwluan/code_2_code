define([ 'text!' + ctx + '/html/component/form-checkbox.html' ], function(template) {

	
	/**
	 * colWidth:(1~12)
	 */
	var component = {
		props : [ 'value', 'name', 'label', 'tip', 'colWidth', 'selected', 'values' ],
		template : template,
		data : function() {
			return { 
				colWidthClass: 'col-sm-12',
				checkedVals: [],
				
			};
		},
		methods : {
			changeValue: function() {
				this.$emit('changeSelected', this.checkedVals);
				console.info(this.checkedVals);
			}
		},
		created : function() {
			
			
		}, mounted : function() {
			
			this.checkedVals = this.value;
			
			if(this.colWidth) {
				this.colWidthClass = 'col-sm-' + this.colWidth;
			}
			
		}, watch : {
			value : function(val) {
				this.checkedVals = val;
			}
		}
	}

	return component;
})