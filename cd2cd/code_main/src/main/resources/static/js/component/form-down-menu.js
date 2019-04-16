define([ 'text!' + ctx + '/html/component/form-down-menu.html' ], function(
		template) {

	/**
	 * colWidth:(1~12)
	 * 
	 */
	var component = {
		props : [ 'value', 'name', 'label', 'tip', 'colWidth', 'selected',
				'values' ],
		template : template,
		data : function() {
			return {
				colWidthClass : '', // col-sm-12
				selectedObj : {}
			};
		},
		methods : {
			selectItem : function(value) {
				this.selectedObj = value;
				this.$emit('changeSelected', value)
			}
		},
		created : function() {
			this.selectedObj = this.selected;

		},
		mounted : function() {
			if (this.colWidth) {
				this.colWidthClass = 'col-sm-' + this.colWidth;
			}

		},
		watch : {
			value : function(val) {

				var defaultValue = {
					label : '选择' + this.label,
					key : 0
				};
				for (var i = 0; i < this.values.length; i++) {
					var unit = this.values[i];
					if (unit.key == val) {
						defaultValue = unit;
						break;
					}
				}

				this.selectedObj = defaultValue;
				this.$emit('changeSelected', defaultValue);
			},
			selected : function(val) {
				this.selectedObj = val;
			}
		}
	}

	return component;
})