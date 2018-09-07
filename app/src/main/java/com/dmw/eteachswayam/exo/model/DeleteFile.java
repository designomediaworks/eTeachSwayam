package com.dmw.eteachswayam.exo.model;

import java.io.File;


public class DeleteFile {

    public  DeleteFile(String filePath) {

        File f1 = new File ( filePath);
        f1.delete();
    }
}

