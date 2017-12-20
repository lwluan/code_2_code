define(['text!' + ctx + '/html/project/left-slider.html', 
        'rcss!' + ctx + '/css/zTreeStyle/zTreeStyle', 
//        ctx+'/js/lib/jquery.ztree.all-3.5.min.js',
//        ctx+'/js/lib/jquery.cleverTabs.js',
//        ctx+'/js/lib/jquery.contextMenu.js'
        ], function( left_resource ){

    var data = { };
    var component = {
        template: left_resource,
        data: function(){ return data; },
        methods: {
        	
        }, created: function() {
        	// zTree = $.fn.zTree.init($("#treeDemo"), setting, zNodes);
        }
    }

    return component;
});