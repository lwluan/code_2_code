/** 底部工具栏 */
define(['text!'+ctx+'/html/project/file-editor-tab.html'], function( template ) {

    var data = { 
    	tabMenus: [ 
    	           /* test data
    	            {"dType":"class","name":"CustomController.java","pId":4,"id":10,"fileType":"controller"},
    	            {"dType":"class","name":"UserController.java","pId":4,"id":11,"fileType":"controller"},
    	            {"dType":"class","name":"SystemController.java","pId":4,"id":12,"fileType":"controller"}
    	            */
    	]
    };
    
    var component = {
        template: template,
        data: function(){ 
        	var _data = {}
        	$.extend(true, _data, data);
            return _data;
        },
        components: {
            'file-editor': createComponent('/js/project/file/file-editor.js'),
            'vo-file': createComponent('/js/project/file/vo-file.js'),
        },
        methods: {
        	
        	closeFile: function(index, file) {
        		this.tabMenus.splice(index, 1);
        	},
        	openFile: function(file) {
        		
        		var noHasNode = true;
        		for(var i=0; i<this.tabMenus.length; i++) {
        			if(file.id == this.tabMenus[i].id) {
        				noHasNode = false;
            			break;
        			}	
        		}
        		if(noHasNode) {
        			this.tabMenus.push(file);
        		}
        	}
           
        
        }, created: function() {
        	
        	
        }, mounted: function() {
        	
        }
    }

    return component;
})