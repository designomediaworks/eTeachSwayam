package com.dmw.eteachswayam.exo.model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    @Override
    public
    String toString() {
        return "<UserInfo><V_UserName>" + V_UserName
                + "</V_UserName><V_Password>" + V_Password + "</V_Password><appName>"+appNameInXml+"</appName></UserInfo>";
    }

    private String V_UserName;
    private String V_Password;
    private String accountID;
    private String hDDID;
    private String appNameInXml;

    public
    String getAppNameInXml() {
        return appNameInXml;
    }
    public void setAppNameInXml(String appNameInXml) {
        this.appNameInXml = appNameInXml;
    }
    public
    String getAccountID() {
        return accountID;
    }
    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }
    public User() {
        super();
    }
    public User( String username, String password) {
        this.V_UserName=username;
        this.V_Password=password;

    }
    public User( String username, String password, String hDDID) {
        this.V_UserName=username;
        this.V_Password=password;
        this.hDDID=hDDID;
    }

    public User( String v_UserName, String v_Password, String accountID, String hDDID) {
        super();
        V_UserName = v_UserName;
        V_Password = v_Password;
        this.accountID = accountID;
        this.hDDID=hDDID;
    }

    public User( String v_UserName, String v_Password, String hDDID, String appName, String empty) {
        super();
        V_UserName = v_UserName;
        V_Password = v_Password;
        this.hDDID=hDDID;
        this.appNameInXml = appName;
    }

    public
    String gethDDID() {
        return hDDID;
    }

    public void sethDDID(String hDDID) {
        this.hDDID = hDDID;
    }

    public
    String getV_UserName() {
        return V_UserName;
    }

    public void setV_UserName(String v_UserName) {
        V_UserName = v_UserName;
    }

    public
    String getV_Password() {
        return V_Password;
    }

    public void setV_Password(String v_Password) {
        V_Password = v_Password;
    }

    @Override
    public boolean equals(Object o) {
        User u = (User)o;
        if(this.V_UserName.equalsIgnoreCase(u.V_UserName)&&this.V_Password.equalsIgnoreCase(u.V_Password)&&this.hDDID.equals(u.hDDID)&&this.appNameInXml.equals(u.appNameInXml))
            return true;
        return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel( Parcel dest, int flags) {


        dest.writeString( V_UserName);
        dest.writeString( V_Password);
        dest.writeString( accountID);
        dest.writeString( hDDID);
        dest.writeString(appNameInXml);
    }
    public static Parcelable.Creator<User> CREATOR =new Creator<User> () {

        public User[] newArray(int size) {
            return null;
        }

        public User createFromParcel(Parcel source) {
            User u=new User();

            u.setV_UserName(source.readString());
            u.setV_Password(source.readString());
            u.setAccountID(source.readString());
            u.sethDDID(source.readString());
            u.setAppNameInXml(source.readString());
            return u;
        }
    };
}


