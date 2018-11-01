import java.util.*;
import java.io.*;

class CheckPlagiarism{

    /*
        I'll need to separate words by not only their space, but by punctuation as well

    */

    /*
        Assumptions:
        1. assumes that input only takes words and chars, but not numbers
        2. words in synonyms are unique. Once used in group/line, will not be used again.
           Also assumes sysnonyms file is correct, ie. 2 or more words per line, only words, etc.

    */
    public static void main(String[] args){
        CheckPlagiarism t = new CheckPlagiarism();

        String synonymsFile = "synonyms.txt";
        String file1 = "file1.txt";
        String file2 = "";
        int tupleSize = 3;
        //t.checkPlagiarism();

        //t.buildDict(new HashSet<String>(), new HashMap<String, String>(), synonymsFile);
        t.parseFile(file1);
    }

    /**
    * @input: path to synonyms file, string1, string2, tuple size
    * @output: will print percentage similarity
    *
    * Which is # matches/targetSet Size
    */
    public void checkPlagiarism(String synonymsPath, String filepath1, String filepath2, int tupleSize){

        // set is to know if the word has a synonym based on synonym file
        Set<String> synSet = new HashSet<String>();

        // map is put the synonym in the correct group
        Map<String, String> synMap = new HashMap<String, String>();

        buildDict(synSet, synMap, synonymsPath);

        // build tuples for file1 and 2
        Set<String> tuplesSet1 = new HashSet<String>();
        Set<String> tuplesSet2 = new HashSet<String>();

        buildTuples(tuplesSet1, filepath1);
        //buildTuples(tuplesSet1);
    }

    public void buildTuples(Set<String> tuples, String filepath){
        List<String> words = parseFile(filepath);

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

            System.out.println(Arrays.toString(words));

            //result.addAll(Arrays.asList(words));
        }

        //System.out.println(result);
        return result;
    }

    // reads from synonym file line by line
    public void buildDict(Set<String> set, Map<String, String> map, String filepath){
        File file = new File(filepath);
        Scanner cin = null;

        try{
            cin = new Scanner(file);
        } catch (FileNotFoundException e) { e.printStackTrace(); }

        int lineNumber = 0;
        String line = "";

        while(cin.hasNextLine()){
            line = cin.nextLine();
            buildSetMap(set, map, line);
        }

        System.out.println(set);
        System.out.println(map);
    }

    public void buildSetMap(Set<String> set, Map<String, String> map, String line){
        String[] words = line.split(" ");
        if (words.length <= 1) return;

        String base = words[0];   // children words mapped to same "base" word (first word)

        for (int i = 1; i < words.length; i++){
            set.add(words[i]);
            map.put(words[i], base);
        }
    }
}
