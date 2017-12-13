define(['text!'+ctx+'/html/component/app-header.html'], function( template ) {

    var data = { };
    var component = {
        template: template,
        data: function(){ return data; },
        methods: {

        }, created: function() {

        }
    }

    return component;
})