define(['text!'+ctx+'/html/proDatabase/index.html', 
        'rcss!'+ctx+'/css/database', 
        'rcss!' + ctx + '/css/zTreeStyle/zTreeStyle',
		ctx + '/js/lib/jquery.ztree.all-3.5.min.js'], function( template ) {

	var data = {
		showPanel: 'db'
	};

    var component = {
        template: template,
        components: {
            'database': createComponent('/js/proDatabase/database.js'),
            'db-table': createComponent('/js/proDatabase/db-table.js')
        },
        data: function() { return data;  },
        methods: {
            initDbTree: function () {
            	var that = this;
                accessHttp({
                    url: buildUrl('/database/databaseTree'),
                    success: function (res) {
                    	
                    	var setting = {
                            data: { simpleData: { idKey:'id', pIdKey:'pid', enable: true } },
                            callback: {
                    			onClick: function(event, treeId, treeNode, clickFlag) {
                    				var type = treeNode.type;
                    				if( type == 1 ) {
                    					that.showDbDetail(treeNode.value);
                    				} else {
                    					that.showTableDetail({id:treeNode.value});
                    				}
                    			},
                    		}
                        };

                        var zNodes = res.data;
                        $.fn.zTree.init($("#databaseTree"), setting, zNodes);
                    }
                });
            }, showDbDetail: function(id) {
            	this.showPanel = 'db';
            	this.$refs.databasePanel.showDetail(id)
            }, showTableDetail: function(params) {
            	this.showPanel = 'tab';
            	this.$refs.dbTablePanel.showDetail(params);
            }
        }, mounted: function(){
        	this.initDbTree();
        }
    }
    return component;
});