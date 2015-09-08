import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.Map.Entry;

public class MP1 {
    Random generator;
    String userName;
    String inputFileName;
    String delimiters = " \t,;.?!-:@[](){}_*/";
    String[] stopWordsArray = {"i", "me", "my", "myself", "we", "our", "ours", "ourselves", "you", "your", "yours",
            "yourself", "yourselves", "he", "him", "his", "himself", "she", "her", "hers", "herself", "it", "its",
            "itself", "they", "them", "their", "theirs", "themselves", "what", "which", "who", "whom", "this", "that",
            "these", "those", "am", "is", "are", "was", "were", "be", "been", "being", "have", "has", "had", "having",
            "do", "does", "did", "doing", "a", "an", "the", "and", "but", "if", "or", "because", "as", "until", "while",
            "of", "at", "by", "for", "with", "about", "against", "between", "into", "through", "during", "before",
            "after", "above", "below", "to", "from", "up", "down", "in", "out", "on", "off", "over", "under", "again",
            "further", "then", "once", "here", "there", "when", "where", "why", "how", "all", "any", "both", "each",
            "few", "more", "most", "other", "some", "such", "no", "nor", "not", "only", "own", "same", "so", "than",
            "too", "very", "s", "t", "can", "will", "just", "don", "should", "now"};

    void initialRandomGenerator(String seed) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA");
        messageDigest.update(seed.toLowerCase().trim().getBytes());
        byte[] seedMD5 = messageDigest.digest();

        long longSeed = 0;
        for (int i = 0; i < seedMD5.length; i++) {
            longSeed += ((long) seedMD5[i] & 0xffL) << (8 * i);
        }

        this.generator = new Random(longSeed);
    }

    Integer[] getIndexes() throws NoSuchAlgorithmException {
        Integer n = 10000;
        Integer number_of_lines = 50000;
        Integer[] ret = new Integer[n];
        this.initialRandomGenerator(this.userName);
        for (int i = 0; i < n; i++) {
            ret[i] = generator.nextInt(number_of_lines);
        }
        return ret;
    }

    public MP1(String userName, String inputFileName) {
        this.userName = userName;
        this.inputFileName = inputFileName;
    }

    public String[] process() throws Exception {
        
    	// Read all titles from the input into a list
    	List<String> inputTitleList = new ArrayList<String>();

    	URL path = ClassLoader.getSystemResource(this.inputFileName);
    	if (path == null) {
             throw new NullPointerException("Input file not found \n");
        }
    	File f = new File(path.toURI());
    	try (BufferedReader br = new BufferedReader(new FileReader(f))) {
    	    String line = br.readLine();
    	    while (line != null) {
    	    	inputTitleList.add(line);
    	    	line = br.readLine();
    	    }
    	}

    	// Select titles at position determined by getIndexes()
    	List<String> processTitleList = new ArrayList<String>();
    	Integer[] idx = getIndexes();
    	for (int i = 0; i <= idx.length - 1; i++) {
    		String title = inputTitleList.get(idx[i]);
    		title = title.trim();
    		title = title.toLowerCase();
    		processTitleList.add(i, title);
    	}

    	// Parse each title, and build a map of words (keys) and their count (values)
    	Map<String, Integer> map = new HashMap<String, Integer>();
    	// These are the words to filter out
    	List<String> stopWordsList = Arrays.asList(this.stopWordsArray);
    	 
    	for (int i = 0; i <= processTitleList.size() - 1; i++) {
        	// Account for word delimiters 
        	StringTokenizer st = new StringTokenizer(processTitleList.get(i), this.delimiters);
        	while (st.hasMoreTokens()) {
        		String tok = st.nextToken();
        		if (stopWordsList.contains(tok)) {
        			continue;
        		}
        		else {
        			// Add the word (key), and increment it's count (value)
        			if (map.containsKey(tok)) {
        				map.put(tok, map.get(tok) + 1);
        			}
        			else {
        				map.put(tok, 1);
        			}
        		}
        	}
    	}

        // Sort data in the map based on word count 
        Set<Entry<String, Integer>> set = map.entrySet();
        List<Entry<String, Integer>> list = new ArrayList<Entry<String, Integer>>(set);
        Collections.sort( list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare( Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2 )
            {
                return (o2.getValue()).compareTo( o1.getValue() );
            }
        } );

        // Get the top 20 words
        int topn = 20;
        String[] ret = new String[topn];
        int count = 0;
        for (Map.Entry<String, Integer> entry:list) {
        	ret[count++] = entry.getKey();
        	if (count == topn) {
        		break;
        	}
        }

        return ret;
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 1){
            System.out.println("MP1 <User ID>");
        }
        else {
            String userName = args[0];
            String inputFileName = "./input.txt";
            MP1 mp = new MP1(userName, inputFileName);
            String[] topItems = mp.process();
            for (String item: topItems){
                System.out.println(item);
            }
        }
    }
}
