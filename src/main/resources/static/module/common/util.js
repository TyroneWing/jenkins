function SetCookie(sName, sValue) {
    var date = new Date();
    date = new Date(date.valueOf() + 3600 * 1000);
    document.cookie = sName + "=" + escape(sValue) + "; expires=" + date.toGMTString();
}

function GetCookie(sName) {
    var cookies = document.cookie.split("; ");

    for (var i = 0; i < cookies.length; i++) {
        var aCrumb = cookies[i].split("=");

        if (sName == aCrumb[0]) {
            return unescape(aCrumb[1]);
        }
    }

    return null;
}

Date.prototype.Format = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1,
        "d+": this.getDate(),
        "h+": this.getHours(),
        "m+": this.getMinutes(),
        "s+": this.getSeconds(),
        "q+": Math.floor((this.getMonth() + 3) / 3),
        "S": this.getMilliseconds()
    };
    
    if (/(y+)/.test(fmt)) {
    	fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    }
    
    for (var k in o) {
    	if (new RegExp("(" + k + ")").test(fmt)) {
    		fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    	}
    }
    
    return fmt;
}

/**
 * 截取self.href最后的数字为id
 * @param url
 * @returns
 */
function getIdFromUrl(url) {
	var id = url.substring(url.lastIndexOf("/")+1, url.length);
	return id;
}
