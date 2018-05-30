/** 底部工具栏 */
define(['text!'+ctx+'/html/project/file/file-editor.html'], function( template ) {

    var data = { 
    	formData: {},
    	funs: []
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
        	
        	
        	removeFunFromFile: function(f, index) {
        		
        		var that = this;
                popModal('确认提示', '确定是否删除？', null, function() {
                    $('#pop_box').modal('hide');
                    if( ! f.id ) {
                    	that.funs.splice(index, 1);
                    	return;
                    }
                    
                    RestData.deleteFunctionByFunId(f.id, function(res) {
                    	that.funs.splice(index, 1);
                    });
                    
                });
                
        	},
        	
        	/** add new fun to file */
        	addNewFunToFile: function() {
        		
        		var fileObj = this.fileObj;
				var hasEmpty = true;
				for(var i=0; i<this.funs.length; i++) {
					var tc = this.funs[i];
					if( ! tc.id ) {
						hasEmpty = false;
						break;
					}
				}
				if( hasEmpty ) {
					this.funs.push({ cid: this.fileObj.fileId });
				}
				
        	},
        	
        	loadFunsFromCtrl: function() {
        		var that = this;
        		var fileId = this.fileObj.fileId;
        		RestData.fetchFunsByFileId(fileId, function(res) {
        			that.funs = res.data;
        		});
        	},
        	
        	loadControllerFileInfo: function() {
        		
        		var that = this;
        		var fileId = this.fileObj.fileId;
        		RestData.fetchFileInfo(fileId, function(res){
        			
        			var data = res.data;
        			formData = {
        				id: data.id,
        				name: data.name,
        				comment: data.comment,
        				reqPath: data.reqPath
        			};
        			that.formData = formData;
        			
        			var fileComment = $(that.$el).find('.fileComment')[0];
					setTimeout(function(){ setTextareaStyle(fileComment) }, 800);
        		});
        		
        		// load funs
        		this.loadFunsFromCtrl();
        	},
        	
        	saveFileInfo: function() {
        		
        		RestData.saveFileInfo(this.formData);
        		
        	},
        	
        	updateFunReturnType: function(i, f) {
        		console.info('i=' + i + ' | ' + JSON.stringify(f));
        		Vue.set(this.funs, i, f);
        	}
        	
        	
        }, created: function() {
        	
        }, mounted: function() {
        	var fileComment = $(this.$el).find('.fileComment')[0];
			makeExpandingArea(fileComment);
			this.loadControllerFileInfo();
        }
    }

    return component;
})