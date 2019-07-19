package com.rentondemand.exception;

public enum RentOnDemandFailedResponse {

REGISTERATION_FAIL(4000,"USER REGISTERATION FAILED","FAIL"),
USER_ALREADY_EXIST(4001,"USER ALREADY EXIST","FAIL"),
EMPTY_EMAIL(4002,"EMAIL IS EMPTY","FAIL"),
EMPTY_NAME(4003,"NAME FIELD IS EMPTY","FAIL"),
EMPTY_PASSWORD(4004,"PASSWORD FIELD IS EMPTY","FAIL"),
EMPTY_PHONE(4005,"PHONE FIELD IS EMPTY","FAIL"),
LOGIN_FAILED(4006,"INVALID CREDENTIALS","FAIL")
;
	
	
	
	private RentOnDemandFailedResponse(int code, String message, String status) {
		this.code = code;
		this.message = message;
		this.status = status;
	}
	private int code;
	private String message;
	private String status;
	
	public int getCode() {
		return code;
	}
	public String getMessage() {
		return message;
	}
	public String getStatus() {
		return status;
	}
	
	
	
}
