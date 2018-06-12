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
        	
        	popSelfPanel: function(cbFun, seldVo) {
        		$(this.$el).show();
        		this.cbFun = cbFun;
        		var _data = {}
            	$.extend(true, _data, this.seldVoObj);
        		
        		// collectionType
        		if( seldVo && seldVo.collectionType ) {
        			$('input[value='+seldVo.collectionType+']').attr('checked', true);
        		}
        		
        		
        		for( var key in this.allVoListDic) {
        			var tmp = this.allVoListDic[key];
        			if( tmp.id == seldVo.id ) {
        				this.selectedObj = this.allVoListDic[key];
        			} 
        		}
                
        	},
        	
        	checkOk: function() {
        		$(this.$el).hide();
        		
        		// single list map set
        		var collectionType = $('input[name=collectionType]:checked').val();
        		this.selectedObj.collectionType = collectionType;
        		this.cbFun.call(this, this.selectedObj);
        		
        	},
        	
        	closePanel: function() {
        		$(this.$el).hide();
        	}
        	
        }, created: function() {

        }, mounted: function() {
        	
        }
    }

    return component;
})