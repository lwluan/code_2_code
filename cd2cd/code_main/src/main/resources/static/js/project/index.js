require.config({
    urlArgs: "r=" + (new Date()).getTime(),
    baseUrl: ctx,
    paths: {
        'text': 'js/lib/text',
        'rcss': 'js/lib/css.min'
    }
});

const routes = [  ];

const router = new VueRouter({
    routes: routes
});

const mainVm = new Vue({
    router: router,
    data: {},
    components: {
        'top-toolbar': createComponent('/js/project/top-toolbar.js'),
        'left-slider': createComponent('/js/project/left-slider.js'),
    }
}).$mount('#app')
