define(['text!'+ctx+'/html/sysRole/list.html'], function ( template ) {

    var data = {
        tablePage:{totalCount: 0, currPage: 0, notificationChange: 0, reloadNotification: 0},
        queryData: {currPage: 1, pageSize: 5, keyword: null},
        entityDataList: []
    };

    var component =  {
        template: template,
        components: {
            'sys-role-info': createComponent('/js/sysRole/info.js')
        },
        data: function () {
            return data;
        },
        methods: {
            queryPageData: function (currPage, pageSize) {

                this.queryData.pageSize = pageSize;
                this.queryData.currPage = currPage;
                var queryStr = $.param(this.queryData);

                var that = this;
                accessHttp({
                    url: buildUrl('/sysRole/list?' + queryStr),
                    success: function (res) {
                        data.entityDataList = res.data.rows;
                        that.tablePage.totalCount = res.data.totalCount;
                        that.tablePage.notificationChange = new Date().getTime();
                    }
                });
            }, delRowData: function(id) {
                var that = this;
                popModal('确认提示', '确定是否删除？', null, function() {
                    $('#pop_box').modal('hide');
                    accessHttp({
                        url: buildUrl('/sysRole/del/' + id),
                        success: function (res) {
                            that.queryPageData(data.queryData.currPage, data.queryData.pageSize);
                        }
                    });
                });
            }, reloadPage: function () {
                this.tablePage.reloadNotification = new Date().getTime();
            }
        }
    }

    return component;
});