define([ 'text!' + ctx + '/html/component/form-group-btns.html' ], function(
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
				colWidthClass: 'col-sm-12',
				selectedKey : ''
			};
		},
		methods : {
			updateValue : function(value) {
				this.$emit('input', value)
			},
			selectItem : function(val) {
				this.value = val;
				this.selectedKey = val;
				this.$emit('changeSelected', val);
			}
		
		},
		created : function() {
			this.selectedKey = this.selected;
		}, mounted: function() {
			if(this.colWidth) {
				this.colWidthClass = 'col-sm-' + this.colWidth;
			}
		},watch : {
			value : function(val) {
				
				console.info('watch=' + this.selected);
				
				this.value = this.selected;
				this.selectedKey = this.selected;
				this.$emit('changeSelected', this.selected);
			}
		}
	}

	return component;
})