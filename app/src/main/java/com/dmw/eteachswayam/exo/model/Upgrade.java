package com.dmw.eteachswayam.exo.model;

public class Upgrade {
	String appName;
	String version;
	String instaler;
	public Upgrade( String appName, String version, String instaler) {
		super();
		this.appName = appName;
		this.version = version;
		this.instaler = instaler;
	}
	public Upgrade() {
		super();
		// TODO Auto-generated constructor stub
	}
	public
    String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public
    String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public
    String getInstaler() {
		return instaler;
	}
	public void setInstaler(String instaler) {
		this.instaler = instaler;
	}

}
