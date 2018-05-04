/** 底部工具栏 */
define(['text!'+ctx+'/html/project/file/file-method.html'], function( template ) {

    var data = { 
    	
    };
    
    var component = {
        template: template,
        props : [ 'fileObj' ],
        data: function(){ 
        	var _data = {}
        	$.extend(true, _data, data);
            return _data;
        },
        methods: {
        
        }, created: function() {
        	
        }, mounted: function() {
        	
        }
    }

    return component;
})