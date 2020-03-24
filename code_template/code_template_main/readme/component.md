# 表单元素
## nav-route 路径地址
## confirm-box 确认提示框
## table-page 表格分页
## form-text-input 表单-输入文本
## form-textarea-input 表单-大文本输入
## form-datetime-input 表单-时间输入选项
## list-column-option 
	.no-show 初始不显示
	.ignore  不进行控制
	
## 	dropdown-menu
	<dropdown-menu
		v-bind:buttonName="'packageType'"
	   	v-bind:values="packageTypeDropdown.values" 
	   	v-bind:selected="packageTypeDropdown.selected" 
	   	v-on:changeSelected="packageTypeDropdownChange" />
	   	
	   	modulTypeDropdown: { 
        	values: [{label: '全部模块', key: '0'}],
        	selected: {label: '全部模块', key: '0'},
        },
        
##         
