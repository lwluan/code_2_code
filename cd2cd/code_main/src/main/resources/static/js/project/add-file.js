/** 底部工具栏 */
define(['text!'+ctx+'/html/project/add-file.html'], function( template ) {

	var initFormValidate = function() {
        /** 表单验证 **/
        var formValidate = $('#formAddFileValidate').validate({
            rules:{
                name:{ required:true },
                fileType:{ required:true },
            },
            messages:{
                name:{ required: "文件名不可为空" },
                fileType:{ required: "文件不可为空" },
            }
        });

        return formValidate;
    }
	
    var data = {
    	formData: { paradigm: 'no', classType: 'class', superId: 0 },
    	formValidate: {},
		fileTypeDropdown: { 
        	values: [{label: 'controller', key: 'controller'}, 
        	         {label: 'service', key: 'service'},
        	         {label: 'doamin', key: 'doamin'},
        	         {label: 'vo', key: 'vo'},
        	         {label: 'page', key: 'page'},
        	         {label: 'dao', key: 'dao'},
        	],
        	selected: {label: 'controller', key: 'controller'},
        },
        tableListDrodown: { 
        	values: [  ],
        	selected: {label: '无继承', key: 0},
        },
        classTypeListDrodown: { 
        	// class|generics|enum|interface|abstruct
        	values: [ {label: 'enum', key: 'enum' },
        	          {label: 'class', key: 'class' },],
        	selected: {label: 'class', key: 'class' },
        }, 
        moduleListDrodown: {
        	values: [ {label: '-公共模块-', key: 0 } ],
        	selected: {label: '-公共模块-', key: 0 },
        }
        
    };
    
    var component = {
        template: template,
        data: function(){ 
        	var _data = {}
        	$.extend(true, _data, data);
            return _data;
        },
        methods: {
        	
        	changeFileType: function(fileType) {
        		
        		var that = this;
        		if( 'vo' == fileType ) {
        			
        			accessHttp({
                        url: buildUrl('/project/fetchTableListByProjectHasDb?projectId=' + projectId),
                        success: function (res) {
                            var tabs = res.data;
                            
                            var tabList = [{label: '无继承', key: 0}];
                            if( tabs ) {
                            	for( var i=0; i<tabs.length; i++ ) {
                            		var tb = tabs[i];
                            		tabList.push({ label: tb.name, key: tb.id });
                            	}
                            }
                            that.tableListDrodown.values = tabList;
                            
                        }
                    });
        		} else {
        			that.tableListDrodown.values = [];
        			that.tableListDrodown.selected = {label: '无继承', key: 0};
        		}
        	},
        
        	popShowPanel : function(node) {
        		
        		this.formValidate.resetForm();
        		var that = this;
        		
        		var fileType = node.name;
        		var fileTypeSeld = null;
        		for( var i=0; i<this.fileTypeDropdown.values.length; i++  ) {
        			
        			var fTypeObj = this.fileTypeDropdown.values[i];
        			if( fileType == fTypeObj.key ) {
        				fileTypeSeld = fTypeObj;
        				break;
        			}
        		}
        		
        		if( fileTypeSeld ) {
        			this.fileTypeDropdown.selected = fileTypeSeld;
        			this.changeFileType(fileTypeSeld.key);
        			this.formData.fileType = fileTypeSeld.key;
        		}
        		
        		// 获取所有模块
                accessHttp({
                    url: buildUrl('/proProject/moduleList?pageSize=100&currPage=1'),
                    success: function (res) {
                        var rows = res.data.rows;
                        
                        var values = [{label: '-公共模块-', key: 0 }];
                        for( var i=0; i<rows.length; i++ ) {
                        	var mo = rows[i];
                        	values.push({ label: mo.name+'('+mo.showName+')', key: mo.id });
                        }
                        that.moduleListDrodown.values = values;
                    }
                });
                
				$(this.$el).modal('show');
			},
			
			subToAddFile: function() {
				var that = this;
				var valid = $('#formAddFileValidate').valid();
				if( valid ) {
					var postData = this.formData;
                    postData.projectId = projectId;
					accessHttp({
                        url: buildUrl('/project/addFile'),
                        contentType: 'application/json; charset=utf-8',
                        data: JSON.stringify(postData),
                        type: 'post',
                        success: function (res) {
                            $(that.$el).modal('hide');
                            that.$emit('completed');
                        }
                    });
				}
			}
			
        }, created: function() {

        }, mounted: function() {
            this.formValidate = initFormValidate();
        }
    }

    return component;
})