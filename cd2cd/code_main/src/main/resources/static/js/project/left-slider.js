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

		var zNodes = [ {
			id : 1,
			pId : 0,
			name : "src",
			open : true,
			fileType : 'folder',
			icon : ctx + "/images/source-folder.gif"
		}, {
			id : 11,
			pId : 1,
			name : "com.test.aa.action",
			open : true,
			fileType : 'package',
			icon : ctx + "/images/package_obj.gif"
		}, {
			id : 110,
			pId : 11,
			name : "ActionAction.java",
			fileType : 'controller',
			icon : ctx + "/images/jcu_obj.gif"
		}, {
			id : 12,
			pId : 1,
			name : "com.test.aa.service",
			open : true,
			fileType : 'service',
			icon : ctx + "/images/package_obj.gif"
		}, {
			id : 120,
			pId : 12,
			name : "ActionService.java",
			fileType : 'service',
			icon : ctx + "/images/jcu_obj.gif"
		}, {
			id : 13,
			pId : 1,
			name : "com.test.aa.service.impl",
			open : true,
			fileType : 'service',
			icon : ctx + "/images/package_obj.gif"
		}, {
			id : 130,
			pId : 13,
			name : "ActionServiceImpl.java",
			fileType : 'service',
			icon : ctx + "/images/jcu_obj.gif"
		}, {
			id : 14,
			pId : 1,
			name : "com.test.aa.persistence",
			open : true,
			fileType : 'package',
			icon : ctx + "/images/package_obj.gif"
		}, {
			id : 140,
			pId : 14,
			name : "ActionMapper.java",
			icon : ctx + "/images/jcu_obj.gif"
		}, {
			id : 141,
			pId : 14,
			fileType : 'xml',
			name : "ActionMapper.xml",
			icon : ctx + "/images/xmldoc.gif"
		}, {
			id : 15,
			pId : 1,
			name : "com.test.aa.domain",
			open : true,
			fileType : 'package',
			icon : ctx + "/images/package_obj.gif"
		}, {
			id : 150,
			pId : 15,
			name : "Action.java",
			fileType : 'controller',
			icon : ctx + "/images/jcu_obj.gif"
		}, {
			id : 16,
			pId : 1,
			name : "app-bean.xml",
			fileType : 'xml',
			icon : ctx + "/images/xmldoc.gif"
		}, {
			id : 17,
			pId : 1,
			fileType : 'xml',
			name : "app-config.xml",
			icon : ctx + "/images/xmldoc.gif"
		}, {
			id : 18,
			pId : 1,
			fileType : 'xml',
			name : "struts.xml",
			icon : ctx + "/images/xmldoc.gif"
		}, {
			id : 2,
			pId : 0,
			name : "db",
			open : true,
			fileType : 'folder',
			icon : ctx + "/images/fldr_obj.gif"
		}, {
			id : 21,
			pId : 2,
			name : "Action.sql",
			fileType : 'source',
			icon : ctx + "/images/sourceEditor.gif"
		}, {
			id : 3,
			pId : 0,
			name : "WebContent",
			fileType : 'folder',
			open : true,
			icon : ctx + "/images/fldr_obj.gif"
		}, {
			id : 31,
			pId : 3,
			name : "action",
			open : true,
			fileType : 'folder',
			icon : ctx + "/images/fldr_obj.gif"
		}, {
			id : 301,
			pId : 31,
			name : "index.jsp",
			fileType : 'page',
			icon : ctx + "/images/sourceEditor.gif"
		}, {
			id : 302,
			pId : 31,
			name : "add.jsp",
			fileType : 'page',
			icon : ctx + "/images/sourceEditor.gif"
		}, {
			id : 303,
			pId : 31,
			name : "update.jsp",
			fileType : 'page',
			icon : ctx + "/images/sourceEditor.gif"
		}, {
			id : 304,
			pId : 31,
			name : "result.jsp",
			fileType : 'page',
			icon : ctx + "/images/sourceEditor.gif"
		} ];
		

	function onClick(event, treeId, treeNode, clickFlag) {
		console.info(JSON.stringify(treeNode));
		var fileType = treeNode.fileType;
	}

	function zTreeOnRightClick(event, treeId, treeNode) {  
		
		console.info('treeId=' + treeId + ',treeNode=' + JSON.stringify(treeNode));
		
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
	
	
    var data = { };
    var component = {
        template: left_resource,
        data: function(){ return data; },
        methods: {
        	
        	fetchProjectFileTree: function(packageType, modulId) {
        		
        		console.info('fetchProjectFileTree packageType=' + packageType + ',modulId=' + modulId);
        		
        	}
        	
        }, created: function() {
        	
        }, mounted: function() {
        	zTree = $.fn.zTree.init($("#treeDemo"), setting, zNodes);
        }
    }

    return component;
});