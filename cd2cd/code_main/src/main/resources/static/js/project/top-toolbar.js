/** 底部工具栏 */
define(['text!'+ctx+'/html/project/top-toolbar.html'], function( template ) {

    var data = { };
    var component = {
        template: template,
        data: function(){ return data; },
        methods: {
        	
        	
        	
        }, created: function() {

        }
    }

    return component;
})