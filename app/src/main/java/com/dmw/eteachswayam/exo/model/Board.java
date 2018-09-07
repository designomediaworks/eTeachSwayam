package com.dmw.eteachswayam.exo.model;

public class Board{
private String actualName;
private String displayName;
public Board(){
    
}
public Board( String actualName, String displayName) {
    super();
    this.actualName = actualName;
    this.displayName = displayName;
}
public
String getActualName() {
    return actualName;
}
public void setActualName(String actualName) {
    this.actualName = actualName;
}
public
String getDisplayName() {
    return displayName;
}
public void setDisplayName(String displayName) {
    this.displayName = displayName;
}
@Override
    public
String toString() {
        
        return displayName;
    }
@Override
    public boolean equals(Object o) {
    String name =(String )o;
        if(name.equals(actualName))
        return true;
        return false;
    }

}
