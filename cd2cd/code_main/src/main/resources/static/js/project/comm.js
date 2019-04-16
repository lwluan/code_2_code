var BASE_TYPES = [ {
	key : 'String',
	label : 'String'
}, {
	key : 'Integer',
	label : 'Integer'
}, {
	key : 'Double',
	label : 'Double'
}, {
	key : 'Float',
	label : 'Float'
}, {
	key : 'Long',
	label : 'Long'
}, ];

var HTTP_METHODS = [ 'GET', 'POST', 'PUT', 'DELETE', 'HEAD' ]

/** T 类型vo选择范型对象 */
Vue.component('T-vo-choose', createComponent('/js/project/T-vo-choose.js'));
Vue.component('pop-choose-vo', createComponent('/js/project/pop-choose-vo.js'));