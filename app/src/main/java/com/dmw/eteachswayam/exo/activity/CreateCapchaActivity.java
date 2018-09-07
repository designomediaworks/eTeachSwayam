package com.dmw.eteachswayam.exo.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.dmw.eteachswayam.R;
import com.dmw.eteachswayam.exo.model.DeleteFile;
import com.dmw.eteachswayam.exo.model.Rijndael;
import com.dmw.eteachswayam.exo.model.XML;

import org.xml.sax.SAXException;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import javax.xml.parsers.ParserConfigurationException;

public class CreateCapchaActivity extends AppCompatActivity {

    Random random;
    private ImageView imageView;
    private TextView  textView;
    private Button    submitButton;
    private EditText  editText;
    private String    next;
    private String    capchaString;
    private Button    changeCapechButton;
    String path = Environment.getExternalStorageDirectory().getAbsolutePath();
    private String uptoString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_create_capcha);
        setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        Intent intent =getIntent();
        uptoString=intent.getStringExtra("uptoString");

        random=new Random ();
        new Rijndael ();
        initComponent();
    }
    private void initComponent() {
        ganerateId();
        showCapcha();
        registerEvent();
    }
    private void ganerateId() {
        imageView=(ImageView ) findViewById( R.id.capchaimageView);
        imageView.setBackgroundColor( Color.WHITE);
        textView=(TextView ) findViewById( R.id.textView1);
        submitButton=(Button ) findViewById( R.id.submitcapchaButton);
        editText=(EditText ) findViewById( R.id.enterTextCapcha);
        changeCapechButton=(Button ) findViewById( R.id.changeButton);
    }
    private void showCapcha() {
        try {
            capchaString=generateCaptchaString();

            foo(capchaString);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void registerEvent() {

        submitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(validation()){

                    String getEditTextCapcha =editText.getText().toString();
                    if(getEditTextCapcha.equals(capchaString)){
                        try {
                            FileInputStream  fileInputStream  =new FileInputStream ( uptoString + "/mac.xml");
                            FileOutputStream fileOutputStream =new FileOutputStream ( uptoString + "/macD.xml");
                            Rijndael.EDR(fileInputStream, fileOutputStream, 2);
                            new DeleteFile ( uptoString + "/mac.xml");
                            XML.editByDOM( uptoString + "/macD.xml", "User", "capchacount", "0");


                            FileInputStream  fileInputStream1  =new FileInputStream ( uptoString + "/macD.xml");
                            FileOutputStream fileOutputStream1 =new FileOutputStream ( uptoString + "/mac.xml");
                            Rijndael.EDR(fileInputStream1, fileOutputStream1, 1);

                            new DeleteFile(uptoString+"/macD.xml");

                        } catch (ParserConfigurationException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (SAXException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        Toast.makeText( CreateCapchaActivity.this, "Successfully", Toast.LENGTH_SHORT).show();
                        CreateCapchaActivity.this.finish();
                    }
                    else{
                        Toast.makeText( CreateCapchaActivity.this, "Please Enter Valid data", Toast.LENGTH_SHORT).show();
                    }
                }

            }

        });

        changeCapechButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                initComponent();
            }
        });
    }



    public
    String generateCaptchaString() {
        int length = 5 + ( Math.abs( random.nextInt()) % 3);

        StringBuffer captchaStringBuffer = new StringBuffer ();
        for (int i = 0; i < length; i++) {
            int baseCharNumber = Math.abs( random.nextInt()) % 62;
            int charNumber = 0;
            if (baseCharNumber < 26) {
                charNumber = 65 + baseCharNumber;
            } else if (baseCharNumber < 52) {
                charNumber = 97 + (baseCharNumber - 26);
            } else {
                charNumber = 48 + (baseCharNumber - 52);
            }
            captchaStringBuffer.append((char) charNumber);
        }

        return captchaStringBuffer.toString();
    }
    void foo(final String text) throws IOException {
        final Paint textPaint = new Paint () {
            {
                setColor( Color.BLACK);
                setTextAlign( Align.LEFT);

                setTextSize(50f);
                setAntiAlias(true);
            }
        };
        Display display = getWindowManager().getDefaultDisplay();

        int        width  =  ((display.getWidth()*35)/150);
        int        height = ((display.getHeight()*18)/200);
        final Rect bounds = new Rect ();
        textPaint.getTextBounds(text, 0, text.length(), bounds);
        final Bitmap bmp    = Bitmap.createBitmap( width, height, Bitmap.Config.RGB_565); //use ARGB_8888 for better quality
        final Canvas canvas = new Canvas ( bmp);
        canvas.drawColor( Color.WHITE);
        canvas.drawText(text, 20,50f, textPaint);
        //imageView.setScaleX(1);
        //imageView.setScaleY(1);

        imageView.setImageBitmap(bmp);

    }
    public boolean validation()
    {
        boolean isvalidate=true;
        if(editText.getText().toString().matches(""))
        {
            Toast.makeText( CreateCapchaActivity.this, "Please enter capecha", Toast.LENGTH_SHORT).show();
            isvalidate=false;

        }
        return isvalidate;
    }
}
