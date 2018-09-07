package com.dmw.eteachswayam.exo.model;


public class RegBinClass {
	private String classpath;
	private String ActDate;
	private String ExpDate;
	private String CouponNo;
	private String Courefnumber;
	private String uName;
	private String hhid;
	public RegBinClass() {
		super();
		// TODO Auto-generated constructor stub
	}
	public RegBinClass( String classpath, String ActDate, String ExpDate, String CouponNo, String Courefnumber, String uName, String hhid) {
		super();
		this.CouponNo=CouponNo;
		this.classpath = classpath;
		this.ActDate = ActDate;
		this.ExpDate = ExpDate;
		this.Courefnumber = Courefnumber;
		this.uName=uName;
		this.hhid=hhid;
	}
	
	public
    String getclasspath() {
		return classpath;
	}
	
	public void setclasspath(String classpath) {
		this.classpath = classpath;
	}

	public
    String getCourefnumber() {
		return Courefnumber;
	}
	public void setCourefnumber(String courefnumber) {
		Courefnumber = courefnumber;
	}
	public
    String getActDate() {
		return ActDate;
	}
	public void setActDate(String ActDate) {
		this.ActDate = ActDate;
	}
	public
    String getExpDate() {
		return ExpDate;
	}
	public void setExpDate(String ExpDate) {
		this.ExpDate = ExpDate;

	}
	public
    String getCouponNo() {
		return CouponNo;
	}
	public void setCouponNo(String CouponNo) {
		this.CouponNo = CouponNo;
	}
	@Override
	public
    String toString() {
		return ExpDate;
	}
	public
    String getuName() {
		return uName;
	}
	public void setuName(String uName) {
		this.uName = uName;
	}
	public
    String getHhid() {
		return hhid;
	}
	public void setHhid(String hhid) {
		this.hhid = hhid;
	}



}
