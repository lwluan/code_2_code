/** 底部工具栏 */
define(['text!'+ctx+'/html/project/top-toolbar.html'], function( template ) {

    var data = {
		packageTypeDropdown: { 
        	values: [{label: 'Flat', key: 'Flat'}, {label: 'Hierarchical', key: 'Hierarchical'}],
        	selected: {label: 'Hierarchical', key: 'Hierarchical'},
        },
        moduleTypeDropdown: { 
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
        	projectModuleList: function() {
        		var that = this;
        		accessHttp({
                    url: buildUrl('/proProject/projectModuleList?projectId=' +projectId),
                    success: function (res) {
                    	var values = [{label: '全部模块', key: '0'}];
                    	var moduleeList = res.data;
                    	for(var i=0; i<moduleeList.length; i++ ) {
                    		var modulee = moduleeList[i];
                    		values.push({key: modulee.id, label: modulee.name+'('+modulee.showName+')'});
                    	}
                    	that.moduleTypeDropdown.values = values;
                    }
                });
        	},
        	moduleTypeDropdownChange: function(obj) {
        		this.moduleTypeDropdown.selected = obj;
        		var packageObj = this.packageTypeDropdown.selected;
        		this.toChangeFileFree(packageObj.key, obj.key);
        		
        	},
        	
        	packageTypeDropdownChange: function(obj) {
        		
        		this.packageTypeDropdown.selected = obj;
        		var moduleObj = this.moduleTypeDropdown.selected;
        		this.toChangeFileFree(obj.key, moduleObj.key);
        		
        	},
        	toChangeFileFree: function(packageType, moduleId) {
        		
        		// 时间控制，500毫秒内只让调用一次
        		var nowTime = new Date().getTime();
        		if( nowTime - this.actionTime > 500 ) {
        			this.$emit('change_file_free', packageType, moduleId);
        			this.actionTime = nowTime;
        		}
        	}
        	
        }, created: function() {

        }, mounted: function() {
        	this.projectModuleList();
        }
    }

    return component;
})