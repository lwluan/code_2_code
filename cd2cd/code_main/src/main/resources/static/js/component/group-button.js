define(['text!'+ctx+'/html/component/group-button.html'], function( template ) {

    /**
     * 事件
     * change_page
     */

    var data = { 
    	selectedKey: ''
    };
    
    var component = {
        props:['values', 'selected', 'buttonName'],
        template: template,
        data: function(){
            return data;
        },
        methods: {
            selectItem: function(val) {
            	this.selectedKey = val;
            	this.$emit('changeSelected', val);
            }
        }, created: function() {
            this.selectedKey = this.selected;
        }, mounted: function() {
        	
        }, watch: {
        	selected: function(val) {
        		this.selectedKey = this.selected;
        		this.$emit('changeSelected', this.selected);
            }
        }
    }

    return component;
})