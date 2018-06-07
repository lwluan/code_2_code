/** 底部工具栏 */
define(['text!'+ctx+'/html/project/pop-choose-vo.html'], function( template ) {

	
    var data = {
    		selectedObj: {},
    		cbFun: ''
    };
    
    var component = {
        template: template,
        props: ['seldVoObj', 'allVoListDic'],
        data: function(){ 
        	var _data = {}
        	$.extend(true, _data, data);
            return _data;
        },
        methods: {
        	
        	popSelfPanel: function(cbFun) {
        		$(this.$el).show();
        		console.info(' - - - ');
        		this.cbFun = cbFun;
        		
        		var _data = {}
            	$.extend(true, _data, this.seldVoObj);
                this.selectedObj = _data;
        	},
        	
        	checkOk: function() {
        		$(this.$el).hide();
        		
        		console.info(JSON.stringify(this.selectedObj));
        		
        		if( this.cbFun ) {
        			this.cbFun.call(this, 'name---');
        		}
        	}
        	
        }, created: function() {

        }, mounted: function() {
        	
        }
    }

    return component;
})