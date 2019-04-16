require.config({
    urlArgs: "r=" + (new Date()).getTime(),
    baseUrl: ctx,
    paths: {
        'text': 'js/lib/text',
        'rcss': 'js/lib/css.min'
    }
});


const routes = [
    { path: '/sysUserList', component: createComponent('/js/sysUser/list.js') },
    { path: '/sysRoleList', component: createComponent('/js/sysRole/list.js') },
    { path: '/proProjectList', component: createComponent('/js/proProject/list.js')},
    { path: '/proDatabaseIndex', component: createComponent('/js/proDatabase/index.js')}
    
]

const router = new VueRouter({
    routes: routes
})


const mainVm = new Vue({
    router: router,
    data: {},
    components: {
        'app-header': createComponent('/js/component/app-header.js'),
        'app-slider-left': createComponent('/js/component/app-slider-left.js'),
    }
}).$mount('#app')

