import java.awt.Color;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hslf.model.Fill;
import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.model.SlideMaster;
import org.apache.poi.hslf.model.TextBox;
import org.apache.poi.hslf.usermodel.RichTextRun;
import org.apache.poi.hslf.usermodel.SlideShow;

public class Main {

	/**
	 * @param args
	 */

	public static void main(String[] args) {
		FileController.main();
		try {
			//runNOUVELLE_AMIE();
			//run();
			//FrmPowerpoint.run();
		    //run2015();
			//runReformat();
			//docRun();
			runTest();
		} catch (IOException e) {
			System.out.println("Nerastas failas!");
		}
	}
	
	private static void runTest() throws IOException {
		//ToPowerpoint.timecodeFormatter("2 00:02:44:03 00:02:48:20 labukas");
        //String line = "";
        //TextManipulation.printUnicodes(line);
		String text = "01234567890 ir 123456789 123456789?123456789)123456789 123456789";
		//FrmPowerpoint.wrapping1206(text);
		List<String> texts = new ArrayList<String>();
		texts.add(text);		
		System.out.println(TextManipulation.wrapSlideText(texts));
	}

	private static void run() throws IOException {
		String fileName = "MeistrasIrTatjana_RU";
		String fileNameBase = "/Users/kristinaslekyte/Desktop/Scanorama/";
		File fin = new File(fileNameBase + fileName + ".txt");
		List<String> texts = ToPowerpoint.filesRead1(fin);
		texts = TextManipulation.avoidingThreeLines(texts);
		ToPowerpoint.putToPowerPointFile(texts, fileName);
		System.out.println("DONE! :-)");
	}

	private static void runNOUVELLE_AMIE() throws IOException {
		// File dir = new File(".");
		// File fin = new File(dir.getCanonicalPath() + File.separator +
		// "a.rtf");

		//String fileNameBase = "/Users/kristinaslekyte/Desktop/Scanorama/NOUVELLE_AMIE_";
		//String fileNameEnd = "AB_ANG-1-1.txt";
		String fileNameBase = "/Users/kristinaslekyte/Desktop/Scanorama/NOUVELLE_AMIE_0";
		String fileNameEnd = ".txt";
		List<String> texts = new ArrayList<String>();
		for (int i = 1; i < 7; i++) {
			String fileName = fileNameBase + i + fileNameEnd;
			File fin = new File(fileName);
			List<String> texts0 = ToPowerpoint.filesRead(fin);
			System.out.println("Done: " + fileName);
			texts = ToPowerpoint.combineLists(texts, texts0);
		}
		ToPowerpoint.putToPowerPointFile(texts, "NOUVELLE_AMIE");
	}	
	
	static void docRun()throws IOException{
		String fileTitle = "PriesGamta";
		String fileNameBase = "/Users/kristinaslekyte/Desktop/Scanorama/";
		String fileNameEnd = ".doc";
		String fileNameIn = fileNameBase + fileTitle + fileNameEnd;
		String fileNameOut = fileNameBase + fileTitle + "_mod" + fileNameEnd;
		ManipulateDoc.main(fileNameIn, fileNameOut);
	}
	
	//reformats the titles to the main format or to stl
	static void runReformat()throws IOException{
		String fileTitle = "14plus";
		String fileNameBase = "/Users/kristinaslekyte/Desktop/Scanorama/";
		String fileNameEnd = ".txt";
		String fileNameIn = fileNameBase + fileTitle + fileNameEnd;
		String fileNameOut = fileNameBase + fileTitle + "_mod" + fileNameEnd;

		File fin = new File(fileNameIn);
		
		FileController.main();
		
        boolean sort = STL.detectSequenceErrors(ToPowerpoint.titlesToMap(fin));	
		if(sort == true){
			ToPowerpoint.mapToFile(STL.correctSequenceErrors(ToPowerpoint.titlesToMap(fin)), fileNameOut);
		} else {
			ToPowerpoint.mapToFile(ToPowerpoint.titlesToMap(fin), fileNameOut);
		}
		//STL.mapToSTLFile(ToPowerpoint.titlesToMap(fin), fileNameOut);		
	}
	
	//reformats the titles to the main format or to stl
	static void runReformat(String path)throws IOException{
		String fileNameIn = path;
		String fileNameOut = path.replace(".txt", "_mod.txt");

		File fin = new File(fileNameIn);
		
		FileController.main();
		
        boolean sort = STL.detectSequenceErrors(ToPowerpoint.titlesToMap(fin));	
		if(sort == true){
			ToPowerpoint.mapToFile(STL.correctSequenceErrors(ToPowerpoint.titlesToMap(fin)), fileNameOut);
		} else {
			ToPowerpoint.mapToFile(ToPowerpoint.titlesToMap(fin), fileNameOut);
		}		
	}

	static void run2015() throws IOException{	
        String fileTitle = "Atleidimas";
		String fileNameBase = "/Users/kristinaslekyte/Desktop/Scanorama/";
		String fileNameIn = fileNameBase + fileTitle + ".txt";
		String fileNameOut = fileNameBase + fileTitle +".ppt";
		
		File fin = new File(fileNameIn);
		List<String> texts = ToPowerpoint.filesRead2015(fin);
		//texts = TextManipulation.wrapSlideText(texts);
		SlideShow pptAfter = ToPowerpoint.writeToPowerPoint(texts);
		FrmPowerpoint.writingText(pptAfter, fileNameOut);
		
		//FileChooser: https://docs.oracle.com/javase/7/docs/api/javax/swing/JFileChooser.html
		//Thread kelis: vienas skaito .txt, kitas jau rašo į slide
	}
}
