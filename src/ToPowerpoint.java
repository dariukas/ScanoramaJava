import java.awt.Color;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hslf.model.Fill;
import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.model.SlideMaster;
import org.apache.poi.hslf.model.TextBox;
import org.apache.poi.hslf.model.TextRun;
import org.apache.poi.hslf.usermodel.RichTextRun;
import org.apache.poi.hslf.usermodel.SlideShow;


public class ToPowerpoint {
	
	public static SlideShow writeToPowerPoint(List<String> texts)
			throws IOException {
		SlideShow ppt = new SlideShow();
		backgroundToBlack(ppt);
		for (String text : texts) {
			// adding the empty slide
			Slide s0 = ppt.createSlide();
			Slide s = ppt.createSlide();
			TextBox title = s.addTitle();
			RichTextRun rt = title.getTextRun().getRichTextRuns()[0];
			rt.setFontColor(Color.white);
			rt.setFontName("Arial");
			rt.setFontSize(44);

			Rectangle anchorInTheSlide = new java.awt.Rectangle(5, 5, 710, 125);
			title.setAnchor(anchorInTheSlide);

			// italic text
			if (text.contains("<i>")) {
				String[] texts0 = text.split("<i>");
				// texts0.length;
				// text = text.replace("<i>", "");
				
				title.setText("");
				TextRun run = title.getTextRun();
				//run.setText("");
				for (String text0 : texts0) {
					if (text0.contains("</i>")) {
						text0 = text0.replace("</i>", "");
						RichTextRun rtr2 = run.appendText(text0);
						rtr2.setItalic(true);
					} else {
						//run.appendText(text0).setItalic(false);
						//rt.setItalic(false);
						RichTextRun rtr2 = run.appendText(text0);
						rtr2.setItalic(true);
					}
				}
				System.out.println(run.getText());
				title.setText(run.getText());
				rt.setItalic(true);
			} else {
				title.setText(text);
			}
		}
		return ppt;
	}
	
	public static List<String> combineLists(List<String> list1,
			List<String> list2) {
		List<String> list = list1;
		for (String frmList2 : list2) {
			list.add(frmList2);
		}
		return list;
	}

	// http://poi.apache.org/slideshow/how-to-shapes.html
	public static void putToPowerPointFile(List<String> texts, String fileOutName)
			throws IOException {
		SlideShow ppt = new SlideShow();
		// change the color of the background of the slide
		backgroundToBlack(ppt);

		for (String text : texts) {
			// adding the empty slide
			Slide s0 = ppt.createSlide();

			Slide s = ppt.createSlide();
			TextBox title = s.addTitle();
			// change the color of the text
			RichTextRun rt = title.getTextRun().getRichTextRuns()[0];
			rt.setFontColor(Color.white);

			if (text.startsWith("#")) {
				text = text.replace("#", "");
				rt.setItalic(true);
			}

			Rectangle anchorInTheSlide = new java.awt.Rectangle(5, 5, 710, 125);
			// rect.setLocation(5, 0);
			// rect.setSize(710, 125);
			title.setAnchor(anchorInTheSlide);
			title.setText(text);
		}

		// save changes in a file
		FileOutputStream out = new FileOutputStream(
				"/Users/kristinaslekyte/Desktop/Scanorama/" + fileOutName
						+ ".ppt");
		ppt.write(out);
		out.close();
	}

	private static void backgroundToBlack(SlideShow ppt) {

		SlideMaster master = ppt.getSlidesMasters()[0];
		Fill fill = master.getBackground().getFill();
		fill.setForegroundColor(Color.black);
		fill.setFillType(Fill.FILL_SOLID);

	}
	
	private static String removeI(String text) {
        text = text.replaceAll("<i>", "");
        return text;
		}
	

	public static List<String> filesRead(File fin) throws IOException {
		FileInputStream fis = new FileInputStream(fin);
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		List<String> texts = new ArrayList<String>();
		String line = null;
		String text = "";
		// a checks if it is a long digital
		boolean a = true;
		// b is trigger
		boolean b = false;

		while ((line = br.readLine()) != null) {
			line = line.trim();
			a = (!line.isEmpty() && Character.isDigit(line.charAt(0)) && line
					.length() > 24);
			// possible bug is if it is a text line starting with a digit (e. g.
			// hours), and very long text ()

			if (b == true && b != a) {
				if (line.isEmpty()) {
					text = removeI(text);
					texts.add(text.trim());
					System.out.println("Tekstas:" + text);
					text = "";
				}
				text = text + line + "\r\n";
			}
			// to start the text of the subtitles
			if (a == true) {
				b = true;
			}

		}
		br.close();
		return texts;
	}
	
	public static List<String> filesRead1(File fin) throws IOException {
		FileInputStream fis = new FileInputStream(fin);
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		List<String> texts = new ArrayList<String>();
		String line = null;
		String text = "";
		int i = 0;

		while ((line = br.readLine()) != null) {
			line = line.trim();

			if (line.isEmpty()) {
				if (!text.trim().isEmpty()) {
					texts.add(text.trim());
					i++;
					System.out.println("Tekstas:" + i + text);
					text = "";

				}
			} else {
				text = text + line + "\r\n";
			}

		}
		br.close();
		return texts;
}
	
	public static void mapToFile(Map<String, String> titles, String filepath) throws IOException {
		System.out.println("Puttting the modified titles to the file...");
		BufferedWriter writer = null;
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(filepath, true), "utf-8"));
				
		for (Map.Entry<String, String> entry : titles.entrySet()) {
			writer.write(entry.getKey());
			writer.newLine();
			writer.write(entry.getValue());
			writer.newLine();
			writer.newLine();
		}	
		writer.close();
		System.out.println("The modified titles are in the file " + filepath);
	}

	public static Map<String, String> titlesToMap(File fin) throws IOException {
		FileInputStream fis = new FileInputStream(fin);
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		Map<String, String> texts = new LinkedHashMap<String, String>();
		String line = null;
		String timecode = "";
		String text = "";
		System.out.println("Reading the titles from the file and modifying them...");
		while ((line = br.readLine()) != null) {
		/*
		 for (int i=0; i<5; i++){
			line = br.readLine();
			TextManipulation.printUnicodes(line);
		*/
			//System.out.println(line);
			Matcher numberMatcher = Pattern.compile("^[0-9]{1,4}+").matcher(line);
			//possible problem if the title starts with the year
			if (numberMatcher.find()){
			line = timecodeFormatter(line);
			}
			
			//lietuvisku simboliu ir to bruksnio filtras
			
			//{timestamp}filtras
			Matcher bracketMatcher = Pattern.compile("\\u007b|\\u007d").matcher(line);
			if (bracketMatcher.find()){
				System.out.println("Brackets detected in "+line);
			}
			
			Matcher timeStampMatcher = Pattern.compile("\\|").matcher(line);
			if (timeStampMatcher.find()){
				texts.put(timecode.trim(), text.trim());
				timecode=line.substring(0, timeStampMatcher.end()-1);
				text = line.substring(timeStampMatcher.end()).trim()+"\n";			
				
			} else{
				text +=line+"\n";
				//text +=line;	
			}
			}
		//add the final title
		texts.put(timecode.trim(), text.trim());
		br.close();
		//remove the first empty item
		texts.remove("");
		return texts;	
	}
	
	public static String timecodeFormatter(String line){
		String number = "";
		Matcher numberMatcher = Pattern.compile("^[0-9]+\\u003a?\\u0020+\\u003e?[0-9]").matcher(line);
		if (numberMatcher.find()){
		number = line.substring(numberMatcher.start(), numberMatcher.end()).trim();
		line = line.substring(numberMatcher.end()-1);
		}
	
		String timecodeAfter = "";
		Matcher timecodeMatcher = Pattern.compile("[0-9]{2,3}").matcher(line);
	    if (timecodeMatcher.find()){
			timecodeAfter = timecodeMatcher.group();
		}
	    for(int i=0; i<2; i++){
	    	if (timecodeMatcher.find()){
	    	timecodeAfter += ":"+timecodeMatcher.group();
	    	}
			}
	    if (timecodeMatcher.find()){
	    	timecodeAfter += ","+timecodeMatcher.group()+">";
	    	}
	    for(int i=0; i<2; i++){
	    	if (timecodeMatcher.find()){
	    	timecodeAfter += timecodeMatcher.group()+":";
	    	}
			}
	    if (timecodeMatcher.find()){
	    	timecodeAfter += timecodeMatcher.group()+",";
	    	}
	    if (timecodeMatcher.find()){
	    	timecodeAfter += timecodeMatcher.group();
	    	//separate from the title text
			return timecodeAfter+"|"+line.substring(timecodeMatcher.end()).trim();
	    	}   
	    //its already checked for timecodes, so, it covers 10 metu, 10ieji, 2015 m, 2015ieji etc.
	    Matcher checkIfNumber= Pattern.compile("[a-zA-Z]").matcher(line);
		if (checkIfNumber.find()){
			return line;
		}
	number = line;	
	return "";
	//number and then text in the same line as well as just plain years as the text not solved
	}
	
public static List<String> filesRead2015(File fin) throws IOException {
	System.out.println("Reading .txt file, and putting its titles to the list...");
	FileInputStream fis = new FileInputStream(fin);
	BufferedReader br = new BufferedReader(new InputStreamReader(fis));
	List<String> texts = new ArrayList<String>();
	String line = null;
	String text = "";
	
	while ((line = br.readLine()) != null) {
	/*for (int i=0; i<5; i++){
		line = br.readLine();
		TextManipulation.printUnicodes(line);*/
		
		 //ManoSesute, PublicAffair
		//Matcher mDigit = Pattern.compile("^[0-9]").matcher(line);
		
		//OtherGirls	00:00:10:05
		//String timeStamp="[0-9]{2}+:[0-9]{2}+:[0-9]{2}+:[0-9]{2}+";
		//ManoMama, CracheCoeur :20[tab]00:00:10:05
		//String timeStamp=":[0-9]{2}+\\u0009[0-9]{2}+:[0-9]{2}+:[0-9]{2}+:[0-9]{2}+";
		//PriesGamta
		//String timeStamp="0[0-9]{3}";
		//SauliausSunus, JaunojoEdoBedos, Banga, Bitlai	:21[space]01:00:35:21
		//String timeStamp = ":[0-9]{2}+\\u0020[0-9]{2}+:[0-9]{2}+:[0-9]{2}+:[0-9]{2}+"; 
	    //ManoSesute, PublicAffair ,640 --> 00:01:09,640
		//String timeStamp = "\\u003e\\u0020[0-9]{2}+:[0-9]{2}+:[0-9]{2}+\\u002c[0-9]{3}+";
		//Dypanas, MonRoi, LesCowboys	.09  01:02:32.11
	    //String timeStamp = "\\u002e[0-9]{2}+\\u0020\\u0020[0-9]{2}+:[0-9]{2}+:[0-9]{2}+\\u002e[0-9]{2}+";
		//Atleidimas .01 <01:01:03.13
		String timeStamp="\\u003c[0-9]{2}+:[0-9]{2}+:[0-9]{2}+\\u002e[0-9]{2}+";
		
		Matcher timeStampMatcher = Pattern.compile(timeStamp).matcher(line);
		if (timeStampMatcher.find()){
			texts.add(text.trim());
			text = line.substring(timeStampMatcher.end()).trim()+"\n";
			//text="";			
			}	
		 //ManoSesute, PublicAffair
		/*else if (mDigit.find()) {	
		}*/
		else{
			text +=line+"\n";
			//text +=line;	
		}
		}
	texts.add(text.trim());
	br.close();
	texts.remove(0);
	//System.out.println(texts);
	return texts;
}

//delete this
public static String filterTextFromTimeStamp(String line){
	
//Matcher m = Pattern.compile("[A-Za-z]|\\u002D|[<]").matcher(line);
	//timestamp with spaces \\u0020
	String timeStamp1 = ":\\u0020[0-9]{2}+\\u0020[0-9]{2}+:\\u0020[0-9]{2}+:\\u0020[0-9]{2}+:\\u0020[0-9]{2}+";
	//timestamp without spaces
	String timeStamp2 = ":[0-9]{2}+\\u0020[0-9]{2}+:[0-9]{2}+:[0-9]{2}+:[0-9]{2}+"; 
	//timeStamp2="\\u003e\\u0020[0-9]{2}+:[0-9]{2}+:[0-9]{2}+\\u002c[0-9]{3}+";
	timeStamp2=":[0-9]{2}+\\u0009[0-9]{2}+:[0-9]{2}+:[0-9]{2}+:[0-9]{2}+";
	Matcher timeStampMatcher1 = Pattern.compile(timeStamp1).matcher(line);
	Matcher timeStampMatcher2 = Pattern.compile(timeStamp2).matcher(line);
	if (timeStampMatcher1.find()){
	int position = timeStampMatcher1.end();
    line = line.substring(position);
	} else if (timeStampMatcher2.find()){
		int position = timeStampMatcher2.end();
		 line = line.substring(position);
	//System.out.println(position);
}
/*else {
	line="";
}*/
return line;
}

}
