package com.dmw.eteachswayam.exo.model;

public class CheckInfo {

    String fileName;
    String size;
    public CheckInfo( String fileName, String size) {
        super();
        this.fileName = fileName;
        this.size = size;
    }

    public CheckInfo() {
        super();
        // TODO Auto-generated constructor stub
    }

    public
    String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public
    String getSize() {
        return size;
    }
    public void setSize(String size) {
        this.size = size;
    }

}
