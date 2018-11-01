package source;

import java.util.Arrays;

public class CommandLine{

    public final String INPUT_ERR = "Input error. Exiting.";

    public static void main(String[] args){
        new CommandLine().readCommandLine(args);
    }

    public void readCommandLine(String[] args){
        if (args == null){ System.err.println(INPUT_ERR); return; }

        // command line arguments
        String synonymsFile = "";
        String file1 = "";
        String file2 = "";
        int tupleSize = 3;

        if (args.length == 3 || args.length == 4){
             try{
                synonymsFile = args[0].trim();
                file1 = args[1].trim();
                file2 = args[2].trim();

                if (args.length == 4)
                    tupleSize = Integer.valueOf(args[3]);
            }
            catch(Exception e){
                System.err.println(INPUT_ERR);
                e.printStackTrace();
            }
        } else
            System.err.println(INPUT_ERR);

        new CheckPlagiarism().checkPlagiarism(synonymsFile, file1, file2, tupleSize);
    }


}
