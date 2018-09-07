package com.dmw.eteachswayam.exo.model;

import android.content.Context;
import android.widget.EditText;

import java.util.ArrayList;

public class ValidationInRegister {
    Context context;

    public ValidationInRegister(Context context)
    {
        this.context=context;
    }

    public  void checkEmptyValidation( ArrayList<EditText> alValidation, String[] strings){
        for (int i = 0; i < alValidation.size(); i++) {
            if(alValidation.get(i).getText().toString()==null || alValidation.get(i).getText().toString().isEmpty())
            {
                alValidation.get(i).setError("Please enter"+" "+strings[i]);
                if(Constant.checkValidation)
                    //editextArrayList.get(i).requestFocus();
                    Constant.checkValidation=false;
            }
            else{
                alValidation.get(i).setError(null);
            }
        }
        Constant.checkValidation=true;
    }

    public void resetAllEditText(ArrayList<EditText> resetArrayList){
        for (int i = 0; i < resetArrayList.size(); i++) {
            resetArrayList.get(i).setText("");
        }
    }

}

