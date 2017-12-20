define(['text!' + ctx + '/html/project/left-slider.html', 
        'rcss!' + ctx + '/css/zTreeStyle/zTreeStyle', 
        ctx + '/js/lib/jquery.ztree.all-3.5.min.js',
        ctx + '/js/lib/jquery.cleverTabs.js',
        ctx + '/js/lib/jquery.contextMenu.js'], function( left_resource ){

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
		if (treeNode.name.indexOf(".java") != -1
				|| treeNode.name.indexOf(".jsp") != -1
				|| treeNode.name.indexOf(".xml") != -1
				|| treeNode.name.indexOf(".sql") != -1) {
			var param = "?filecode=1607121356384496&packageName=com.test.aa&className=Action&folder=action&fileName="
					+ treeNode.name;
			
			console.info(JSON.stringify(treeNode));
			
			var fileType = treeNode.fileType;
			
			tabs.add({
				url : '/backend/classfile/showfile?filetype='+ fileType,
				label : treeNode.name
			});
			// window.self.location = "/front/code_searchFileContent.action" + param;
		}
	}

	function zTreeOnRightClick(event, treeId, treeNode) {  
        if (!treeNode) {  
            zTree.cancelSelectedNode();  
            showRMenu("root", event.clientX, event.clientY);  
        } else if (treeNode && !treeNode.noR) { //noR属性为true表示禁止右键菜单  
            if (treeNode.newrole && event.target.tagName != "a" && $(event.target).parents("a").length == 0) {  
                zTree.cancelSelectedNode();  
                showRMenu("root", event.clientX, event.clientY);  
            } else {  
                zTree.selectNode(treeNode);  
                showRMenu("node", event.clientX, event.clientY);  
            }  
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
        $("#rMenu").css({"top":y+"px", "left":x+"px", "display":"block"});  
    }  
	
	// 树添加结点
	function addNodeType(nodeType) {
		$('#rMenu').hide();
		tabs.add({
			url : '/backend/classFile/showFile?filetype='+nodeType,
			label : '新建【' + nodeType + '】'
		});
	}
	
	var tabs;
	var zTree;
	
	$("body").bind( // 鼠标点击事件不在节点上时隐藏右键菜单  
    "mousedown",  
    function(event) {  
        if ( ! (event.target.id == "rMenu" || $(event.target).parents("#rMenu").length > 0)) {
        	
            $("#rMenu").hide();
            
        }  
    });  
	
	tabs = $('#tabs').cleverTabs();
	
	/*
	tabs.add({
		url : 'http://localhost:9001/project/index?2',
		label : 'Dao.java'
	});
	*/
	
    var data = { };
    var component = {
        template: left_resource,
        data: function(){ return data; },
        methods: {
        	
        }, created: function() {
        	
        }, mounted: function() {
        	zTree = $.fn.zTree.init($("#treeDemo"), setting, zNodes);
        }
    }

    return component;
});