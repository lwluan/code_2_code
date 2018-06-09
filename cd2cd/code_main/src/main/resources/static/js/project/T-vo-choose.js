/** 底部工具栏 */
define(['text!'+ctx+'/html/project/T-vo-choose.html'], function( template ) {

	
    var data = {
    	seledVoId: 0,
    	newPopChooseVoPanel:{},
    	newSeldVoObj: {},
    };
    
    var component = {
        template: template,
        props: ['seldVoObj', 'popChooseVoPane', 'changeTVoValue'],
        data: function(){ 
        	var _data = {}
        	$.extend(true, _data, data);
            return _data;
        },
        methods: {
        	
        	toSelectVo: function() {
        		var that = this;
        		that.$emit('to-select-vo', function(vo){
        			
        			that.newSeldVoObj = vo;
        			
        			that.$emit('completed', that.newSeldVoObj);
        		});
        	},
        	
        	completed: function(newSeldVoObj) {
        		this.newSeldVoObj.next = newSeldVoObj;
        		this.$emit('completed', this.newSeldVoObj);
        	}
        	
        }, created: function() {

        }, mounted: function() {
            
        }, computed: {
        	checkChangeTVoValue: function() {
        		this.newPopChooseVoPanel = this.popChooseVoPane;
            	this.newSeldVoObj = this.seldVoObj;
        		return this.changeTVoValue;
        	}
        	
        }
    }

    return component;
})