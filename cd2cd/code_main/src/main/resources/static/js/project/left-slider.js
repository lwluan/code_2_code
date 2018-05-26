define(['text!' + ctx + '/html/project/left-slider.html', 
        'rcss!' + ctx + '/css/zTreeStyle/zTreeStyle', 
        ctx + '/js/lib/jquery.ztree.all-3.5.min.js'], function( left_resource ){

	var setting = {
		data : {
			simpleData : {
				enable : true
			}
		},
		callback : {
			onClick : onClick,
			onRightClick : zTreeOnRightClick // 右键事件
		}
	};

	function onClick(event, treeId, treeNode, clickFlag) {
		if(treeNode.fileType != 'package' ) {
			leftSliderVm.$emit('open_file', treeNode);
		}
	}

	function zTreeOnRightClick(event, treeId, treeNode) {  
		leftSliderVm.selectTreeNode = treeNode;
		
		/**
		 * package 	右击时: 显示【添加】，隐藏【删除】
		 * file 	右击时: 显示【添加】，隐藏【添加】
		 * page 	右击时: 显示【添加】，隐藏【添加】
		 * 
		 **/
        if (treeNode) {    
        	var fileType = treeNode.fileType;
        	console.info('fileType=' + fileType);
        	if( 'package' == fileType ) {
	            $('#r-menu-remove-btn').hide();
	            $('#r-menu-add-btn').show();
        	} else {
        		$('#r-menu-remove-btn').show();
	            $('#r-menu-add-btn').hide();
        	}
        	zTree.selectNode(treeNode);  
            showRMenu("node", event.clientX, event.clientY);
        }  
    }  
	
	//显示右键菜单  
    function showRMenu(type, x, y) {  
        $("#rMenu").show();  
        if (type=="root") {  
            $("#m_del").hide();  
            $("#m_check").hide();  
            $("#m_unCheck").hide();  
        }  
        $("#rMenu").css({"top":(y - 40)+"px", "left":x+"px", "display":"block"});  
    }  
	
	var zTree;
	
	// 鼠标点击事件不在节点上时隐藏右键菜单
	$("body").bind("mousedown", function(event) {  
        if ( ! (event.target.id == "rMenu" || $(event.target).parents("#rMenu").length > 0)) {
            $("#rMenu").hide();
        }  
    });  
	
	var leftSliderVm;
	
    var data = { packageType:'', moduleId: '' };
    var component = {
        template: left_resource,
        data: function(){ return data; },
        methods: {
        	reloadProjectFileTree: function() {
        		
        		this.fetchProjectFileTree(this.packageType, this.moduleId);
        		
        	},
        	fetchProjectFileTree: function(packageType, moduleId) {
        		
        		var url = '/project/fetchProjectFileTree?projectId='+projectId+'&packageType=' + packageType;
        		if(moduleId ) {
        			url += "&moduleId=" + moduleId 
        		}
        		console.info('fetchProjectFileTree packageType=' + packageType + ',moduleId=' + moduleId);
        		accessHttp({
                    url: buildUrl(url),
                    success: function (res) {
                    	var zNodes = eval('(' + res.data + ')');
                    	
                    	/*
                    	icon : ctx + "/images/package_obj.gif"
                		icon : ctx + "/images/source-folder.gif"
                		icon : ctx + "/images/jcu_obj.gif"
                		icon : ctx + "/images/xmldoc.gif"
                		icon : ctx + "/images/sourceEditor.gif"
                		icon : ctx + "/images/fldr_obj.gif"
                		controller|service:vo|dao|domain|package
                		*/
                    	// fetch all nodes 
                    	var treeObj = zTree;
                		var openStats = {};
                    	if( treeObj ) {
                        	var node = treeObj.getNodes();
                        	var nodes = treeObj.transformToArray(node);
                        	
                        	for( var i in nodes ) {
                        		var nn = nodes[i];
                        		if( nn['open'] ) {
                        			var indentify = nn['indentify'];
                        			openStats[indentify] = true;
                        		}
                        	}
                    	}
                    	
                    	
                    	for( var i=0; i<zNodes.length; i++ ) {
                    		
                    		var node = zNodes[i];
                    		var type = node.fileType;
                    		if( type == 'package' ) {
                    			node['icon'] = ctx + "/images/package_obj.gif";
                    		} else if( type == 'folder' ) {
                    			node['icon'] = ctx + "/images/source-folder.gif";
                    		} else {
                    			node['icon'] = ctx + "/images/jcu_obj.gif";
                    		}
                    		
                    		// open status
                    		if( openStats[node.indentify] ) {
                    			node['open'] = true;
                    		}
                    		
                    		zNodes[i] = node;
                    	}
                    	
                    	zTree = $.fn.zTree.init($("#treeDemo"), setting, zNodes);
                    }
                });
        	}
        	
        }, created: function() {
        	
        	leftSliderVm = this;
        	
        }, mounted: function() {
        	
        }
    }

    return component;
});