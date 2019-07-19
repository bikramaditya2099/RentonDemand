package com.rentondemand.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.rentondemand.exception.RentOnDemandException;
@JsonInclude(Include.NON_NULL)
public class RentOnDemandResponse {
private int code;
private String message;
private String status;
private Object data;


public RentOnDemandResponse(RentOnDemandSuccessResponse response,Object data) {
	super();
	this.code = response.getCode();
	this.message = response.getMessage();
	this.status=response.getStatus();
	this.data=data;
}

public RentOnDemandResponse(RentOnDemandException response) {
	super();
	this.code = response.getCode();
	this.message = response.getMessage();
	this.status=response.getStatus();
}
public RentOnDemandResponse(RentOnDemandSuccessResponse response) {
	super();
	this.code = response.getCode();
	this.message = response.getMessage();
	this.status=response.getStatus();
}
public int getCode() {
	return code;
}

public String getStatus() {
	return status;
}
public void setStatus(String status) {
	this.status = status;
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
public Object getData() {
	return data;
}
public void setData(Object data) {
	this.data = data;
}


}
