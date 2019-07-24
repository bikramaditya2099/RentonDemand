package com.rentondemand.beans;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.stereotype.Component;
@Entity
@Table(name = "BOOKING_INFO")
@Component
public class BookingBean {
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
private int bookingId;
@OneToOne(fetch = FetchType.EAGER)
@JoinColumn(name = "item_id",referencedColumnName="id")
private ItemBean itemBean;
private Date fromDate;
private Date toDate;
private Double totalPrice;
public int getBookingId() {
	return bookingId;
}
public void setBookingId(int bookingId) {
	this.bookingId = bookingId;
}
public ItemBean getItemBean() {
	return itemBean;
}
public void setItemBean(ItemBean itemBean) {
	this.itemBean = itemBean;
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
public Double getTotalPrice() {
	return totalPrice;
}
public void setTotalPrice(Double totalPrice) {
	this.totalPrice = totalPrice;
}



}
