package org.sgpro.wps;

import org.hibernate.exception.ConstraintViolationException;

public class Result {
	private String code;
	private String message;
	private Object data;
	
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Result(String code, String message) {
		super();
		this.code = code;
		this.message = message;
	}
	
	
	public Result() {
		// TODO Auto-generated constructor stub
	}
	
	public static Result success() {  
		return new Result("0", "OK");
	}
	
	public static Result unknowException(Throwable t) {
		 
		return new Result("99", t.getMessage());
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return GsonProvider.getGson().toJson(this);
	}
}
