define(['text!'+ctx+'/html/component/app-header.html'], function( template ) {

    var data = { };
    var component = {
        template: template,
        data: function(){ return data; },
        methods: {
        	logout: function() {
        		$('#logout-form').attr('action', ctx + '/logout');
        		$('#logout-form')[0].submit();
        	}
        }, created: function() {

        }
    }

    return component;
})