package com.dmw.eteachswayam.exo.model;

import android.os.Environment;
import android.util.Log;
import android.widget.EditText;

import java.util.ArrayList;

public class Constant {

    public static final String WEBSERVER = "www.dmwerp.com";
    public static String ProfGetData;
    public static final String WEBSERVE ="http://www.eteach.co.in";
    public static ArrayList<EditText> arrayListValidation;
    public static       boolean checkValidation =true;
    public static final String  appName         ="eTeach";
    public static Float ScreenSize;
    public static String ContentPath ="";
    public static ArrayList<ChapterName> chapterArrayList;

    public static final String INTERNAL_SDCARD = Environment.getExternalStorageDirectory().getAbsolutePath() + "/eteach/assets/Content/";
    public static       String EXTERNAL_SDCARD ="";

    public static
    String algo( String value){

        //CharSequence value[];
        String SentToSer ="";
        String GetFrmSer ="";
        int    count     = 0;

        value=value.toUpperCase();
        try{
            for (int i = 0; i < value.length(); i++)
            {
                if (value.charAt(i) == '0') { SentToSer += '2'; GetFrmSer += '7'; } else if (value.charAt(i) == '1') { SentToSer += '8'; GetFrmSer += '4'; }
                else if (value.charAt(i) == '2') { SentToSer += '5'; GetFrmSer += '8'; } else if (value.charAt(i) == '3') { SentToSer += '9'; GetFrmSer += '1'; }
                else if (value.charAt(i) == '4') { SentToSer += '0'; GetFrmSer += '9'; } else if (value.charAt(i) == '5') { SentToSer += '3'; GetFrmSer += '2'; }
                else if (value.charAt(i) == '6') { SentToSer += '7'; GetFrmSer += '0'; } else if (value.charAt(i) == '7') { SentToSer += '1'; GetFrmSer += '3'; }
                else if (value.charAt(i) == '8') { SentToSer += '6'; GetFrmSer += '5'; } else if (value.charAt(i) == '9') { SentToSer += '4'; GetFrmSer += '6'; }

                else if (value.charAt(i) == 'A') { SentToSer += 'S'; GetFrmSer += 'K'; } else if (value.charAt(i) == 'B') { SentToSer += 'G'; GetFrmSer += 'E'; }
                else if (value.charAt(i) == 'C') { SentToSer += 'I'; GetFrmSer += 'T'; } else if (value.charAt(i) == 'D') { SentToSer += 'M'; GetFrmSer += 'F'; }
                else if (value.charAt(i) == 'E') { SentToSer += 'L'; GetFrmSer += 'B'; } else if (value.charAt(i) == 'F') { SentToSer += 'R'; GetFrmSer += 'Q'; }
                else if (value.charAt(i) == 'G') { SentToSer += 'U'; GetFrmSer += 'L'; } else if (value.charAt(i) == 'H') { SentToSer += 'A'; GetFrmSer += 'C'; }
                else if (value.charAt(i) == 'I') { SentToSer += 'P'; GetFrmSer += 'N'; } else if (value.charAt(i) == 'J') { SentToSer += 'N'; GetFrmSer += 'Y'; }
                else if (value.charAt(i) == 'K') { SentToSer += 'W'; GetFrmSer += 'I'; } else if (value.charAt(i) == 'L') { SentToSer += 'Y'; GetFrmSer += 'R'; }
                else if (value.charAt(i) == 'M') { SentToSer += 'B'; GetFrmSer += 'H'; } else if (value.charAt(i) == 'N') { SentToSer += 'K'; GetFrmSer += 'J'; }
                else if (value.charAt(i) == 'O') { SentToSer += 'Z'; GetFrmSer += 'X'; } else if (value.charAt(i) == 'P') { SentToSer += 'C'; GetFrmSer += 'Z'; }
                else if (value.charAt(i) == 'Q') { SentToSer += 'F'; GetFrmSer += 'A'; } else if (value.charAt(i) == 'R') { SentToSer += 'D'; GetFrmSer += 'V'; }
                else if (value.charAt(i) == 'S') { SentToSer += 'J'; GetFrmSer += 'O'; } else if (value.charAt(i) == 'T') { SentToSer += 'Q'; GetFrmSer += 'M'; }
                else if (value.charAt(i) == 'U') { SentToSer += 'V'; GetFrmSer += 'S'; } else if (value.charAt(i) == 'V') { SentToSer += 'H'; GetFrmSer += 'P'; }
                else if (value.charAt(i) == 'W') { SentToSer += 'E'; GetFrmSer += 'D'; } else if (value.charAt(i) == 'X') { SentToSer += 'O'; GetFrmSer += 'U'; }
                else if (value.charAt(i) == 'Y') { SentToSer += 'X'; GetFrmSer += 'G'; } else if (value.charAt(i) == 'Z') { SentToSer += 'T'; GetFrmSer += 'W'; }
                else if (value.charAt(i) == '_') { SentToSer += '_'; GetFrmSer += '_'; } else  { SentToSer += '-'; GetFrmSer += '-'; }


                count++;
            }
        }
        catch(Exception e){
            e.printStackTrace();
            Log.e( "Conversion Error", "Error encountered while converting.....");
        }
        int SentLen = SentToSer.length(); int GetLen = GetFrmSer.length();
        //         String fullMsg = Cls_Global_Var.AccountId + " " + SentToSer;
        //         label4.Text = "Send eteach ACT " + fullMsg + " to 57333 \nAnd press 'Activate' button if you have recieved activation key.";
        return SentToSer;
    }

    public static String libpath;

    public static String MAC_XML_DEMO_KEY  ="";
    public static String MAC_XML_DEMO_SALT = "";
    public static String MAC_XML_DEMO_IV   = "";

    public static String MAC_XML_FILE_ACTUAL_KEY  = "";
    public static String MAC_XML_FILE_ACTUAL_SALT = "";
    public static String MAC_XML_FILE_ACTUAL_IV   = "";

    public static final String SERVER_ADDRESS ="cdn.dmwerp.com.s3.amazonaws.com/vdata";

}
