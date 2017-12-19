define(['text!'+ctx+'/html/sysUser/info.html'], function( template ) {

    var initFormValidate = function() {
        /** 表单验证 **/
        var formValidate = $('#formValidate').validate({
            rules:{
                nickname:{ required:true },
                username:{ required:true },
                password:{ required:true, rangelength:[6,16] },
                confirm_password:{ required:true, equalTo:"#password" },
                email:{ required:true, email: true },
                mobile:{ required:true, mobile: true },
                roleIds: {required:true },
            },
            messages:{
                username:{ required: "用户名不可为空" },
                password:{ required: "密码不可为空",
                    rangelength: $.validator.format("密码最小长度:{0}, 最大长度:{1}。")
                },
                confirm_password:{ required: "密码不可为空", equalTo:"两次密码输入不一致" },
                nickname:{ required: "昵称不可为空" },
                email:{ required:'邮箱不可为空', email: "为邮箱格式xxx@xx.xx" },
                mobile:{ mobile: '手机格式不正确', required: "手机不可为空" },
                roleIds:{ required: "角色不可为空" },
            }
        });

        return formValidate;
    }

    /**
     * 对外通知事件
     * completed
     */
    var data = {
        formData: {},
        sysRoles: [],
        checkRoles: [],
        formValidate: {}
    };

    var component = {
        template: template,
        data: function(){ return data; },
        methods: {

            // 显示详情
            showInfoPanel: function (formData) {

                this.formValidate.resetForm();


                if( formData.id ) {
                    $("#password").rules("remove", 'required');
                    $("#confirm_password").rules("remove", "required");
                    // $("#confirm_password").rules("remove", "equalTo");
                } else {
                    $("#password").rules("add", { required: true, messages: { required: "密码不可为空"} });
                    $("#confirm_password").rules("add", {
                        required: true,
                        equalTo: "#password",
                        messages: { required: "密码不可为空", equalTo:"两次密码输入不一致" }
                    });
                }

                this.formData = formData;
                $('#addUserModal').modal('show');

                data.checkRoles = [];

                var id = formData.id;
                id = id ? id : 0;

                accessHttp({
                    url: buildUrl('/sysUser/detail/' + id),
                    success: function (res) {
                        data.formData = res.data.data1;
                        data.sysRoles = res.data.data2;

                        $(res.data.data2).each(function(){
                            if(this.hasRole ===1 ) {
                                data.checkRoles.push(this.id);
                            }
                        })

                    }
                });

            },

            // 提交
            subFormData: function() {

                if( $('#formValidate').valid() ) {

                    var postData = this.formData;
                    postData.roles = data.checkRoles;

                    let _url = '/sysUser/add';
                    _url = postData.id ? '/sysUser/modify' : _url;

                    var that = this;
                    accessHttp({
                        url: buildUrl(_url),
                        contentType: 'application/json; charset=utf-8',
                        data: JSON.stringify(postData),
                        type: 'post',
                        success: function (res) {
                            $('#addUserModal').modal('hide');

                            that.$emit('completed');
                        }
                    });
                }
            }

        }, created: function() {


        }, mounted: function() {

            this.formValidate = initFormValidate();
        }
    }

    return component;
});