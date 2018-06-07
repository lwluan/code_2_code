/** 底部工具栏 */
define(['text!'+ctx+'/html/project/T-vo-choose.html'], function( template ) {

	
    var data = {
    	seledVoId: 0
    };
    
    var component = {
        template: template,
        props: ['seldVoObj', 'allVoListDic', 'popChooseVoPane'],
        data: function(){ 
        	var _data = {}
        	$.extend(true, _data, data);
            return _data;
        },
        methods: {
        	
        	toSelectVo: function() {
        		this.$emit('to-select-vo', function(){
        			
        		});
        	}
        	
        }, created: function() {

        }, mounted: function() {
            
        	
        }
    }

    return component;
})