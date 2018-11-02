package source;

import java.util.*;
import java.io.*;

class CheckPlagiarism{

    // constant values
    final String REGEX_RULE = "[^a-z'A-Z\\d]+";
    final String PARSE_ERR = "File is empty or some error occured when parsing";

    // empty constructor
    public CheckPlagiarism(){}
    /*
        Assumptions:
        1. assumes that input only takes words and chars, but not numbers
        2. words in synonyms are unique. Once used in group/line, will not be used again.
           Also assumes sysnonyms file is correct, ie. 2 or more words per line, only words, etc.
        3. tupleSize input is valid, so that tupleSize won't be larger than wordCounts in the files
    */

    /**
    * @param: synonymsPath, path to synonyms file
    * @param: filepath1, the target file
    * @param: filepath2, the file to be compared with file 1
    * @param: tupleSize, defaults to 3 if user specified no tuple size
    * @output:  prints percentage similarity
    *
    *       checkPlagiarism is the main algorithm/routine
    */
    public void checkPlagiarism(String synonymsPath, String filepath1, String filepath2, int tupleSize){

        // map is put the synonym in the correct group
        Map<String, String> synMap = new HashMap<String, String>();

        buildDict(synMap, synonymsPath);

        // parse the file and get the words for each file
        List<String> words1 = parseFile(filepath1);
        List<String> words2 = parseFile(filepath2);

        // for the words, replace the words that are synonyms
        replaceWithSynonyms(words1, synMap);
        replaceWithSynonyms(words2, synMap);

        // build tuples for file1 and 2
        Set<String> tuplesSet1 = new HashSet<String>();
        Set<String> tuplesSet2 = new HashSet<String>();

        buildTuples(words1, tuplesSet1, tupleSize);
        buildTuples(words2, tuplesSet2, tupleSize);

        // percentage similarity
        String percentSimilar = getIntersection(tuplesSet1, tuplesSet2);
        System.out.println(percentSimilar);
    }

    public String getIntersection(Set<String> set1, Set<String> set2){

        // numbers of tuples found in intersection
        int count = 0;

        // iterate over set1, and count how many tuples are also in set2
        for (String tuple : set1){
            if (set2.contains(tuple)) count++;
        }

        float percent = (count*100)/set2.size();
        return String.format("%.1f%%", percent);
    }

    public void replaceWithSynonyms(List<String> words, Map<String, String> map){
        if (words == null || words.size() <= 0) {
            System.err.println(PARSE_ERR);
            return;
        }

        for (int i = 0; i < words.size(); i++){
            String word = words.get(i);

            // replace the synonym with its "base" or parent word
            if (hasSynonym(word, map)){ words.set(i, map.get(word)); }

        }
    }

    public boolean hasSynonym(String word, Map<String, String> map){ return map.containsKey(word); }

    public void buildTuples(List<String> words, Set<String> set, int tupleSize){

        List<String> tuple = new LinkedList<String>();
        // build initial window, then incrementally include the last, kill the first

        for (int index = 0; index < words.size(); index++){

            // build the initial tuple
            if (index < tupleSize){ tuple.add(words.get(index)); continue; }

            // add the initial tuple
            if (index == tupleSize)
                addTupleToSet(tuple, set);

            // include new one, kill the first, add to set
            tuple.add(words.get(index));
            tuple.remove(0);
            addTupleToSet(tuple, set);
        }
    }

    public void addTupleToSet(List<String> tuple, Set<String> set){
        int tupleSize = tuple.size();
        StringBuilder sb = new StringBuilder(tuple.get(0));

        for (int i = 1; i < tupleSize; i++){
            sb.append(" "+tuple.get(i));
        }

        //System.out.println(sb.toString());
        set.add(sb.toString());
    }

    // test this function if it parses the file correctly
    public List<String> parseFile(String filepath){
        List<String> result = new LinkedList<String>();

        File file = new File(filepath);
        Scanner cin = null;

        try{
            cin = new Scanner(file);
        } catch (FileNotFoundException e) { e.printStackTrace(); }

        String line = "";
        while(cin.hasNextLine()){
            line = cin.nextLine();

            if (line.equals("")) continue;

            String[] words = line.split(REGEX_RULE);

            result.addAll(Arrays.asList(words));
        }

        //System.out.println(result);
        //System.out.println("");
        return result;
    }

    // reads from synonym file line by line
    public void buildDict(Map<String, String> map, String filepath){
        File file = new File(filepath);
        Scanner cin = null;

        try{
            cin = new Scanner(file);
        } catch (FileNotFoundException e) { e.printStackTrace(); }

        int lineNumber = 0;
        String line = "";

        while(cin.hasNextLine()){
            line = cin.nextLine();
            buildSetMap(map, line);
        }
    }

    public void buildSetMap(Map<String, String> map, String line){
        String[] words = line.split(" ");
        if (words.length <= 1) return;

        String base = words[0];   // children words mapped to same "base" word (first word)

        for (int i = 1; i < words.length; i++){
            map.put(words[i], base);
        }
    }
}
