/**
 * 失去焦点时验证
 * @param inputNode
 * @returns {Boolean}
 */
function onblurVali(inputNode){
	inputNode = $(inputNode);
	inputDataVali(inputNode, "input");
}

/**
 * 整个form一次验证
 * @param formNode
 * @returns {Number}
 */
function formVali(formNode){
	var length = formNode.length;
	var errorCount = 0;
	for ( var i = 0; i < length; i++) {
		var node = formNode.elements[i];
		node = $(node);
		var result = inputDataVali(node, "form");
		if(result == false){
			errorCount += 1;
		}
	}
	return errorCount;
}

/**
 * 显示输入框验证提示
 * @param inputNode
 */
function showInputColor(inputNode,message, type){
	var controlGroupDiv = inputNode.parent();
	
	if(type == "error"){
		controlGroupDiv.addClass("has-error");
		controlGroupDiv.append("<label class='badge bg-red'>"+message+"</label>");
	}else if(type == "success"){
		controlGroupDiv.addClass("has-success");
	}else if(type == "warning"){
		controlGroupDiv.addClass("has-warning");
		controlGroupDiv.append("<label class='badge bg-yellow'>"+message+"</label>");
	}
}

/**
 * 隐藏输入框提示
 * @param inputNode
 */
function hiddenInputColor(inputNode){
	var controlGroupDiv = inputNode.parent();
	controlGroupDiv.children(".badge").remove();
	controlGroupDiv.removeClass("has-error");
	controlGroupDiv.removeClass("has-success");
	controlGroupDiv.removeClass("has-warning");
}

/**
 * 输入框数据验证
 * @param nodeId
 * @returns {Boolean}
 */
function inputDataVali(inputNode, nodeType){
	var vType = inputNode.attr("vType");
	var type= inputNode[0]["type"];
	
	if(type=="select-one"&&vType=="unexpect"){
		var selectVal = inputNode.val();
		var unValue = inputNode.attr("unValue");
		if(selectVal==unValue){
			hiddenInputColor(inputNode);
			showInputColor(inputNode, "error");
			return false;
		}else{
			hiddenInputColor(inputNode);
			showInputColor(inputNode, "success");
			return true;
		}
	}
	if(null != vType && (type == "text" || type == "password"||type=="textarea")){// || inputNode.type == "hidden"
		var value = inputNode.val();
		value = $.trim(value);
		inputNode.val(value);
		 
		var minLength = inputNode.attr("vMin");
		var maxLength = inputNode.attr("maxlength");//inputNode.getAttribute("vMax");

		var resultArr = {"result" : false, "message" : ""};
		
		//1.验证长度
		resultArr = utils_valiFunc.minMax(value, minLength, maxLength);
		var result = resultArr["result"];// true or false
		var message = resultArr["message"];
		
		if(result != true){
			hiddenInputColor(inputNode);
			showInputColor(inputNode, message, "error");
			return false;
		}else if(vType == "length"){//如果只验证长度
			hiddenInputColor(inputNode);
			showInputColor(inputNode, message, "success");
			return true;
			
		}else if(value.length == 0){
			hiddenInputColor(inputNode);
			showInputColor(inputNode, message, "success");
		}
		
		//2.验证格式
		if(value.length != 0){
			if(vType == "email"){//邮箱
				resultArr = utils_valiFunc.email(value);
				
			}else if(vType == "number"){//整数
				resultArr = utils_valiFunc.number(value);
				
			}else if(vType == "numberZ"){//正整数
				resultArr = utils_valiFunc.numberZ(value);
				
			}else if(vType == "floatZ"){//正浮点数：金额
				resultArr = utils_valiFunc.floatZ(value);
				
			}else if(vType == "chinaLetterNumber"){//中文字母数字
				resultArr = utils_valiFunc.chinaLetterNumber(value);
				
			}/*else if(vType == "string"){//普通验证
				resultArr = utils_valiFunc.string(value);
				
			}*/else if(vType == "letterNumber"){//字母数字
				resultArr = utils_valiFunc.letterNumber(value);
				
			}else if(vType == "tell"){//电话,如02788888888,注意没有横杠(-)
				resultArr = utils_valiFunc.tell(value);
				
			}else if(vType == "phone"){//手机
				resultArr = utils_valiFunc.phone(value);
				
			}else if(vType == "postboy"){//邮编
				resultArr = utils_valiFunc.postboy(value);
				
			}else if(vType == "idCard"){//身份证号15-18位
				resultArr = utils_valiFunc.idCard(value);
				
			}else if(vType == "qq"){//QQ
				resultArr = utils_valiFunc.qq(value);
				
			}else if(vType == "url"){//网址
				resultArr = utils_valiFunc.url(value);
				
			}else if(vType == "stature"){//身高
				resultArr = utils_valiFunc.stature(value);
				
			}else if(vType == "ip"){//IP
				resultArr = utils_valiFunc.ip(value);
				
			}else if(vType == "avoirdupoi"){//体重
				resultArr = utils_valiFunc.avoirdupoi(value);
				
			}
			
			result = resultArr["result"];// true or false
			message = resultArr["message"];
			
			if(result != true){
				hiddenInputColor(inputNode);
				showInputColor(inputNode, message, "error");
				return false;
			}else{
				hiddenInputColor(inputNode);
				showInputColor(inputNode, message, "success");
			}
		}
	}
	return true;
}
	
var utils_valiFunc = {
	/**
	 * 验证长短
	 * @param str
	 * @param minLength
	 * @param maxLength
	 * @returns
	 */
	"minMax" : function(str, minLength, maxLength){
		var objectLength = str.length;//.getBytes();//length;
		if(objectLength == 0 && minLength == 0){
			return {"result" : true, "message" : ""};
		}
		if(objectLength == 0){
			return {"result" : false, "message" : "数据不能为空！"};
		}
		if(null != minLength && null != maxLength && minLength==maxLength && objectLength < minLength){
			return {"result" : false, "message" : "长度至少为" + minLength};
		}
		if((null != minLength && objectLength < minLength) || (null != maxLength && objectLength > maxLength)){
			return {"result" : false, "message" : "最小长度为"+ minLength};
		}else{
			return {"result" : true, "message" : ""};
		}
	},
	
	/**
	 * 整数
	 * @param str
	 * @returns
	 */
	"number" : function(str){
		var result = str.match(/^(-|\+)?\d+$/);
	    if(result == null){
	    	return {"result" : false, "message" : "请输入整数"};
	    }else{
	    	return {"result" : true, "message" : ""};
	    }
	},

	/**
	 * 正整数
	 * @param str
	 * @returns
	 */
	"numberZ" : function(str){
		var result = str.match(/^[0-9]*[1-9][0-9]*$/);
	    if(result == null){
	    	return {"result" : false, "message" : "请输入正整数"};
	    }else{
	    	return {"result" : true, "message" : ""};
	    }
	},

	/**
	 * 正浮点数，可验证>=0 && <=99999999.99 的数字
	 * @param str
	 * @returns
	 */
	"floatZ" : function(str){
		//var exp = /^([1-9][\d]{0,7}|0)(\.[\d]{1,2})?$/;
		var exp = /^[1-9]\d*\.\d*|0\.\d*[1-9]\d*$/
	    if(exp.test(str)){
	    	return {"result" : true, "message" : ""};
	    }else{
	    	return {"result" : false, "message" : "请输入浮点类型"};
	    }
	},

	/**
	 * 中文_字母_数字
	 * @param str
	 * @returns
	 */
	"chinaLetterNumber" : function(str){
		var pattern = /^[0-9a-zA-Z\u4e00-\u9fa5]+$/i; 
		if (pattern.test(str)){ 
			return {"result" : true, "message" : ""};
		}else{ 
			return {"result" : false, "message" : "只能包含中文、字母、数字！"};
		} 
	},
	
	/**
	 * 字母_数字
	 * @param str
	 * @returns
	 */
	"letterNumber" : function(str){
		var letterNumber=/^[A-Za-z0-9]+$/;
		if(letterNumber.test(str)){
			return {"result" : true, "message" : ""};
		}else {
			showMessage = "";
			return {"result" : false, "message" : "只能输入字母和数字"};
		}
	},

	/**
	 * 验证邮箱
	 * @param str
	 * @returns
	 */
	"email" : function(str){
		var email = /^[\w.+-]+@(?:[-a-z0-9]+\.)+[a-z]{2,4}$/i;//正则/\b[^\s\@]+\@(?:[a-z\d-]+\.)+(?:com|net|org|info|cn|jp|gov|aero)\b/;
		if(email.test(str)){
			return {"result" : true, "message" : ""};
		}else {
			return {"result" : false, "message" : "请输入正确的邮箱地址"};
		}
	},

	/**
	 * 匹配固定电话或小灵通，例如：031185907468或02185907468格式
	 * @param str
	 * @returns
	 */
	"tell" : function(str){
		var partten = /^[+]{0,1}(\d){1,3}[ ]?([-]?((\d)|[ ]){1,12})+$/;
		if(partten.test(str)){
			return {"result" : true, "message" : ""};
		}else{
			return {"result" : false, "message" : "请输入正确的电话号码"};
		}
	},

	/**
	 * 手机
	 * @param str
	 * @returns
	 */
	"phone" : function(str){
		var partten = /^0?(13[0-9]|15[012356789]|18[0236789]|14[57])[0-9]{8}$/; ///^1[3,5]\d{9}$/;
		if(partten.test(str)){
			return {"result" : true, "message" : ""};
		}else{
			return {"result" : false, "message" : "请输入正确的手机号码"};
		}
	},

	/**
	 * 邮编
	 * @param str
	 * @returns
	 */
	"postboy" : function(str){
		var partten = /^[a-zA-Z0-9 ]{3,12}$/;
		if(partten.test(str)){
			return {"result" : true, "message" : ""};
		}else{
			return {"result" : false, "message" : "请输入正确的邮编号"};
		}
	},

	/**
	 * 身份证号15-18位
	 * @param str
	 * @returns
	 */
	"idCard" : function(str){
		var result=str.match(/\d{15}|\d{18}/);
	    if(result == null){
			return {"result" : false, "message" : "请输入15-18位的身份证号"};
	    }else{
	    	return {"result" : true, "message" : ""};
	    }
	},

	/**
	 * qq
	 * @param str
	 * @returns
	 */
	"qq" : function(str){
		var result = str.match(/^(-|\+)?\d+$/);
	    if(result == null){
			return {"result" : false, "message" : "请输入正确的QQ号"};
	    }else{
	    	return {"result" : true, "message" : ""};
	    }
	},

	/**
	 * URL 网址
	 * @param str
	 * @returns
	 */
	/*"url" : function(str){
		var result = str.match(/^[0-9a-zA-Z_-.:?&=\/%@]+$/);
	    if(result == null){
	    	return {"result" : false, "message" : "请输入正确的网址格式"};
	    }else{
	    	return {"result" : true, "message" : ""};
	    }
	},*/

	/**
	 * 	IP 地址
	 * @param str
	 * @returns
	 */
	"ip" : function(str){
		var val = /([0-9]{1,3}\.{1}){3}[0-9]{1,3}/;
	    var vald = val.exec(str);
	    if (vald == null) {
	    	return {"result" : false, "message" : "请按正确的IP格式输入"};
	    }
	    if (vald != '') {
	        if (vald[0] != str) {
	        	return {"result" : false, "message" : "请按正确的IP格式输入"};
	        }
	    }
	    return {"result" : true, "message" : ""};
	},

	/**
	 * 身高
	 * @param str
	 * @returns
	 */
	"stature" : function(str){
		var result = str.match(/^(-|\+)?\d+$/);
	    if(result == null){
	    	return {"result" : false, "message" : "请按正确的身高格式输入"};
	    }else{
	    	if(parseInt(str) < 25 || parseInt(str) > 250){
	    		return {"result" : false, "message" : "请输入区间 " + 25 + "-" + 250 + "cm 的值" };
			}
	    	return {"result" : true, "message" : ""};
	    }
	},
	
	/**
	 * 体重
	 * @param str
	 * @returns
	 */
	"avoirdupoi" : function(str){
		var result = str.match(/^(-|\+)?\d+$/);
	    if(result == null){
	    	return {"result" : false, "message" : "请按正确的体重格式输入"};
	    }else{
	    	if(parseInt(str) < 2 || parseInt(str) > 500){
	    		return {"result" : false, "message" : "应该在" + 2 + "-" + 500 + "kg"};
			}
	    	return {"result" : true, "message" : ""};
	    }
	},
	
	/**
	 * 判断文件上传类型
	 * @param valuePath
	 * @returns
	 */
	"valiFile" : function(valuePath){
		var imageGeShi = valuePath.substr(valuePath.lastIndexOf(".")+1);
		var geShi = ["jpg", "jpeg", "png"];
		var imageResult = false;
		for(var i in geShi){
			if(imageGeShi == geShi[i]){
				imageResult =  true;
			}
		}
		if(imageResult == false){
			return {"result" : false, "message" : "上传类型错误"};
		}else{
			return {"result" : true, "message" : ""};
		}
	}
};

/**
 * 去除字符串两端空格
 * @returns
 */
String.prototype.trim = function(){  
	return this.replace(/(^\s*)|(\s*$)/g, "");  
};

/**
 * 字符串真实长度
 * @returns
 */
String.prototype.getBytes = function() {    
    var cArr = this.match(/[^\x00-\xff]/ig);    
    return this.length + (cArr == null ? 0 : cArr.length);    
};