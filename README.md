## PlagiarismCheckProgram

### *Assumptions*
  1. assumes that input only takes words and chars, but not numbers
  2. words in synonyms are unique. Once used in group/line, will not be used again.
  Also assumes sysnonyms file is correct, ie. 2 or more words per line, only words, etc.
  3. tupleSize input will be valid, so for example, tupleSize won't be larger than wordCounts in the files
  4. the three proper files are present, named, and called from the command line correctly
  
### *How to compile from command line -- Assumes you have some java or jdk installed*

*Make sure you are in the ```source``` root directory*

to compile:
```
javac source/CommandLine.java
```

to run (example):
```
java source/CommandLine source/synonyms.txt source/file1.txt source/file2.txt 3
```

### *Algorithm*

  *Explanation: The "parent" synonym is the unique first word that all synonyms will get mapped to.
  So if the synonym group is*
  ```student scholar undergraduate prisoner```
  *then the ```student``` is the base/parent word, and words ```scholar```, ```undergraduate```, and ```prisoner``` will be
  replaced by the base synonym ```student```*

  ```
  words1, words2 = parse the words from file1 and file2, respectively
  
  replace the synonyms in words1, words2 with their appropriate "base" synonym
  
  set1, set2 = create a set of tuples of words1 and words2 using tupleSize
  
  percent = get percent intersection(set1, set2);
  ```

### *Complexity analysis*
#### Time
 N = larger # of words from file1 and file2, 
 M = size of the tuple, defaults to 3
 
 runtime is **O(N)** if the tupleSize is default, where M can be treated as constant. 
 if tupleSize is large, then O(N*M)
    
#### Space
 O(N*M)
 
### *Design*
  1. Main algorithm is implemented in ```CheckPlagiarism.java``` with all its relevant subroutines
  2. Command line logic is handled in ```CommandLine.java```
  

