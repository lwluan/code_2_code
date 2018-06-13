/** 底部工具栏 */
define(['text!'+ctx+'/html/project/controller/controller-fun.html'], function( template ) {

    var data = { 
    	formData: {},
    	methods: HTTP_METHODS
    };
    
    var component = {
        template: template,
        props : [ 'fileObj', 'funObj' ],
        data: function(){ 
        	var _data = {}
        	$.extend(true, _data, data);
            return _data;
        },
        methods: {
        	
        	/**
        	 * update or save fun to file 
        	 */
        	updateFun: function() {
        		
        		var that = this;
        		// save or update 
        		RestData.updateOrSaveFunction(this.formData, function(res){
        			that.formData = res.data;
        			
        			that.$emit('update-completed', res.data);
        			
        			var fileComment = $(that.$el).find('.fileComment')[0];
        			setTimeout(function(){ setTextareaStyle(fileComment); }, 800);
        		});
        		
        	},
        	
        	removeFun: function() {
        		console.info('removeFun');
        		this.$emit('remove-fun', this.formData);
        	},
        	
        	setFuncReturnType: function() {
        		console.info('setFuncReturnType');
        		var that = this;
        		this.$emit('set-func-return-type', function(f) {
        			
        			var fd = $.extend(true, {}, that.formData);
            		
            		fd.resType = f.resType;
            		fd.returnShow = f.returnShow;
            		if( f.resVoId ) {
            			fd.resVoId = f.resVoId;
            			fd.returnVo = f.returnVo;
            		}
            		
            		if( f.resPageId ) {
            			fd.resPageId = f.resPageId;
            		}
            		
            		that.formData = fd;
        		});
        	}
        	
        }, created: function() {
        	
        }, mounted: function() {
        	var fileComment = $(this.$el).find('.fileComment')[0];
        	makeExpandingArea(fileComment);
        	this.formData = $.extend(true, {}, this.funObj);
        	
        	setTimeout(function(){ setTextareaStyle(fileComment); }, 800);
        	
        }
    }

    return component;
})