package com.scheng.concurrency.contact;

/**
 * Created by scheng on 7/21/2015.
 */
public class Contact {
    private String phone, name;

    	public String getPhone() {
    		return phone;
    	}

    	public String getName() {
    		return name;
    	}

    	public Contact(String phone, String name) {
    		this.phone = phone;
    		this.name = name;
    	}
}
