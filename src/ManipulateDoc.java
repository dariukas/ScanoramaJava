import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.xwpf.usermodel.Document;


public class ManipulateDoc {
	
	public static void main (String fileNameIn, String fileNameOut) throws IOException {
		HWPFDocument doc = getFromFile(fileNameIn);
		//String text = doc.getDocumentText();
		modify(doc);
		writeToFile(fileNameOut, doc);
	}
	
	public static HWPFDocument getFromFile(String fileNameIn) throws FileNotFoundException, IOException {
		System.out.println("Reading .doc file, and putting its titles to the list...");
		File fin = new File(fileNameIn);
		FileInputStream fis = new FileInputStream(fin);
		fis.close();
		HWPFDocument doc = new HWPFDocument(fis);
		return doc;
	}

	public static void writeToFile (String fileNameOut, HWPFDocument doc) throws IOException{	
		System.out.println("Writing the modified .doc file in "+fileNameOut+" ...");
		File fon = new File(fileNameOut);
		FileOutputStream fos = new FileOutputStream(fon);
		fos.close();
		doc.write(fos);		
	}
	
	//modification of the texts
	public static void modify (HWPFDocument doc){
		Range r = doc .getRange();
		System.out.println("Modifying texts...");
		String line = "";
		for(int i = 0; i<r.numParagraphs(); i++)
		{
			Paragraph p = r.getParagraph(i);
			line = p.text();
			removeNumeration (p, line);
			removeItalic(p, line);
		}
	}

	//delete the numeration, years in the text can be affected
	public static void removeNumeration (Paragraph paragraph, String line){	
		Matcher numberMatcher = Pattern.compile("[0-9]{4}").matcher(line);
		if (numberMatcher.find()){
			line = line.replaceAll("[0-9]{4}\\u0020", "");
			paragraph.replaceText(line, true);
		}
	}


	public static void removeItalic (Paragraph paragraph, String line){	
		for(int j = 0; j<paragraph.numCharacterRuns(); j++)
		{
			CharacterRun cr = paragraph.getCharacterRun(j);
			//modify italics
			if (cr.isItalic()){
				//\r is for \\u000d
				line="\n<i>"+line.replaceAll("\r", "</i>\r");
				cr.setItalic(false);
				paragraph.replaceText(line, true);
				//p.replaceText("\r", "</i>\r");
				break;
			}
		}	
	}

	//print texts of ranges
	public void printRange (Range r){
		for(int i = 0; i<r.numCharacterRuns(); i++)
		{
			CharacterRun cr = r.getCharacterRun(i);
			System.out.println(i+": "+cr.text());
		}
	}
		
	/*
	for(int i = 0; i<r.numCharacterRuns(); i++)
	{
		CharacterRun cr = r.getCharacterRun(i);
		line = cr.text();
		
		//modify italics
		if (cr.isItalic()){
			cr.replaceText("<i>"+line, true);
         while(cr.isItalic()){
        	 i++;
        	 cr = r.getCharacterRun(i);	 
         }
         r.getCharacterRun(i-1).replaceText(r.getCharacterRun(i-1).text().trim()+"</i>", true);
		//System.out.println(i+": "+cr.text());
		}
	}
*/

}
