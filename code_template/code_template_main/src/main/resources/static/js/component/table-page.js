define(['text!'+ctx+'/html/component/table-page.html'], function( template ) {

    /**
     * 事件
     * change_page
     */

    var data = {
        startNum: 1,
        endNum: 5,
        totalPage: 1,
        currPage: 1,
        pageSize: 2,
        pageNumArr: [2, 12, 20, 50, 100]
    };
    var component = {
        props:['totalCount', 'notificationChange', 'reloadNotification'],
        template: template,
        data: function(){
            return data;
        },
        methods: {
            queryPageData: function(_currPage, disabled) {
                if( ! disabled ) {
                    this.currPage = _currPage;
                    this.$emit('change-page', _currPage, this.pageSize);
                }
            },
            reloadPageData: function() {
                this.totalPage = this.totalCount % this.pageSize == 0
                    ? this.totalCount / this.pageSize
                    : parseInt(this.totalCount / this.pageSize) + 1;

                var pageObj = evaluatePage(this.currPage, this.totalPage);
                this.startNum = pageObj.startNum;
                this.endNum = pageObj.endNum;

            }, changePageSize: function(pageSize) {
                this.pageSize = pageSize;
                this.queryPageData(1);
            }
        }, watch: {
            notificationChange: function(val) {
                this.reloadPageData();
            },
            reloadNotification: function(val) {
                this.$emit('change-page', this.currPage, this.pageSize);
            }
        }, created: function() {
            this.queryPageData(1);
        }
    }

    return component;
})