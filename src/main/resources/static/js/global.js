IPAddress = "http://"+window.location.host+"/capi/";

function showSearchInput() {
  	if($('.search-input').parent().parent('li').css('display') == 'none') {
  		$('#searchBtn').parent('li').css('display', 'none');

  		$('.search-input').val("");
  		$('.search-input').parent().parent('li').css('display', 'block');
  		$('.search-input').focus();
  	 } else {
  	 	$('#searchBtn').parent("li").css('display', 'block');
  	 	$('.search-input').parent().parent('li').css('display', 'none');
  	 }
}

// 日期计算
function  DateDiff(startDate,  endDate){    // 日期格式：yyyy-MM-dd
	var startTime = new Date(Date.parse(startDate.replace(/-/g,   "/"))).getTime();
    	var endTime = new Date(Date.parse(endDate.replace(/-/g,   "/"))).getTime();
    	var dates = Math.abs((startTime - endTime))/(1000*60*60*24);     
    	return  dates;
}

// 读取url参数
function GetQueryString(key) {
      var reg = new RegExp("(^|&)"+key+"=([^&]*)(&|$)");
      var result = window.location.search.substr(1).match(reg);
      return result?decodeURIComponent(result[2]):null;
}
