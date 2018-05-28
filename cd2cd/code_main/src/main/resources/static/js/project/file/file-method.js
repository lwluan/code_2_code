/** 底部工具栏 */
define(['text!'+ctx+'/html/project/file/file-method.html'], function( template ) {

    var data = { 
    	formData: {}
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
        		});
        		
        	},
        	
        	removeFun: function() {
        		console.info('removeFun');
        		this.$emit('remove-fun', this.formData);
        	},
        	
        	setFuncReturnType: function() {
        		console.info('setFuncReturnType');
        		this.$emit('set-func-return-type');
        	}
        	
        }, created: function() {
        	
        }, mounted: function() {
        	this.formData = $.extend(true, {}, this.funObj);
        }
    }

    return component;
})