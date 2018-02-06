Vue.component('nav-route', createComponent('/js/component/nav-route.js'));
Vue.component('confirm-box', createComponent('/js/component/confirm-box.js'));
Vue.component('table-page', createComponent('/js/component/table-page.js'));
Vue.component('form-text-input', createComponent('/js/component/form-text-input.js'));
Vue.component('form-textarea-input', createComponent('/js/component/form-textarea-input.js'));
Vue.component('form-datetime-input', createComponent('/js/component/form-datetime-input.js'));
Vue.component('form-group-btns', createComponent('/js/component/form-group-btns.js'));
Vue.component('form-checkbox', createComponent('/js/component/form-checkbox.js'));
Vue.component('group-button', createComponent('/js/component/group-button.js'));
Vue.component('list-query-filter', createComponent('/js/component/list-query-filter.js'));
Vue.component('list-column-option', createComponent('/js/component/list-column-option.js'));
Vue.component('dropdown-menu', createComponent('/js/component/dropdown-menu.js'));
Vue.component('form-down-menu', createComponent('/js/component/form-down-menu.js'));



//filter
Vue.filter("date", function(input) {
	return dateFmt(input);
});
