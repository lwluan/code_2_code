define(['text!' + ctx + '/html/component/app-slider-left.html'], function(app_slider_html){

    var component = {
        template: app_slider_html,
        data: {},
        methods : {
            toggle: function(event){

                var path = event.path;
                var menuLi;
                for(var i=0; i<path.length; i++) {
                    if( path[i].nodeName == 'LI' ) {
                        menuLi = path[i];
                        break;
                    }
                }

                // 判断哪个菜单是打开
                var nowClickLi;
                var menu_li = this.$refs.menu_ul.children;
                for(var i=0; i<menu_li.length; i++) {
                    if( menu_li[i] == menuLi ) {
                        nowClickLi = menuLi;
                        break;
                    }
                }

                var closeMenu = function(liNode, currLiNode) {
                    if( currLiNode == liNode ) return;

                    var iconNode = liNode.children[0].children[0];

                    var listGroup = liNode.getElementsByClassName('list-group');
                    if( listGroup.length > 0 ) {

                        iconNode.classList.remove('glyphicon-menu-down');
                        iconNode.classList.add('glyphicon-menu-right');

                        var listGroupUnit = listGroup[0];

                        var isHide = listGroupUnit.classList.contains('hide');
                        if( ! isHide ) {
                            listGroupUnit.classList.add('hide');
                        }
                    }
                }

                var toggleMenu = function(liNode) {
                    var nowClickListGroup = liNode.getElementsByClassName('list-group')[0];

                    var iconNode = liNode.children[0].children[0];

                    if( nowClickListGroup.classList.contains('hide') ) {
                        iconNode.classList.add('glyphicon-menu-down');
                        iconNode.classList.remove('glyphicon-menu-right');
                        nowClickListGroup.classList.remove('hide');

                    } else {
                        iconNode.classList.remove('glyphicon-menu-down');
                        iconNode.classList.add('glyphicon-menu-right');
                        nowClickListGroup.classList.add('hide');

                    }
                }

                if( nowClickLi ) {
                    var nowListGroup = nowClickLi.getElementsByClassName('list-group');

                    if( nowListGroup.length > 0 ) {
                        for(var i=0; i<menu_li.length; i++) {
                            var liNode = menu_li[i];
                            closeMenu(liNode, nowClickLi);
                        }
                        toggleMenu(nowClickLi);
                    }
                }
            }
        }
    }

    return component;
});