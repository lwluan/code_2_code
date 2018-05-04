define(['text!'+ctx+'/html/component/dropdown-menu.html'], function( template ) {

    /**
     * 事件
     * change_page
     */
    var data = { 
    	selectedObj: {}
    };
    
    var component = {
        props:['values', 'selected', 'buttonName'],
        template: template,
        data: function(){
        	var _data = {}
        	$.extend(true, _data, data);
            return _data;
        },
        methods: {
            selectItem: function(obj) {
            	this.selectedObj = obj;
            	this.$emit('changeSelected', obj);
            }
        }, created: function() {
            this.selectedObj = this.selected;
        }, mounted: function() {
        	this.$emit('changeSelected', this.selected);
        }, watch : {
        	selected : function(val) {
        		this.selectedObj = val;
			}
		}
    }

    return component;
})