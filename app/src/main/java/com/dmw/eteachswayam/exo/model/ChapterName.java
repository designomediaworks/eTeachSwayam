package com.dmw.eteachswayam.exo.model;

import android.os.Parcel;
import android.os.Parcelable;


public class ChapterName implements Parcelable {
private int    flag;
private String key;
private String value;

public ChapterName() {
	super();
	// TODO Auto-generated constructor stub
}
public ChapterName( int flag, String key, String value) {
	super();
	this.flag = flag;
	this.key = key;
	this.value = value;
}
public int getFlag() {
	return flag;
}
public void setFlag(int flag) {
	this.flag = flag;
}
public
String getKey() {
	return key;
}
public void setKey(String key) {
	this.key = key;
}
public
String getValue() {
	return value;
}
public void setValue(String value) {
	this.value = value;
	this.value=this.value.replace(":","");
	this.value=this.value.replace("?","");
	this.value=this.value.replace("|","");
	this.value=this.value.replace("*","");
	this.value=this.value.replace(",","");
}
@Override
public
String toString() {
	return value.replace(" ", "_");
}
@Override
public void writeToParcel( Parcel dest, int flags) {

	dest.writeInt( flag);
	dest.writeString( key);
	dest.writeString( value);
	

}
public static final Creator<ChapterName> CREATOR =new Creator<ChapterName> () {

	public ChapterName[] newArray(int size) {
		return null;
	}

	public ChapterName createFromParcel(Parcel source) {
		int    i =source.readInt();
		String k =source.readString();
		String v =source.readString();
		return new ChapterName(i,k,v);
	}
};

@Override
public int describeContents() {

	return 0;
}
}
