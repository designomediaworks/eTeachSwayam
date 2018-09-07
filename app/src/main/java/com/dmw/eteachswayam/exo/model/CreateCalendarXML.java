package com.dmw.eteachswayam.exo.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


public class CreateCalendarXML {

	private static int              i;
	private static String           actDate;
	private static String           ServerDate;
	private static String           stdNum;
	private static FileInputStream  fis;
	private static FileOutputStream fos;
	private static String           medium;
	private static String           board;
	private static String           stdStr;
	private static String           path;
	private static String           macpath;
	private static String           classs;
	private static String           accountId;
	private static String           username;
	private static int              N_CalCounter;
	private static String           HDID;
	private static String           USNAME;
	private static String           accountIdtocalender;
	public CreateCalendarXML( String date, String board, String medium, String classs, String pat, String macpath, Context c, int N_CalCounter, String HDID, String USNAME, String accountIdtocalender)
	{
		CreateCalendarXML.board=board;
		CreateCalendarXML.medium = medium;
		CreateCalendarXML.classs = classs;
		CreateCalendarXML.path=pat;
		CreateCalendarXML.macpath=macpath;
		CreateCalendarXML.N_CalCounter=N_CalCounter;
		actDate = date;
		SharedPreferences settings = c.getSharedPreferences( "username", 0);
		accountId =settings.getString("ACCOUNTID", "xxx");
		username =settings.getString("user","xxx");
		CreateCalendarXML.HDID=HDID;
		CreateCalendarXML.USNAME=USNAME;
		CreateCalendarXML.accountIdtocalender=accountIdtocalender;
		buildXML(actDate);

	}

	public static
    String getSTD( int stdNum2) {
		// TODO Auto-generated method stub
		if(stdNum2 == 1)
		{
			stdStr = "I";
			return stdStr;
		}
		if(stdNum2 == 2)
		{
			stdStr = "II";
			return stdStr;
		}if(stdNum2 == 3)
		{
			stdStr = "III";
			return stdStr;
		}if(stdNum2 == 4)
		{
			stdStr = "IV";
			return stdStr;
		}if(stdNum2 == 5)
		{
			stdStr = "V";
			return stdStr;
		}if(stdNum2 == 6)
		{
			stdStr = "VI";
			return stdStr;
		}if(stdNum2 == 7)
		{
			stdStr = "VII";
			return stdStr;
		}if(stdNum2 == 8)
		{
			stdStr = "VIII";
			return stdStr;
		}if(stdNum2 == 9)
		{
			stdStr = "IX";
			return stdStr;
		}if(stdNum2 == 10)
		{
			stdStr = "X";
			return stdStr;
		}if(stdNum2 == 11)
		{
			stdStr = "XI";
			return stdStr;
		}if(stdNum2 == 12)
		{
			stdStr = "XII";
			return stdStr;
		}
		return null;
	}

	private void buildXML(String presentDate2) {
		
		try {

			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder        docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc         = docBuilder.newDocument();
			Element  rootElement = doc.createElement( "Calendar");
			doc.appendChild(rootElement);

			for(i=0;i<=CreateCalendarXML.N_CalCounter;i++)
			{
				Element staff = doc.createElement( "Date");
				rootElement.appendChild(staff);

				Element dt = doc.createElement( "dt");
				dt.appendChild(doc.createTextNode(actDate));
				actDate = addDate(actDate);
				staff.appendChild(dt);

				Element flag = doc.createElement( "flag");
				flag.appendChild(doc.createTextNode("0"));
				staff.appendChild(flag);

				Element counter = doc.createElement( "counter");
				counter.appendChild(doc.createTextNode("0"));
				staff.appendChild(counter);

				Element accNo = doc.createElement( "CardNo");
				accNo.appendChild(doc.createTextNode(CreateCalendarXML.accountIdtocalender));
				staff.appendChild(accNo);
				
				Element classname = doc.createElement( "ClassName");
				classname.appendChild(doc.createTextNode(board+"/"+medium+"/"+classs));
				staff.appendChild(classname); 
				
				Element hdidtab = doc.createElement( "HDDID");
				hdidtab.appendChild(doc.createTextNode(CreateCalendarXML.HDID));
				staff.appendChild(hdidtab); 
				
				Element usernameforchecking = doc.createElement( "UName");
				usernameforchecking.appendChild(doc.createTextNode(CreateCalendarXML.USNAME));
				staff.appendChild(usernameforchecking); 
				
				

				
			}

			// write the content into xml file						\assets\Content\CBSE\ENGLISH\Class I
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer        transformer        = transformerFactory.newTransformer();
			DOMSource          source             = new DOMSource ( doc);
			Log.e( "path", CreateCalendarXML.macpath + "/" + algo( CreateCalendarXML.accountIdtocalender + "/" + CreateCalendarXML.USNAME + "/" + CreateCalendarXML.board + "/" + CreateCalendarXML.medium + "/" + CreateCalendarXML.classs) + ".xml");
			File file =new File ( CreateCalendarXML.macpath + "/" + algo( CreateCalendarXML.accountIdtocalender + "/" + CreateCalendarXML.USNAME + "/" + CreateCalendarXML.board + "/" + CreateCalendarXML.medium + "/" + CreateCalendarXML.classs) + "D.xml");

			StreamResult result = new StreamResult ( file);

			transformer.transform(source,result);


		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}

		try {                         
			fis = new FileInputStream ( CreateCalendarXML.macpath + "/" + algo( CreateCalendarXML.accountIdtocalender + "/" + CreateCalendarXML.USNAME + "/" + CreateCalendarXML.board + "/" + CreateCalendarXML.medium + "/" + CreateCalendarXML.classs) + "D.xml");
			fos = new FileOutputStream ( CreateCalendarXML.macpath + "/" + algo( CreateCalendarXML.accountIdtocalender + "/" + CreateCalendarXML.USNAME + "/" + CreateCalendarXML.board + "/" + CreateCalendarXML.medium + "/" + CreateCalendarXML.classs) + ".xml");

		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		new Rijndael();
		Rijndael.EDR(fis, fos, 1);

		new DeleteFile(CreateCalendarXML.macpath+"/"+algo(CreateCalendarXML.accountIdtocalender+"/"+CreateCalendarXML.USNAME+"/"+CreateCalendarXML.board+"/"+CreateCalendarXML.medium+"/"+CreateCalendarXML.classs)+"D.xml");
	}

	private
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

	private static
    String addDate( String dt)
	{
		SimpleDateFormat sdf = new SimpleDateFormat ( "yy-MM-dd");
		Calendar         c   = Calendar.getInstance();
		try {
			c.setTime(sdf.parse(dt));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		c.add( Calendar.DATE, 1);  // number of days to add
		dt = sdf.format(c.getTime());  // dt is now the new date

		return dt;
	}

}