function dateFmt(input) {
	if (input) {
		var d = new Date(input);
		var year = d.getFullYear();
		var month = d.getMonth() + 1;
		var day = d.getDate() < 10 ? '0' + d.getDate() : '' + d.getDate();
		var hour = d.getHours() < 10 ? '0' + d.getHours() : '' + d.getHours();
		var minutes = d.getMinutes() < 10 ? '0' + d.getMinutes() : ''
				+ d.getMinutes();
		var seconds = d.getSeconds() < 10 ? '0' + d.getSeconds() : ''
				+ d.getSeconds();
		return year + '-' + month + '-' + day + ' ' + hour + ':' + minutes
				+ ':' + seconds;
	} else {
		return "";
	}
}

/**
 * make textarea expanding
 * @param el
 */
var setTextareaStyle = function(el) {  
    el.style.height = 'auto';  
    var h = el.scrollHeight;
    if( h < 20 ) {
    	h = 20;
    }
    el.style.height = h + 'px';  
}
function makeExpandingArea(el) {  
      
    var delayedResize = function(el) {  
        window.setTimeout(function() {  
                setTextareaStyle(el)  
        }, 0);  
    }  
    if (el.addEventListener) {  
        el.addEventListener('input', function() {  
            setTextareaStyle(el)  
        }, false);  
        setTextareaStyle(el)  
    } else if (el.attachEvent) {  
        el.attachEvent('onpropertychange', function() {  
            setTextareaStyle(el)  
        });  
        setTextareaStyle(el)  
    }  
    if (window.VBArray && window.addEventListener) { //IE9  
        el.attachEvent("onkeydown", function() {  
            var key = window.event.keyCode;  
            if (key == 8 || key == 46) delayedResize(el);  

        });  
        el.attachEvent("oncut", function() {  
            delayedResize(el);  
        }); //处理粘贴  
    }  
}  