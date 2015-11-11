import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;


public class STL {
	
	public static void mapToSTLFile(Map<String, String> titles, String filepath) throws IOException {
		detectSequenceErrors(titles);
		System.out.println("Puttting the modified titles to the file...");
		BufferedWriter writer = null;
		writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(filepath, true), "utf-8"));
		for (Map.Entry<String, String> entry : titles.entrySet()) {
			
			String [] timecodes = entry.getKey().split(">");
			String [] texts = entry.getValue().split("\\n");
			if(timecodes.length>1){
				String line = from24to25(timecodes[0])+"\u0009\u002c\u0009"+from24to25(timecodes[1])+"\u0009\u002c\u0009";
				line += texts[0].trim();
				for(int i=1; i<texts.length; i++){
                     line +="\u007c"+texts[i].trim();//for adding new lines		
				}
            writer.write(line);
			writer.newLine();
			}
		}
		writer.close();
		System.out.println("The modified titles are in the file " + filepath);	
	}

	//recalculates from 24 fps to 25 fps
	public static String from24to25(String values24){
		double value24d = timecodeToDouble(values24);
		int value25 = (int) (value24d*1000*0.96);
		return integerToTimecode(value25);
	}

	public static boolean detectSequenceErrors(Map<String, String> titles){
		System.out.println("Checking for sequence errors...");
		Set <String> keys = titles.keySet(); 
		String key1 = "00:00:00,0>00:00:00,0";
		boolean sort = false;
		for (String key2 : keys){
			String [] timecodes1 = key1.split(">");
			String [] timecodes2 = key2.split(">");
			if (timecodeToDouble(timecodes2[1])<=timecodeToDouble(timecodes2[0])){
				System.out.println("The issue in the sequence: the beginning is later than the end in "+key2); 
			}
			if (timecodeToDouble(timecodes1[1])>=timecodeToDouble(timecodes2[0])){
				System.out.println("The issue in the sequence: the overlap between "+key1+" and "+key2); 
			}
			if (timecodeToDouble(timecodes1[1])==timecodeToDouble(timecodes2[0])){
				sort = true;
			}
			key1=key2;
		}
		return sort;
	}
	
	public static Map<String, String> correctSequenceErrors(
			Map<String, String> titles) {

		System.out.println("Correcting sequence errors...");
		Map<String, String> addMap = new LinkedHashMap<String, String>();
		Set<String> keys = new LinkedHashSet<String>();
		Set<String> keysOfTitles = titles.keySet();
		keys.addAll(keysOfTitles);

		String key1 = "00:00:00,0>00:00:00,0";
		for (String key2 : keys) {
			String[] timecodes1 = key1.split(">");
			String[] timecodes2 = key2.split(">");
			if (timecodeToDouble(timecodes1[1]) == timecodeToDouble(timecodes2[0])) {
				String[] timecodes20 = timecodes2[0].split(",");
				if (timecodes20.length == 2) {
					int framePartInt = Integer.valueOf(timecodes20[1]);
					String timecode = key2;
					if (framePartInt == 23) {
						System.out.println("The frames could reach out of bound 24 in "
								+ timecodes2[0]);

					} else if (framePartInt > 8) {
						timecode = timecodes20[0] + ","
								+ String.valueOf(framePartInt + 1) + ">"
								+ timecodes2[1];
					} else {
						timecode = timecodes20[0] + ",0"
								+ String.valueOf(framePartInt + 1) + ">"
								+ timecodes2[1];
					}
					String value = titles.get(key2);
					titles.remove(key2);
					addMap.put(timecode, value);
				} else {
					System.out.println("Format error was found in "
							+ timecodes2[0]);
				}
			}
			key1 = key2;
		}
		titles.putAll(addMap);
		Map<String, String> treeMap = new TreeMap<String, String>(titles);
		return new LinkedHashMap<String, String>(treeMap);
	}
	
    public static int timecodeToFrames (String timecode, int fps){
    	int frames = 0;
    	String [] timecodes = timecode.split(":");
        if(timecodes.length<3){
        	System.out.println("Something wrong with the format of "+timecode);   	
        }else{
        	int seconds = Integer.valueOf(timecodes[0])*3600+Integer.valueOf(timecodes[1])*60;
        	String [] timecodes2 = timecodes[2].split(",");
        	if (timecodes2.length != 2) {
        		System.out.println("Format error was found in "
						+ timecodes[2]);
        	} else {
        		seconds += Integer.valueOf(timecodes2[0]);
        		frames = seconds*fps;
        		frames += Integer.valueOf(timecodes2[1]);
        		return frames;	
        	}
        }
		return 0;
    }
    
	public static String framesToTimecode(int frames, int fps) {
		int seconds = frames / fps;
		int framesLeft = frames % fps;

		int valueHours = seconds / 3600;
		int valueMinutes = (seconds % 3600) / 60;
		int valueSeconds = (seconds % 3600) % 60;
		String timecode = valuesToTimecode(valueHours, valueMinutes,
				valueSeconds);
		if (framesLeft > 9) {
			timecode += "," + framesLeft;
		} else {
			timecode += ",0" + framesLeft;
		}
		return timecode;
	}
	
    public static double timecodeToDouble (String timecode){
    	String [] timecodes = timecode.split(":");
        if(timecodes.length<3){
        	System.out.println("Something wrong with the format of "+timecode);   	
        }else{
		return (Double.parseDouble(timecodes[0])-0)*3600+Double.parseDouble(timecodes[1])*60
	    		+Double.parseDouble(timecodes[2].replace(",","."));
        }
		return 0;
    }
    
   public static String doubleToTimecode (double value){ 	
    	int valueHours = (int) (value/3600);
    	double left = value-valueHours*3600;
    	int valueMinutes = (int) (left/60);
    	left = left - valueMinutes*60;
    	int valueSeconds = (int) left;
    	left = left - valueSeconds;
        double valueRest = Math.round(left*100)/100;

		String timecode = valuesToTimecode(valueHours, valueMinutes,
				valueSeconds);
		timecode += String.valueOf(valueRest);
    	timecode.replace(".",",");
    	return timecode;
    }
 
	public static String integerToTimecode(int value) {
		int valueHours = value / 3600000;
		int valueHr = value % 3600000;
		int valueMinutes = valueHr / 60000;
		int valueMr = valueHr % 60000;
		int valueSeconds = valueMr / 1000;
		int valueNanoseconds = valueMr % 1000 / 10;

		String timecode = valuesToTimecode(valueHours, valueMinutes,
				valueSeconds);
		if (valueNanoseconds > 9) {
			timecode += ":" + valueNanoseconds;
		} else {
			timecode += ":0" + valueNanoseconds;
		}
		return timecode;
	}
  
	//the method used in many conversions to timecodes, it just fractioned to ensure the zero behind
	public static String valuesToTimecode(int valueHours, int valueMinutes,
			int valueSeconds) {
		String timecode = "0" + valueHours;
		if (valueMinutes > 9) {
			timecode += ":" + valueMinutes;
		} else {
			timecode += ":0" + valueMinutes;
		}
		if (valueSeconds > 9) {
			timecode += ":" + valueSeconds;
		} else {
			timecode += ":0" + valueSeconds;
		}
		return timecode;
	}  
}

