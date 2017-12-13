define(['text!'+ctx+'/html/sysRole/info.html', 'rcss!/css/zTreeStyle/zTreeStyle', '/js/lib/jquery.ztree.all-3.5.min.js'], function ( template ) {

    var initFormValidate = function() {
        /** 表单验证 **/
        var formValidate = $('#formValidate').validate({
            rules:{
                name:{ required:true },
                description:{ required:true },
                authorities: {required:true },
            },
            messages:{
                name:{ required: "用户名不可为空" },
                description:{ required: "说明不可为空" },
                authorities:{ required: "角色不可为空" },
            }
        });

        return formValidate;
    }

    var data = {
        formData: { },
        authorityTree: null,
        formValidate: {}
    };

    var component =  {
        template: template,
        data: function () {
            return data;
        },
        methods: {
            showInfoPanel: function (formData) {
                this.formData = formData;
                $('#addRoleModal').modal('show');

                var id = formData.id;
                id = id ? id : 0;

                accessHttp({
                    url: buildUrl('/sysRole/detail/' + id),
                    success: function (res) {
                        data.formData = res.data.sysRole;

                        var authList = res.data.authList;
                        // show tree

                        var setting = {
                            check: { enable: true },
                            data: { simpleData: { enable: true } }
                        };

                        zNodes = authList;
                        data.authorityTree = $.fn.zTree.init($("#authorityTree"), setting, zNodes);

                    }
                });

            },
            subFormData: function() {

                if( $('#formValidate').valid() ) {
                    var _nodes = data.authorityTree.getCheckedNodes(true);

                    var authIds = [];
                    for (var i = 0; i < _nodes.length; i++) {
                        authIds.push(_nodes[i].id);
                    }

                    var postData = this.formData;
                    postData.authIds = authIds;

                    var that = this;
                    accessHttp({
                        url: buildUrl('/sysRole/modify'),
                        contentType: 'application/json; charset=utf-8',
                        data: JSON.stringify(postData),
                        type: 'post',
                        success: function (res) {
                            $('#addRoleModal').modal('hide');
                            that.$emit('completed');
                        }
                    });
                }
            }
        }, mounted: function() {
            this.formValidate = initFormValidate();
        }
    }

    return component;

});