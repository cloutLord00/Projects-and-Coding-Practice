package frequency;
import utils.In;
import utils.Stopwatch;
/**
 *
 * @author UMD CS
 */
public class WordFrequency {
	/**
	 * @param  input source
	 * @param 
	 * 	This method receives input resource and return the word frequency string
	 *  DO NOT CHANGE THIS METHOD. 
	 */
	public static String freq(String inputSource, int maxLines){
		In in;
		// Frequency class can only count the frequency of strings. DO NOT CHANGE THIS.
        Frequency<String> freq = new Frequency();
        
        int MAX = 100;
        String inputSourceName = inputSource;
        try {
            in = new In(inputSourceName);
            while (!in.isEmpty()) {
                String s = in.readLine();
                //System.out.println(s);
                freq.insertWords(s);    
            }
        }
        catch (Exception e) { 
            e.printStackTrace();
        }
        
        StringBuilder strFrequency = new StringBuilder();
        int cnt = 0;
        for(String s: freq){
            strFrequency.append(s);
            strFrequency.append(",");
            cnt++;
            if(cnt >= maxLines){break;}
        }
        //System.out.println(strFrequency);
        return strFrequency.toString();
	}
	
	
    /**
     *	
     */
    public static void main(String[] args) {
        In in;
        Frequency<String> freq = new Frequency();
        int MAX = 100;
       // String inputSourceName = "http://www.cs.umd.edu/class/summer2015/cmsc132/projects/P3_WordFrequency/test1.html";
        String inputSourceName = "einstein.txt";
        
        // read one line at a time from URL
        System.out.println("read from " + inputSourceName);
        System.out.println("---------------------------------------------------------------------------");
        Stopwatch sw = new Stopwatch();
        try {
            in = new In(inputSourceName);
            while (!in.isEmpty()) {
                String s = in.readLine();
                //System.out.println(s);
                freq.insertWords(s);    
            }
        }
        catch (Exception e) { 
            e.printStackTrace();
        }
        System.out.println("Elapsed time:" + sw.elapsedTime());
        
        int cnt = 0;
        for(String s: freq){
            System.out.println(s);
            cnt++;
            if(cnt >= MAX){break;}
        }
    	
    	
    }
}
