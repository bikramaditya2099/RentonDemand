package com.rentondemand.beans;

import java.util.Date;

public class BookingRequest {
private int id;
private Date fromDate;
private Date toDate;
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public Date getFromDate() {
	return fromDate;
}
public void setFromDate(Date fromDate) {
	this.fromDate = fromDate;
}
public Date getToDate() {
	return toDate;
}
public void setToDate(Date toDate) {
	this.toDate = toDate;
}


}
