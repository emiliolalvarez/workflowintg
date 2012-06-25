package com.workflowintg.partner;

import java.util.LinkedList;
import java.util.List;

public class PartnerAd {

	private String xml;
	private String partner;
	private String title;
	private String description;
	private int category;
	
	List<String> images = new LinkedList<String>();

	public void addImageUrl(String url){
		images.add(url);
	}
	
	public List<String> getImages(){
		return images;
	}
	
	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setCategory(int category){
		this.category = category;
	}
	
	public int getCategory(){
		return category;
	}

}
