/** 底部工具栏 */
define(['text!'+ctx+'/html/project/top-toolbar.html'], function( template ) {

    var data = {
		packageTypeDropdown: { 
        	values: [{label: 'Flat', key: 'Flat'}, {label: 'Hierarchical', key: 'Hierarchical'}],
        	selected: {label: 'Hierarchical', key: 'Hierarchical'},
        },
        modulTypeDropdown: { 
        	values: [{label: '全部模块', key: '0'}],
        	selected: {label: '全部模块', key: '0'},
        },
        actionTime: 0
    };
    
    var component = {
        template: template,
        data: function(){ 
        	var _data = {}
        	$.extend(true, _data, data);
            return _data;
        },
        methods: {
        	
        	modulTypeDropdownChange: function(obj) {
        		
        		this.packageTypeDropdown.selected = obj;
        		var modulObj = this.modulTypeDropdown.selected;
        		this.toChangeFileFree(obj.key, modulObj.key);
        		
        	},
        	
        	packageTypeDropdownChange: function(obj) {
        		
        		this.modulTypeDropdown.selected = obj;
        		var packageObj = this.packageTypeDropdown.selected;
        		this.toChangeFileFree(packageObj.key, obj.key);
        		
        	},
        	toChangeFileFree: function(packageType, modulId) {
        		
        		// 时间控制，500毫秒内只让调用一次
        		var nowTime = new Date().getTime();
        		if( nowTime - this.actionTime > 500 ) {
        			this.$emit('change_file_free', packageType, modulId);
        			this.actionTime = nowTime;
        		}
        	}
        	
        }, created: function() {

        }, mounted: function() {
        	
        }
    }

    return component;
})