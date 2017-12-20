function accessHttp(options) {

    /**
     * 公共拦截内容
     * 1、返回code
     * 2、错误统一提示
     * 3、登录超时统一处理 重新登录
     */

    var successCallBack = options.success;
    var errorCallBack = options.error;

    var success = function(res) {

        if( res.code == 10002 ) { // 未登录后台
            window.location.href = '/html/login.html';
        } else if( res.code == 10003 ) {

            popModal('无权限访问', '无权限访问', null, function() {
                $('#pop_box').modal('hide');
            });

        } else if( res.code == 10001 ) {
            popModal('无权限访问', res.message, null, function() {
                $('#pop_box').modal('hide');
            });
        } else {
            successCallBack.call(this, res);
        }
    }

    var error = function(res) {

        if( errorCallBack ) {
            errorCallBack.call(this, res);
        }
    }

    options.success = success;
    options.error = error;

    $.ajax(options);

}

function popModal(title, content, closeFun, confirmFun) {
    $('#pop_box').modal('show');
    $('#pop_box .modal-title').html(title);
    $('#pop_box .modal-body').html(content);

    $('#pop_box .close-btn')[0].onclick = function(){
        if( closeFun ) {
            closeFun.call(this);
        }
    };
    $('#pop_box .confirm-btn')[0].onclick = function(){
        if( confirmFun ) {
            confirmFun.call(this);
        }
    };

}

/**
 * 分页计算
 */
function evaluatePage(currPage, totalPage) {
    var pageSize = 10;// 页码个数
    var startNum = 0;// 开始页码
    var endNum = 0;// 结束页码

    // 当前页小于5
    if (currPage <= 5) {
        startNum = 1;
        if (totalPage > pageSize) {
            endNum = pageSize;
        } else {
            endNum = totalPage;
        }
    } else {
        if (currPage + 5 >= totalPage) {
            endNum = totalPage;
            if (totalPage - pageSize >= 1) {
                startNum = totalPage - pageSize + 1;
            } else {
                startNum = 1;
            }
        } else {
            endNum = currPage + 5;
            startNum = endNum - pageSize + 1;
        }
    }
    return { startNum: startNum, endNum: endNum };
}

function range(start, end) {
    var arr = [];
    for(var i=start; i<=end; i++) {
        arr.push(i);
    }
    return arr;
}

function buildUrl(url) {
    return ctx + url;
}

/** 返回组件 */
function createComponent(componentPath) {

    var renderTemplate = function(componentPath, resolve, reject) {
        require([ctx + componentPath], function ( _component ) {
            resolve( _component );
        });
    }

    return function(resolve, reject) {
        renderTemplate(componentPath, resolve, reject);
    }
}

$.validator.setDefaults({
    errorClass: 'text-danger',
    errorElement: 'span',
    ignore: [], // 为空时验证所有控件，隐藏表单也要验证
    submitHandler: function() {
        alert("提交事件!");
    }, errorPlacement : function(label, element) {
        var helpBlock = $(element).parents('.form-group').find('.help-block');
        helpBlock.find('.input-tip').hide();
        helpBlock.append(label);
    },
    success: function (element) {
        var helpBlock = $(element).parents('.form-group').find('.help-block');
        var tip = helpBlock.find('.input-tip');
        var errorLabel = helpBlock.find('.text-danger');

        if(errorLabel.html()) {
            tip.hide();
            tip.removeClass('text-success');
        } else {
            tip.show();
            tip.addClass('text-success');
        }
    },
    showErrors: function(errorMap, errorList) {
        this.defaultShowErrors();
        var keys = Object.keys(errorMap);
        if(keys.length > 0) {
            var helpBlock = $('#' + keys[0]).parents('.form-group').find('.help-block');
            helpBlock.find('.input-tip').hide();
        }
    }
});

$.validator.addMethod("mobile", function(value, element) {
    var length = value.length;
    var mobile = /^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\d{8}$/;
    return this.optional(element) || (length == 11 && mobile.test(value));
}, "请正确填写您的手机号码");

(function(jQuery) {

	if (jQuery.browser)
		return;

	jQuery.browser = {};
	jQuery.browser.mozilla = false;
	jQuery.browser.webkit = false;
	jQuery.browser.opera = false;
	jQuery.browser.msie = false;

	var nAgt = navigator.userAgent;
	jQuery.browser.name = navigator.appName;
	jQuery.browser.fullVersion = '' + parseFloat(navigator.appVersion);
	jQuery.browser.majorVersion = parseInt(navigator.appVersion, 10);
	var nameOffset, verOffset, ix;

	// In Opera, the true version is after "Opera" or after "Version"
	if ((verOffset = nAgt.indexOf("Opera")) != -1) {
		jQuery.browser.opera = true;
		jQuery.browser.name = "Opera";
		jQuery.browser.fullVersion = nAgt.substring(verOffset + 6);
		if ((verOffset = nAgt.indexOf("Version")) != -1)
			jQuery.browser.fullVersion = nAgt.substring(verOffset + 8);
	}
	
	// In MSIE, the true version is after "MSIE" in userAgent
	else if ((verOffset = nAgt.indexOf("MSIE")) != -1) {
		jQuery.browser.msie = true;
		jQuery.browser.name = "Microsoft Internet Explorer";
		jQuery.browser.fullVersion = nAgt.substring(verOffset + 5);
	}
	
	// In Chrome, the true version is after "Chrome"
	else if ((verOffset = nAgt.indexOf("Chrome")) != -1) {
		jQuery.browser.webkit = true;
		jQuery.browser.name = "Chrome";
		jQuery.browser.fullVersion = nAgt.substring(verOffset + 7);
	}
	
	// In Safari, the true version is after "Safari" or after "Version"
	else if ((verOffset = nAgt.indexOf("Safari")) != -1) {
		jQuery.browser.webkit = true;
		jQuery.browser.name = "Safari";
		jQuery.browser.fullVersion = nAgt.substring(verOffset + 7);
		if ((verOffset = nAgt.indexOf("Version")) != -1)
			jQuery.browser.fullVersion = nAgt.substring(verOffset + 8);
	}
	
	// In Firefox, the true version is after "Firefox"
	else if ((verOffset = nAgt.indexOf("Firefox")) != -1) {
		jQuery.browser.mozilla = true;
		jQuery.browser.name = "Firefox";
		jQuery.browser.fullVersion = nAgt.substring(verOffset + 8);
	}
	
	// In most other browsers, "name/version" is at the end of userAgent
	else if ((nameOffset = nAgt.lastIndexOf(' ') + 1) < (verOffset = nAgt
			.lastIndexOf('/'))) {
		jQuery.browser.name = nAgt.substring(nameOffset, verOffset);
		jQuery.browser.fullVersion = nAgt.substring(verOffset + 1);
		if (jQuery.browser.name.toLowerCase() == jQuery.browser.name
				.toUpperCase()) {
			jQuery.browser.name = navigator.appName;
		}
	}
	
	// trim the fullVersion string at semicolon/space if present
	if ((ix = jQuery.browser.fullVersion.indexOf(";")) != -1)
		jQuery.browser.fullVersion = jQuery.browser.fullVersion
				.substring(0, ix);
	if ((ix = jQuery.browser.fullVersion.indexOf(" ")) != -1)
		jQuery.browser.fullVersion = jQuery.browser.fullVersion
				.substring(0, ix);

	jQuery.browser.majorVersion = parseInt('' + jQuery.browser.fullVersion, 10);
	if (isNaN(jQuery.browser.majorVersion)) {
		jQuery.browser.fullVersion = '' + parseFloat(navigator.appVersion);
		jQuery.browser.majorVersion = parseInt(navigator.appVersion, 10);
	}
	jQuery.browser.version = jQuery.browser.majorVersion;
})(jQuery);



