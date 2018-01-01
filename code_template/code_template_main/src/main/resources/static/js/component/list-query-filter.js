define(['text!'+ctx+'/html/component/list-query-filter.html'], function( template ) {

    var data = {  };
    var component = {
        template: template,
        data: function(){ return data; },
        methods: {

        }, created: function() {
        	var filterPanel = $('.right-filter-panel');
        	var width = filterPanel.width() + 5;
        	var right = filterPanel.css('right').replace('px', '');
        	
        	if(right < 0) {
        		filterPanel.animate({'right': '0px'});
        	} else {
        		filterPanel.animate({'right': -width});
        	}
        }
    }

    return component;
})