package com.hundsun.entity;

import javax.persistence.Entity;

@Entity
public class Connector {
	private String address;
	private int port;
	private String username;
	private String password;

	public Connector() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Connector(String address, int port, String username, String password) {
		super();
		this.address = address;
		this.port = port;
		this.username = username;
		this.password = password;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return address + ":" + port;
	}

}
