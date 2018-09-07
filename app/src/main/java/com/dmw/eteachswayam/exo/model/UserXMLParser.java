package com.dmw.eteachswayam.exo.model;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class UserXMLParser {
    private User user;

    private static Context c;

    public static ArrayList<User> users;

    public UserXMLParser(Context c) {
        this.c = c;
        parce();

    }

    private void parce() {
        try {

            FileInputStream  fis = new FileInputStream ( Constant.INTERNAL_SDCARD + "/userxml.xml");
            FileOutputStream fos = new FileOutputStream ( Constant.INTERNAL_SDCARD + "/userxmlD.xml");

            new Rijndael();
            Rijndael.EDR(fis, fos, 2);
            fis.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            File                   fXmlFile  = new File ( Constant.INTERNAL_SDCARD + "/userxmlD.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder        dBuilder  = dbFactory.newDocumentBuilder();
            Document               doc       = dBuilder.parse( fXmlFile);

            doc.getDocumentElement().normalize();

            System.out.println( "Root element :" + doc.getDocumentElement().getNodeName());

            NodeList nList = doc.getElementsByTagName( "User");

            users = new ArrayList<User> ();

            for (int temp = 0; temp < nList.getLength(); temp++) {
                user = new User();
                Node nNode = nList.item( temp);

                System.out.println( "\nCurrent Element :" + nNode.getNodeName());

                if ( nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element ) nNode;

                    user.setV_UserName(eElement.getElementsByTagName("id").item(0).getTextContent());
                    user.setV_Password(eElement.getElementsByTagName("pass").item(0).getTextContent());
                    user.setAccountID(eElement.getElementsByTagName("AccountID").item(0).getTextContent());


                    NodeList val1 = eElement.getElementsByTagName( "appName");
                    if (val1.getLength() != 0) {
                        user.setAppNameInXml(eElement.getElementsByTagName("appName").item(0).getTextContent());
                    } else {
                        user.setAppNameInXml("");
                    }

                    //user.sethDDID( eElement.getElementsByTagName("HDDID").item(0).getTextContent());
                    NodeList val = eElement.getElementsByTagName( "HDDID");
                    if (val.getLength() != 0) {
                        user.sethDDID(eElement.getElementsByTagName("HDDID").item(0).getTextContent());
                    } else {
                        user.sethDDID("");
                    }

                    users.add(user);
                }
            }
            new File ( Constant.INTERNAL_SDCARD + "/userxmlD.xml").delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public
    String xmlcompare( String username, String password) {

        String s    = "false";
        User   user = new User();
        user.setV_UserName(username);
        user.setV_Password(password);
        user.sethDDID(getUdid());
        user.setAppNameInXml(Constant.appName);
        //parce();

        if (users != null) {

            for ( Iterator<User> iterator = users.iterator() ; iterator.hasNext(); ) {
                User u = iterator.next();
                if (u.equals(user)) {
                    s = u.getAccountID();
                    break;
                }
            }
        }
        if (user.equals(new User("eteach", "eteach", "1234ABCD", getUdid()))) {
            s = "eteach";
        }
        return s;
    }

    private static
    String getUdid() {

        final String macAddr, androidId;
        String       id = null;

        WifiManager wifiMan = (WifiManager )c.getSystemService( Context.WIFI_SERVICE);
        WifiInfo    wifiInf = wifiMan.getConnectionInfo();

        if(!wifiMan.isWifiEnabled())
        {
            wifiMan.setWifiEnabled(true);
            macAddr = wifiInf.getMacAddress();
            Log.e( "macAddr", "" + macAddr);
            androidId = "" + android.provider.Settings.Secure.getString(c.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
            Log.e( "androidId", "" + androidId);
            UUID deviceUuid = new UUID ( androidId.hashCode(), macAddr.hashCode());
            id=deviceUuid.toString().substring(deviceUuid.toString().lastIndexOf("-")+1);
            // text.setText(deviceUuid.toString().substring(deviceUuid.toString().lastIndexOf("-")+1));
            Log.e( "UUID", "" + id);
            wifiMan.setWifiEnabled(false);

        }
        else{
            macAddr = wifiInf.getMacAddress();
            Log.e( "macAddr", "" + macAddr);
            androidId = "" + android.provider.Settings.Secure.getString(c.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
            Log.e( "androidId", "" + androidId);
            UUID deviceUuid = new UUID ( androidId.hashCode(), macAddr.hashCode());
            id=deviceUuid.toString().substring(deviceUuid.toString().lastIndexOf("-")+1);
            // text.setText(deviceUuid.toString().substring(deviceUuid.toString().lastIndexOf("-")+1));
            Log.e( "UUID", "" + id);
        }
        return id;
    }

    public static void addUserTOXML(User user, String accountID) {

        File temp = new File ( Constant.INTERNAL_SDCARD + "/userxml.xml");
        try {
            if (temp.exists()) {
                try {
                    FileInputStream  fis = new FileInputStream ( Constant.INTERNAL_SDCARD + "/userxml.xml");
                    FileOutputStream fos = new FileOutputStream ( Constant.INTERNAL_SDCARD + "/userxmlD.xml");
                    temp = new File ( Constant.INTERNAL_SDCARD + "/userxmlD.xml");
                    new Rijndael();
                    Rijndael.EDR(fis, fos, 2);
                    if (users != null) {
                        if (users.contains(user)) {
                            new DeleteFile(Constant.INTERNAL_SDCARD + "/userxmlD.xml");
                            return;
                        } else {
                            new DeleteFile(Constant.INTERNAL_SDCARD + "/userxml.xml");
                            RandomAccessFile temp1 = new RandomAccessFile ( temp, "rw");
                            String           s     = "<User><id>" + user.getV_UserName() + "</id><pass>" + user.getV_Password() + "</pass><AccountID>" + accountID + "</AccountID><HDDID>" + user.gethDDID() + "</HDDID><appName>" + Constant.appName + "</appName></User></Users>";
                            temp1.seek(temp.length() - "</Users>".length());
                            temp1.write(s.getBytes());
                            temp1.close();
                        }
                    } else {
                        new DeleteFile(Constant.INTERNAL_SDCARD + "/userxml.xml");
                        RandomAccessFile temp1 = new RandomAccessFile ( temp, "rw");
                        String           s     = "<User><id>" + user.getV_UserName() + "</id><pass>" + user.getV_Password() + "</pass><AccountID>" + accountID + "</AccountID><HDDID>" + user.gethDDID() + "</HDDID><appName>" + Constant.appName + "</appName></User></Users>";
                        temp1.seek(temp.length() - "</Users>".length());
                        temp1.write(s.getBytes());
                        temp1.close();
                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    if (!new File ( Constant.INTERNAL_SDCARD).exists()) {
                        new File ( Constant.INTERNAL_SDCARD).mkdirs();
                    }
                    File temp2 =new File ( Constant.INTERNAL_SDCARD + "/userxmlD.xml");
                    temp2.createNewFile();
                    RandomAccessFile temp1 = new RandomAccessFile ( new File ( Constant.INTERNAL_SDCARD + "/userxmlD.xml"), "rw");
                    String           userN = user.getV_UserName();
                    String           userP = user.getV_Password();


                    String s = "<Users><BlockCount>0</BlockCount><User><id>" + userN + "</id><pass>" + userP + "</pass><AccountID>" + accountID + "</AccountID><HDDID>" + user.gethDDID() + "</HDDID></User></Users>";
                    Log.e( "S", "" + s);
                    temp1.write(s.getBytes());
                    temp1.close();


                    /*String fpath =Constant.INTERNAL_SDCARD + "/userxmlD.xml";

                    File file = new File(fpath);

                    // If file does not exists, then create it
                    if (!file.exists()) {
                        file.createNewFile();
                    }

                   // String s = "<User><id>" + user.getV_UserName() + "</id><pass>" + user.getV_Password() + "</pass><AccountID>" + accountID + "</AccountID><HDDID>" + getUdId() + "</HDDID><appName>" + Constant.appName + "</appName></User></Users>";
                    String s = "Hello";
                    Log.e("SSS",""+s);

                    FileWriter fw = new FileWriter(file.getAbsoluteFile());
                    BufferedWriter bw = new BufferedWriter(fw);
                    bw.write(s);
                    bw.close();*/


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            FileInputStream  fis = new FileInputStream ( Constant.INTERNAL_SDCARD + "/userxmlD.xml");
            FileOutputStream fos = new FileOutputStream ( Constant.INTERNAL_SDCARD + "/userxml.xml");

            new Rijndael();
            Rijndael.EDR(fis, fos, 1);

            new DeleteFile(Constant.INTERNAL_SDCARD + "/userxmlD.xml");
            fis.close();
            fos.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
