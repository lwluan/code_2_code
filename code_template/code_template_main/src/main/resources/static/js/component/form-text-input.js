define([ 'text!' + ctx + '/html/component/form-text-input.html' ], function(
		template) {

	
	/**
	 * colWidth:(1~12)
	 * 
	 */
	var component = {
		props : [ 'value', 'name', 'label', 'tip', 'colWidth', 'inputType' ],
		template : template,
		data : function() {
			return { 
				colWidthClass: '', // col-sm-12
				inputType: 'text'
			};
		},
		methods : {
			updateValue : function(value) {
				this.$emit('input', value)
			}
		},
		created : function() {
			
		}, mounted: function() {
			if(this.colWidth) {
				this.colWidthClass = 'col-sm-' + this.colWidth;
			}
			
			console.info('this.inputType=' + this.inputType);
			
			if( ! this.inputType) {
				this.inputType = 'text';
			}
		}
	}

	return component;
})