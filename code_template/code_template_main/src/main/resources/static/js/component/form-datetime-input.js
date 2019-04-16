define([ 'text!' + ctx + '/html/component/form-datetime-input.html' ], function(
		template) {

	
	/**
	 * colWidth:(1~12)
	 * 
	 */
	var component = {
		props : [ 'value', 'name', 'label', 'tip', 'colWidth' ],
		template : template,
		data : function() {
			return { 
				colWidthClass: 'col-sm-12'
			};
		},
		methods : {
			updateValue : function(value) {
				this.$emit('input', value)
			},
			dateOnpicked: function(dp) {
				var value = dp.cal.getNewDateStr();
				this.$emit('input', value)
				//this.formData[dp.srcEl.id] = dp.cal.getNewDateStr();
			}
		},
		created : function() {
			
		}, mounted: function() {
			if(this.colWidth) {
				this.colWidthClass = 'col-sm-' + this.colWidth;
			}
		}
	}

	return component;
})