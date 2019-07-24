package com.rentondemand.beans;

import java.util.List;

public class ItemResponse {
private int itemId;
public int getItemId() {
	return itemId;
}
public void setItemId(int itemId) {
	this.itemId = itemId;
}
private String owner;
private String description;
private String title;
private Double rate;
private List<ItemImage> images;
public String getOwner() {
	return owner;
}
public void setOwner(String owner) {
	this.owner = owner;
}
public String getDescription() {
	return description;
}
public void setDescription(String description) {
	this.description = description;
}
public String getTitle() {
	return title;
}
public void setTitle(String title) {
	this.title = title;
}
public Double getRate() {
	return rate;
}
public void setRate(Double rate) {
	this.rate = rate;
}
public List<ItemImage> getImages() {
	return images;
}
public void setImages(List<ItemImage> images) {
	this.images = images;
}



}
