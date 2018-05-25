define([ 'text!' + ctx + '/html/component/group-button.html' ], function(
		template) {

	/**
	 * 事件 change_page
	 */

	var component = {
		props : [ 'values', 'selected', 'buttonName' ],
		template : template,
		data : function() {
			return {
				selectedKey : ''
			};
		},
		methods : {
			selectItem : function(val) {
				this.selectedKey = val;
				this.$emit('changeSelected', val);
			}
		},
		created : function() {
			if( this.selected ) {
				this.selectedKey = this.selected;
			}
		},
		mounted : function() {

		},
		watch : {
			selected : function(val) {
				if( this.selected ) {
					this.selectedKey = this.selected;
				}
				this.$emit('changeSelected', this.selectedKey);
			}
		}
	}

	return component;
})