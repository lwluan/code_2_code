Vue.component('nav-route', createComponent('/js/component/nav-route.js'));
Vue.component('confirm-box', createComponent('/js/component/confirm-box.js'));
Vue.component('table-page', createComponent('/js/component/table-page.js'));
Vue.component('group-button', createComponent('/js/component/group-button.js'));

// filter
Vue.filter("date", function(input) {
	return dateFmt(input);
});