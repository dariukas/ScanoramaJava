import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Set;


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

	public static void detectSequenceErrors(Map<String, String> titles){
		System.out.println("Checking for sequence errors...");
		Set <String> keys = titles.keySet(); 
		String key1 = "00:00:00,0>00:00:00,0";
		for (String key2 : keys){
			String [] timecodes1 = key1.split(">");
			String [] timecodes2 = key2.split(">");
			if (timecodeToDouble(timecodes2[1])<=timecodeToDouble(timecodes2[0])){
				System.out.println("The issue in the sequence: the beginning is later than the end in "+key2); 
			}
			if (timecodeToDouble(timecodes1[1])>=timecodeToDouble(timecodes2[0])){
				System.out.println("The issue in the sequence: the overlap between "+key1+" and "+key2); 
			}
			key1=key2;
		}
	}
    
    public static double timecodeToDouble (String timecode){
    	String [] timecodes = timecode.split(":");
        if(timecodes.length<3){
        	System.out.println("Something wrong with the format of "+timecode);   	
        }else{
		return (Double.parseDouble(timecodes[0])-1)*3600+Double.parseDouble(timecodes[1])*60
	    		+Double.parseDouble(timecodes[2].replace(",","."));
        }
		return 0;
    }
 
    public static String integerToTimecode (int value){
        int valueHours = value/3600000;
        int valueHr = value%3600000;
        int valueMinutes = valueHr/60000;
        int valueMr = valueHr%60000;
        int valueSeconds = valueMr/1000;
        int valueNanoseconds = valueMr%1000/10;
        
        String timecode = "0"+valueHours;
        if(valueMinutes>9){
        	timecode += ":"+valueMinutes;	
        }
        else{timecode += ":0"+valueMinutes;}
        if(valueSeconds>9){
        	timecode += ":"+valueSeconds;	
        }
        else{timecode += ":0"+valueSeconds;}
        if(valueNanoseconds>9){
        	timecode += ":"+valueNanoseconds;	
        }
        else{timecode += ":0"+valueNanoseconds;}

    	return timecode;
    }
}

