/** 底部工具栏 */
define(['text!'+ctx+'/html/project/project-module.html'], function( template ) {

    var data = { };
    
    var component = {
        template: template,
        data: function(){ 
        	var _data = {}
        	$.extend(true, _data, data);
            return _data;
        },
        methods: {
        	showProjectModule: function() {
        		$(this.$el).modal('show');
        	}
        
        }, created: function() {
        	window.aaaa = this;
        }, mounted: function() {
        	
        }
    }

    return component;
})