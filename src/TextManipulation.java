import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TextManipulation {

	static List<String> wrapSlideText(List<String> textsBefore) {
		System.out.println("Wrapping text...");
		List<String> textsAfter = new ArrayList<String>();
		for (String text : textsBefore) {	
			//if(text.contains("popierius"))
			text = corrections(text);
			
			List<String> textsException = dangerDialogues(text);
			
			if(textsException==null){
	
			if (text.length() >= 58) {
				textsAfter.addAll(wrapText(text));
			} else {
				textsAfter.add(text.trim());
			}
			
			}else{
				textsAfter.addAll(dangerDialogues(text));
			}
			
		}
		return textsAfter;
	}

	static List<String> wrapText(String text){
		System.out.println(text);
		List<String> texts = new ArrayList<String>();
		String sub="";
		int maxLength = 58;
		int length = text.length();
		int number = length/maxLength+1;
		int length0 = length/number;
		System.out.println("length:"+length+"ir"+length0);
		//alternative 
		//length0 = maxLength;
		//int reserve = maxLength-length0;
		
		int start = 0;
		int end = length0-1;
		while(end<length){
			end = findSplittingPoint(text.substring(start, end))+start;
			//to avoid split between three points or just before the point
			char firstCh = text.charAt(end);
			while(firstCh=='.' || firstCh=='?' || firstCh=='!'){
				end = end+1;//hope it would be less than reserve
				if((end-start)>=maxLength){
					System.out.println("An issue occured while splitting at "+end+" in "+text);
				}
				firstCh = text.charAt(end);
				}
			sub = text.substring(start, end);
			texts.add(sub.trim());
			start=end;
			end=start+length0;
		}
		texts.add(text.substring(start, length-1).trim());
		return texts;
	}
	
	static private int findSplittingPoint (String text) {
		//three points included since it takes the last one
		//.|?|!
        Matcher m1 = Pattern.compile("[\\u002e|\\u003f|\\u0021]").matcher(text);
		if(m1.find()){
			return m1.end();
			}
		//,|-|;|)|–
		//:-u3a
        Matcher m2 = Pattern.compile("[\\u002c|\\u002d|\\u003b|\\u0029|\\u2013]").matcher(text);
		if(m2.find()){
			System.out.println("Found");
            return m2.end();
		}
		
        Matcher m3 = Pattern.compile("( ir )|( ar )").matcher(text);
		if(m3.find()){
            return m3.start();
		}
		 
		Matcher m4 = Pattern.compile(" ").matcher(text);
		if(m4.find()){
            return m4.start();
		}
		System.out.println("Splitting point not found!");
		return text.length()-1;
	}
			
	static void printUnicodes(String text) {
		char[] chars = text.toCharArray();
		for (char cha : chars)
			System.out.print(cha+"- "+"\\u" + Integer.toHexString(cha)+"\n");
	}

	//find if some dialogues make three lines 
	//coz their first line is longer
	static private List<String> dangerDialogues(String text) {
		//find the beginnings with newline+"-"
		Matcher m1 = Pattern.compile("\\u000A\\u002D").matcher(text);
		if (m1.find()) {
			String sub = text.substring(0, m1.start());
			if(sub.length()>29){
				List<String> texts = new ArrayList<String>();
				texts.add(sub);
				texts.add(text.substring(m1.start()).trim());
				return texts;
			}		
		}
		return null;	
	}
	
	
	static private String corrections(String text) {

		//find new lines \\u000A or "$" (not \\u000C)
		Matcher m1 = Pattern.compile("\\u000A[^\u002D]").matcher(text);	
		if (m1.find()) {
			//System.out.println("\nText: " + text);
			text = text.replaceAll("\\u000A", "\u0020");
			//printUnicodes(text);
		}
		
		//find double spaces \\u0020
		Matcher m2 = Pattern.compile("\\u0020\\u0020").matcher(text);	
		if (m2.find()) {
			text = text.replaceAll("\u0020\u0020", "\u0020");
		}	
		return text;
	}

	static void wrapping1206 (String text) throws IOException{
		int maxLength = 58;
		//int splittingPoint = text.lastIndexOf("\s", maxLength);

		Matcher m = Pattern.compile(".*(\\.|\\?|!|,|;|\\-|:|\\))").matcher(text);
		//Matcher m = Pattern.compile(".*[!,;]").matcher(text);
		m.find();
		int splittingPoint = m.start(1);
		
		System.out.println("Split point: "+splittingPoint);
		if (splittingPoint<1){
			splittingPoint = text.lastIndexOf(" ", maxLength);
		}
		if (splittingPoint<1) {
			splittingPoint = maxLength;
		}
		System.out.println ("soriukas"+splittingPoint);
		System.out.println (text.substring(54, 60));
		System.out.println (text.length());
	}

	static void wrapping1106 (String text) throws IOException{
					
		List<String> arrayOfLines = new ArrayList<String>();
		
		String text1 = null;
		text = text.trim();
		int length = text.length();
		System.out.println(length);
		int maxLength = 58;
		int splittingIndex = -1;
		
		while (length >= maxLength) {
			
			int endPoint = length-2;
	
			splittingIndex = text.lastIndexOf('.', endPoint);
			// tritaskiu isvengti ir sauktuko su taskais, gal regex
			
			if ((length - splittingIndex)>maxLength) {
				splittingIndex = -1;
			}
			
			if (splittingIndex < 0) {
				splittingIndex = text.lastIndexOf('!', endPoint);
			}
			
			if ((length - splittingIndex)>maxLength) {
				splittingIndex = -1;
			}
			
			if (splittingIndex < 0) {
				splittingIndex = text.lastIndexOf('?', endPoint);
			}
			
			if ((length - splittingIndex)>maxLength) {
				splittingIndex = -1;
			}
			
			if (splittingIndex < 0) {
				splittingIndex = text.lastIndexOf(';', endPoint);
			}
			
			if ((length - splittingIndex)>maxLength) {
				splittingIndex = -1;
			}
			
			if (splittingIndex < 0) {
				splittingIndex = text.lastIndexOf(',', endPoint);
			}
			
			if ((length - splittingIndex)>maxLength) {
				splittingIndex = -1;
			}
			
			if (splittingIndex < 0) {
				splittingIndex = text.lastIndexOf(':', endPoint);
			}
			
			if ((length - splittingIndex)>maxLength) {
				splittingIndex = -1;
			}
			
			if (splittingIndex < 0) {
				splittingIndex = text.lastIndexOf('-', endPoint);
			}
			
			if ((length - splittingIndex)>maxLength) {
				splittingIndex = -1;
			}
			
			if (splittingIndex < 0) {
				splittingIndex = text.lastIndexOf("ir", endPoint);
			}
			
			if ((length - splittingIndex)>maxLength) {
				splittingIndex = -1;
			}
			
			if (splittingIndex < 0) {
				splittingIndex = text.lastIndexOf(" ", endPoint/2);
				System.out.println("No semantic separations!");				
			}
			
			if ((length - splittingIndex)>maxLength) {
				splittingIndex = -1;
			}
			
			if (splittingIndex < 0) {
				splittingIndex = text.lastIndexOf(" ", endPoint/4);
				System.out.println("No semantic separations!");				
			}
			
			System.out.println("Splitting Index: " + splittingIndex);
			
			text1 = text.substring (splittingIndex+1);
			System.out.println ("Passing "+text1);
			arrayOfLines.add(text1);
			text = text.substring(0, splittingIndex+1).trim();
			
		    length = text.length();
		}
				
		System.out.println (text);
		//int a1 = rt.getEndIndex();
		 arrayOfLines.add(text);
		 Collections.reverse(arrayOfLines);
		 System.out.println (arrayOfLines);
		 //return arrayOfLines; 		 
}
	
	public static List<String> avoidingThreeLines(List<String> texts) {
		// sometimes it could work with the anchor inside .ppt but this method is simpler
		List<String> textsEd = new ArrayList<String>();
		for (String text : texts) {

			String text1 = null;
			int i = 0;
			while (text.length() > 58) {
				i = 58;
				while (text.charAt(i) == ',' || text.charAt(i) == '.') {
					text1 = text.substring(0, i - 1);
					text = text.substring(i, text.length() - 1).trim();
					textsEd.add(text1);
					i = i - 1;
				}
			}
			textsEd.add(text);
		}
		return textsEd;
	}
	
	private static List<String> avoidingThreeLines1(List<String> texts) {
		// sometimes it could work with the anchor inside .ppt but this method is simpler
		List<String> textsEd = new ArrayList<String>();
		for (String text : texts) {

			String text1 = null;
			int length = text.length();
			int maxLength = 58;
			
			while (length>=maxLength) {
			
	
			int pointIndex = text.indexOf ('.');
			// tritaskiu isvengti ir sauktuko su tasku, gal regex
			
			if (pointIndex>0){
				text1.substring(0, pointIndex);
				textsEd.add (text1);
				break;
			}
			}
			textsEd.add(text);
		}
		return textsEd;
	}
}