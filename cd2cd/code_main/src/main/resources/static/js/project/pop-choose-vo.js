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
        		this.cbFun = cbFun;
        		var _data = {}
            	$.extend(true, _data, this.seldVoObj);
                this.selectedObj = _data;
        	},
        	
        	checkOk: function() {
        		$(this.$el).hide();
        		
        		// single list map set
        		var collectionType = $('input[name=collectionType]:checked').val();
        		this.selectedObj.collectionType = collectionType;
        		this.cbFun.call(this, this.selectedObj);
        		
        	}
        	
        }, created: function() {

        }, mounted: function() {
        	
        }
    }

    return component;
})