package com.tinckay.common;

public class ResponseObj {
	private int err;
	private String msg;
	private Object obj;
	
	public ResponseObj() {
		err = 0;
		msg = "";
		obj = null;
	}
	
	public int getErr() {
		return err;
	}
	
	public ResponseObj(int err, String msg, Object obj) {
		super();
		this.err = err;
		this.msg = msg;
		this.obj = obj;
	}

	public ResponseObj(String msg, Object obj) {
		super();
		this.msg = msg;
		this.obj = obj;
	}

	public ResponseObj(String msg) {
		super();
		this.msg = msg;
	}

	
	public ResponseObj(Object obj) {
		super();
		this.obj = obj;
	}

	public void setErr(int err) {
		this.err = err;
	}
	
	public String getMsg() {
		return msg;
	}
	
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public Object getObj() {
		return obj;
	}
	
	@Override
	public String toString() {
		return "Response [err=" + err + ", msg=" + msg + ", obj=" + obj + "]";
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}
}
