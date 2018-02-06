require.config({
    urlArgs: "r=" + (new Date()).getTime(),
    baseUrl: ctx,
    paths: {
        'text': 'js/lib/text',
        'rcss': 'js/lib/css.min'
    }
});

var projectId = $.getUrlParam('projectId');
console.info('projectId=' + projectId);

const mainVm = new Vue({
	el: '#app',
    data: {  },
    components: {
        'top-toolbar': createComponent('/js/project/top-toolbar.js'),
        'left-slider': createComponent('/js/project/left-slider.js'),
        'project-info': createComponent('/js/project/project-info.js'),
        'project-module': createComponent('/js/project/project-module.js'),
        'project-module-list': createComponent('/js/project/project-module-list.js'),
    },
    methods: {
    	test: function(a, b) {
    		console.info(a + '|||' + b);
    	}
    }
});
