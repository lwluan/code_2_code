/** 底部工具栏 */
define(['text!'+ctx+'/html/project/file/file-editor.html'], function( template ) {

    var data = { 
    	
    };
    
    var component = {
        template: template,
        components: {
            'file-method': createComponent('/js/project/file/file-method.js'),
            'return-type': createComponent('/js/project/file/return-type.js'),
        },
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