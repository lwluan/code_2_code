define(['text!'+ctx+'/html/proProject/list.html'], function( template ) {

    'use strict'

    var data = {
        tablePage:{totalCount: 0, currPage: 0, notificationChange: 0, reloadNotification: 0},
        entityDataList: [],
        queryData: { currPage: 1, pageSize: 5, keyword: null},
    };

    var component = {
        template: template,
        components: {
            'entity-info': createComponent('/js/proProject/info.js')
        },
        data: function() { return data;  },
        methods: {
            queryPageData: function (currPage, pageSize) {

                this.queryData.pageSize = pageSize;
                this.queryData.currPage = currPage;
                var queryStr = $.param(this.queryData);

                var that = this;
                accessHttp({
                    url: buildUrl('/proProject/list?' + queryStr),
                    success: function (res) {
                        that.entityDataList = res.data.rows;
                        that.tablePage.totalCount = res.data.totalCount;
                        that.tablePage.notificationChange = new Date().getTime();
                    }
                });
            }, delRowData: function (id) {
                var that = this;
                popModal('确认提示', '确定是否删除？', null, function() {
                    $('#pop_box').modal('hide');
                    accessHttp({
                        url: buildUrl('/proProject/del/' + id),
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