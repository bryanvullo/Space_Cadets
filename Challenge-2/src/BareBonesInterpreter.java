package src;

import java.util.regex.Pattern; //what we're looking for
import java.io.File;  //import the File class
import java.io.FileNotFoundException;  //import this class to handle errors
import java.util.Scanner; //import the Scanner class to read text files
import java.util.HashMap;
import java.util.Map;

class BareBonesInterpreter {
  String fileName;

  //Hashmap holds the variables' names and values in key: value pairs
  HashMap<String, Integer> variables = new HashMap<String, Integer>();
  //creates a hashmap of lineNumber: line pairs
  HashMap<Integer, String> file = new HashMap<Integer, String>();
  //holds the index of what line the program is on
  Integer lineNumber = 1;

  //sets words to look for
  Pattern clear = Pattern.compile("clear", Pattern.CASE_INSENSITIVE);
  Pattern incr = Pattern.compile("incr", Pattern.CASE_INSENSITIVE);
  Pattern decr = Pattern.compile("decr", Pattern.CASE_INSENSITIVE);
  Pattern whileloop = Pattern.compile("while", Pattern.CASE_INSENSITIVE);
  Pattern end = Pattern.compile("end", Pattern.CASE_INSENSITIVE);
  Pattern not = Pattern.compile("not", Pattern.CASE_INSENSITIVE);
  Pattern doWord = Pattern.compile("do", Pattern.CASE_INSENSITIVE);

  //creates the File object and Scanner object
  File myCode;
  Scanner myReader;

  // constructor method which takes a txt file
  public BareBonesInterpreter(String filename) {
    fileName = filename;
  }

  public void clearMethod(String line) {
    if (line.charAt(line.length() - 1) != ';') {
      System.out.println("Missing ';' at the end of line: " + lineNumber);
      System.exit(0);
    }
    line = line.replaceAll(";","");
    line = line.replaceAll("   ","");
    String[] words = line.split(" ");
    if (!clear.matcher(words[0]).find()) {
      System.out.println("Command 'clear' not in correct place, in line: " + lineNumber);
      System.exit(0);
    } else {
      checkVar(words[1]);
      variables.replace(words[1], 0);
    }
  }

  public void checkVar(String varName) {
    if (!variables.containsKey(varName)) {
      createVar(varName);
    }
  }

  public void createVar(String varName) {
    variables.put(varName, 0);
  }

  public void incrMethod(String line) {
    if (line.charAt(line.length() - 1) != ';') {
      System.out.println("Missing ';' at the end of line: " + lineNumber);
      System.exit(0);
    }
    line = line.replaceAll(";","");
    line = line.replaceAll("   ","");
    String[] words = line.split(" ");
    if (!incr.matcher(words[0]).find()) {
      System.out.println("Command 'incr' not in correct place, in line: " + lineNumber);
      System.exit(0);
    } else {
      checkVar(words[1]);
      Integer value = variables.get(words[1]);
      value = ++value;
      variables.replace(words[1], value);
    }
  }

  public void decrMethod(String line) {
    if (line.charAt(line.length() - 1) != ';') {
      System.out.println("Missing ';' at the end of line: " + lineNumber);
      System.exit(0);
    }
    line = line.replaceAll(";","");
    line = line.replaceAll("   ","");
    String[] words = line.split(" ");

    if (!decr.matcher(words[0]).find()) {
      System.out.println("Command 'decr' not in correct place, in line: " + lineNumber);
      System.exit(0);
    } else {
      checkVar(words[1]);
      Integer value = variables.get(words[1]);
      value = --value;
      if (value < 0) {
        value = 0;
      }
      variables.replace(words[1], value);
    }
  }

  public void whileMethod(String line) {
    // checks if line ends in ';'
    if (line.charAt(line.length() - 1) != ';') {
      System.out.println("Missing ';' at the end of line: " + lineNumber);
      System.exit(0);
    }

    // removes ';' and splits the line into words
    line = line.replaceAll(";","");
    line = line.replaceAll("   ","");
    String[] words = line.split(" ");

    //checks if there is an int value for the end value
    try {
      Integer.parseInt(words[3]);
    } catch (NumberFormatException e) {
      System.out.println("Missing int value in while loop statement in line: " + lineNumber);
      System.exit(0);
    }

    // checks in line is semantically correct
    if (!whileloop.matcher(words[0]).find()) {
      System.out.println("Command 'while' not in correct place, in line: " + lineNumber);
      System.exit(0);
    }
    else if (!not.matcher(words[2]).find()) {
      System.out.println("Command 'not' not in correct place, in line: " + lineNumber);
      System.exit(0);
    }
    else if (!doWord.matcher(words[4]).find()) {
      System.out.println("Command 'do' not in correct place, in line: " + lineNumber);
      System.exit(0);
    }
    else {
      // remembers the line and sets up the while loop
      lineNumber = ++lineNumber;
      Integer lineToRemember = lineNumber;
      String variable = words[1];
      Integer value = variables.get(variable);
      Integer endValue = Integer.parseInt(words[3]);
      // sets the loop into motion then branches back into the first line when it reaches "end" commmand
      while (value != endValue) {
        lineNumber = lineToRemember;
        setWhileloop();
        value = variables.get(variable);
      }
    }
  }

  public void setWhileloop() {
    while (lineNumber <= file.size()) {
      String line = file.get(lineNumber);

      if ( clear.matcher(line).find() ) {
        clearMethod(line);
      }
      else if ( incr.matcher(line).find() ) {
        incrMethod(line);
      }
      else if ( decr.matcher(line).find() ) {
        decrMethod(line);
      }
      else if ( whileloop.matcher(line).find() ) {
        whileMethod(line);
      }
      else if ( end.matcher(line).find() ) {
        break;
      }
      lineNumber = ++lineNumber;
    }
  }

  public void printVariables(HashMap<String, Integer> variables) {
    //prints out all the variables values
    for (Map.Entry<String, Integer> entry : variables.entrySet()) {
      String var = entry.getKey();
      Integer value = entry.getValue();
      System.out.println(var + ": " + value);
    }
  }

  public void createFile() {
    //creates the File object and Scanner object
    try {
      myCode = new File(fileName);
      myReader = new Scanner(myCode);
    } catch (FileNotFoundException e) {
      System.out.println("File not found");
      System.exit(0);
    }
    //creates a hashmap of lineNumber: line pairs
    Integer i = 1;
    while (myReader.hasNextLine()) {
      String line = myReader.nextLine();
      file.put(i, line);
      i = ++i;
    }
  }

  public static void main(String[] args) {
    BareBonesInterpreter myInterpreter = new BareBonesInterpreter("src/BareBonesCode2.txt");
    myInterpreter.createFile();
    myInterpreter.setWhileloop();
    myInterpreter.printVariables(myInterpreter.variables);
  }
}