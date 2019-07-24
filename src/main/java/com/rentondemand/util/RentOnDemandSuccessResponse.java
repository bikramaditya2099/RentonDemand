package com.rentondemand.util;

public enum RentOnDemandSuccessResponse {

	REGISTERED_SUCCESSFULLY(1000,"USER REGISTERED SUCCESSFULLY","SUCCESS"),
	LOGIN_SUCCESS(1001,"LOGIN SUCCESS","SUCCESS"),
	ITEM_ADDED(1002,"ITEM ADDED SUCCESFULLY","SUCCESS"),
	ITEMS_FETCHED(1003,"ITEMS FETCHED SUCCESSFULLY","SUCCESS"),
	ITEMS_BOOKED(1004,"ITEM Booked Successfully","Success")
	;
	
	
	private RentOnDemandSuccessResponse(int code, String message, String status) {
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
