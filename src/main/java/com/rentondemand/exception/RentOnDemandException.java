package com.rentondemand.exception;

public class RentOnDemandException extends Exception {

	private int code;
	private String message;
	private String status;
	
	
	public RentOnDemandException(RentOnDemandFailedResponse response) {
		super();
		this.code = response.getCode();
		this.message = response.getMessage();
		this.status = response.getStatus();
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
