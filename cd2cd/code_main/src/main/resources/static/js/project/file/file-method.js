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
        	
        	setFuncReturnType: function() {
        		console.info('setFuncReturnType');
        		this.$emit('set-func-return-type', 111);
        	}
        	
        }, created: function() {
        	
        }, mounted: function() {
        	
        }
    }

    return component;
})