package com.tinckay.common;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;



public abstract class ErrorHandlerController {
	@ExceptionHandler(Exception.class)
	public @ResponseBody ResponseObj handleError(HttpServletRequest req, Exception e) {
		e.printStackTrace();
		ResponseObj resp = new ResponseObj();
		resp.setErr(-1);
		resp.setMsg(e.getMessage());
		e.printStackTrace();
		return resp;
	}
}
