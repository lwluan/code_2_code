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
            'controller': createComponent('/js/project/controller/controller.js'),
            'vo': createComponent('/js/project/vo.js'),
        },
        methods: {
        	
        	closeFile: function(index, file) {
        		this.tabMenus.splice(index, 1);
        	},
        	openFile: function(file) {
        		
        		var openIndex = 0;
        		var noHasNode = true;
        		for(var i=0; i<this.tabMenus.length; i++) {
        			if(file.id == this.tabMenus[i].id) {
        				noHasNode = false;
        				openIndex = i;
            			break;
        			}	
        		}
        		if(noHasNode) {
        			this.tabMenus.push(file);
        			openIndex = this.tabMenus.length - 1; 
        			
        		}
        		setTimeout(function(){
        			$('#myTabs li:eq('+openIndex+') a').tab('show')
        		}, 300);
        	}
           
        
        }, created: function() {
        	
        	
        }, mounted: function() {
        	
        }
    }

    return component;
})