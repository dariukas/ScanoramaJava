import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hslf.HSLFSlideShow;
import org.apache.poi.hslf.model.Shape;
import org.apache.poi.hslf.model.TextBox;
import org.apache.poi.hslf.model.TextRun;
import org.apache.poi.hslf.model.TitleMaster;
import org.apache.poi.hslf.record.Slide;
import org.apache.poi.hslf.usermodel.RichTextRun;
import org.apache.poi.hslf.usermodel.SlideShow;


//control+space - give the suggestions 

public class FrmPowerpoint {
	
	static void run () throws IOException {
		
		//String fileNameIn = "/Users/kristinaslekyte/Desktop/Scanorama/AntTavoRanku1.ppt";
		//String fileNameOut = "/Users/kristinaslekyte/Desktop/Scanorama/AntTavoRanku.ppt";
		//reading (fileNameIn, fileNameOut);
		//String fileNameIn = "/Users/kristinaslekyte/Desktop/Scanorama/Rosita_ed.ppt";
		//String fileNameOut = "/Users/kristinaslekyte/Desktop/Scanorama/Rosita_ed1.ppt";
		String fileNameIn = "/Users/kristinaslekyte/Desktop/Scanorama/AuksoKrantas.ppt";
		String fileNameOut = "/Users/kristinaslekyte/Desktop/Scanorama/AuksoKrantas_ed.ppt";
		
		SlideShow ppt = new SlideShow(new HSLFSlideShow(fileNameIn));		
		/*String text = gettingTextFromSlide(ppt, 1);
		wrapping1106 (text);*/
		ppt = fromPptToPpt(ppt);
		writingText(ppt, fileNameOut);	
	}
		
	//reading ppt
	static void reading (String fileNameIn, String fileNameOut) throws IOException {
		
		SlideShow ppt = new SlideShow(new HSLFSlideShow(fileNameIn));
		
		//TitleMaster tm = ppt.getTitleMasters()[1];
		//Slide[] slide = ppt.getSlides();
		  //for (int i = 0; i < slide.length; i++){
		    //Shape[] sh = slide[i].getShapes();

		org.apache.poi.hslf.model.Slide[] slides = ppt.getSlides();
		for (int i=1; i<slides.length; i=i+2){	
		TextRun tr = slides[i].getTextRuns()[0];	
		RichTextRun rt = tr.getRichTextRuns()[0];
		rt.setFontColor(Color.yellow.brighter());	
		}		
		writingText(ppt, fileNameOut);	
	}
	
	//getting text from one slide
	static String gettingTextFromSlide (SlideShow ppt, int slideNumber) throws IOException {
		org.apache.poi.hslf.model.Slide slide = ppt.getSlides()[slideNumber];
		 //TextRun[] tr = slide.getTextRuns();
		TextRun tr = slide.getTextRuns()[0];	
		RichTextRun rt = tr.getRichTextRuns()[0];
		String text = rt.getText();
		System.out.println(text);
		return text;
	}
	
	//getting text from all slides
	static List<String> gettingTextFromSlides (SlideShow ppt) throws IOException {
		List<String> textOfSlides = new ArrayList<String>();
        org.apache.poi.hslf.model.Slide[] slides = ppt.getSlides();
		for (int i = 1; i < slides.length; i=i+2){
			 String text = slides[i].getTextRuns()[0].getRichTextRuns()[0].getText();
		     textOfSlides.add(text);
		}	
		//System.out.println(textOfSlides);
		System.out.println("reading from the file finished.");
		return textOfSlides;	
	}
	
	//writing ppt to the file
	static void writingText(SlideShow ppt, String fileNameOut) throws IOException {
		FileOutputStream out = new FileOutputStream(fileNameOut);
		ppt.write(out);
		out.close();
		System.out.println (fileNameOut+" was saved!");
	}
	
	//puts the text from list to ppt
	static SlideShow fromListToPpt(List<String> texts) throws IOException{
		texts = TextManipulation.wrapSlideText(texts);
		SlideShow pptAfter = ToPowerpoint.writeToPowerPoint(texts);
	    return pptAfter;
}
	
		
	//puts the text from one to another ppt
	static SlideShow fromPptToPpt(SlideShow pptBefore) throws IOException{
			List<String> texts = gettingTextFromSlides(pptBefore);
			texts = TextManipulation.wrapSlideText(texts);
			SlideShow pptAfter = ToPowerpoint.writeToPowerPoint(texts);
		    return pptAfter;
	}
}
