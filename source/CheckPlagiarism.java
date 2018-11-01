package source;

import java.util.*;
import java.io.*;

class CheckPlagiarism{
    public CheckPlagiarism(){}
    /*
        I'll need to separate words by not only their space, but by punctuation as well

    */

    /*
        Assumptions:
        1. assumes that input only takes words and chars, but not numbers
        2. words in synonyms are unique. Once used in group/line, will not be used again.
           Also assumes sysnonyms file is correct, ie. 2 or more words per line, only words, etc.
        3. tupleSize input is valid, so that tupleSize won't be larger than wordCounts in the files
    */

    /*public static void main(String[] args){
        CheckPlagiarism t = new CheckPlagiarism();

        String synonymsFile = "synonyms.txt";
        String file1 = "file1.txt";
        String file2 = "file2.txt";
        int tupleSize = 3;
        t.checkPlagiarism(synonymsFile, file1, file2, tupleSize);

        //t.buildDict(new HashSet<String>(), new HashMap<String, String>(), synonymsFile);
        //t.parseFile(file1);
    }*/

    /**
    * @input: path to synonyms file, string1, string2, tuple size
    * @output: will print percentage similarity
    *
    * Which is # matches/targetSet Size
    */
    public void checkPlagiarism(String synonymsPath, String filepath1, String filepath2, int tupleSize){

        // map is put the synonym in the correct group
        Map<String, String> synMap = new HashMap<String, String>();

        buildDict(synMap, synonymsPath);

        //System.out.println(synSet);
        //System.out.println(synMap);
        //System.out.println("");

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

        System.out.format("intersection count: %d, set1 size: %d, set2 size: %d \n", count, set1.size(), set2.size());

        System.out.println("");
        System.out.println(set1);
        System.out.println(set2);

        float percent = (count*100)/set2.size();
        return String.format("%.1f%%", percent);
    }

    public void replaceWithSynonyms(List<String> words, Map<String, String> map){
        if (words == null || words.size() <= 0) {
            System.err.println("File is empty or some error occured when parsing");
            return;
        }

        for (int i = 0; i < words.size(); i++){
            String word = words.get(i);

            // replace the synonym with its "base" or parent word
            if (hasSynonym(word, map)){ words.set(i, map.get(word)); }

        }
        System.out.println(words);
    }

    public boolean hasSynonym(String word, Map<String, String> map){ return map.containsKey(word); }

    public void buildTuples(List<String> words, Set<String> set, int tupleSize){

        System.out.println("");

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

        System.out.println("");
    }

    public void addTupleToSet(List<String> tuple, Set<String> set){
        int tupleSize = tuple.size();
        StringBuilder sb = new StringBuilder(tuple.get(0));

        for (int i = 1; i < tupleSize; i++){
            sb.append(" "+tuple.get(i));
        }

        System.out.println(sb.toString());
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

            //String[] words = line.split("[\\p{IsPunctuation}\\p{IsWhite_Space}]+");
            String[] words = line.split("[^a-z'A-Z\\d]+");

            //System.out.println(Arrays.toString(words));

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
