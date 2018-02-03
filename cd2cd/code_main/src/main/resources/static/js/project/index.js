require.config({
    urlArgs: "r=" + (new Date()).getTime(),
    baseUrl: ctx,
    paths: {
        'text': 'js/lib/text',
        'rcss': 'js/lib/css.min'
    }
});


const mainVm = new Vue({
	el: '#app',
    data: {},
    components: {
        'top-toolbar': createComponent('/js/project/top-toolbar.js'),
        'left-slider': createComponent('/js/project/left-slider.js'),
    },
    methods: {
    	test: function(a, b) {
    		console.info(a + '|||' + b);
    	}
    }
});
