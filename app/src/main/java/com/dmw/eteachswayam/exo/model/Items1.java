package com.dmw.eteachswayam.exo.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Items1 implements Parcelable {

	private String fileName;
	private double version;
	
	
	boolean text;
	public boolean isText() {
		return text;
	}
	public void setText(boolean text) {
		this.text = text;
	}
	public
    String getFileName() {
		return fileName;
	}
	@Override
	public boolean equals(Object o) {
		Items1 i=(Items1) o;
		if(this.fileName.toUpperCase().equals(i.fileName.toUpperCase()))
			return true;
		return false;
	}
	public boolean versionEquals(Items1 i) {
		if(this.version==i.version)
			return true;
		return false;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public Items1() {
		super();

	}
	public double getVersion() {
		return version;
	}
	public void setVersion(double version) {
		this.version = version;
	}
	
	@Override
	public
    String toString() {
		return  fileName ;
	}
	public Items1( String fileName, double version, long size) {
		super();
		this.fileName = fileName;
		this.version = version;
	}
	//@Override
	public int describeContents() {
		// TODO Auto-generated method stub

		return 0;
	}
	//@Override
	public void writeToParcel( Parcel dest, int flags) {
		// TODO Auto-generated method stub

		dest.writeString(fileName);
		dest.writeDouble(version);

	}
	public static Creator<Items1> CREATOR =new Creator<Items1> () {

		//@Override
		public Items1[] newArray(int size) {
			// TODO Auto-generated method stub
			return null;
		}

		//@Override
		public Items1 createFromParcel(Parcel source) {
			// TODO Auto-generated method stub

			Items1 i=new Items1();
			i.setFileName(source.readString());
			i.setVersion(source.readDouble());
			return i;
		}
	};

}
