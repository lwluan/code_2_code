/** 底部工具栏 */
define(['text!'+ctx+'/html/project/project-module-list.html'], function( template ) {

	var data = {
			tablePage:{totalCount: 0, currPage: 0, notificationChange: 0, reloadNotification: 0},
	        queryData: {currPage: 1, pageSize: 5, keyword: null},
	        moduleList: [],
	};
    
    var component = {
        template: template,
        data: function(){ 
        	var _data = {}
        	$.extend(true, _data, data);
            return _data;
        },
        methods: {
        	showModuleList: function() {
        		$(this.$el).modal('show');
        	},
        	showModuleInfo: function(moduleId) {
        		this.$emit('show_module_info', moduleId);
        	},
        	queryPageData: function (currPage, pageSize) {

                this.queryData.pageSize = pageSize;
                this.queryData.currPage = currPage;
                var queryStr = $.param(this.queryData);

                var that = this;
                accessHttp({
                    url: buildUrl('/proProject/moduleList?' + queryStr),
                    success: function (res) {
                        that.moduleList = res.data.rows;
                        that.tablePage.totalCount = res.data.totalCount;
                        that.tablePage.notificationChange = new Date().getTime();
                    }
                });
            }, delRowData: function(id) {
                var that = this;
                popModal('确认提示', '确定是否删除？', null, function() {
                    $('#pop_box').modal('hide');
                    accessHttp({
                        url: buildUrl('/proProject/delModule/' + id),
                        success: function (res) {
                            that.queryPageData(data.queryData.currPage, data.queryData.pageSize);
                        }
                    });
                });
            }, reloadPage: function () {
                this.tablePage.reloadNotification = new Date().getTime();
            }
        	
        }, created: function() {
        	
        }, mounted: function() {
        	
        }
    }

    return component;
})