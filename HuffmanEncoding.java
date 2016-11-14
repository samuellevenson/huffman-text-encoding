import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
import javax.swing.JFileChooser;
/**
 * Reads text file given as command line argument, outputs text file containing huffman encoding of input and lookup table for decoding
 * 
 * @author Samuel Levenson
 * @version 11/2/16
 * most helpful resources: http://www.fas.harvard.edu/~cscie119/lectures/trees.pdf - also has useful information about nodes, https://www.youtube.com/watch?v=ZdooBTdW5bM - just huffman encoding
 */
public class HuffmanEncoding {
  public static void main(String[] args) {
    File filepath = null;
    if(args.length == 0) {      
      JFileChooser fileChooser = new JFileChooser();
      int choice = fileChooser.showOpenDialog(null);
      if(choice == JFileChooser.APPROVE_OPTION) {
        filepath = fileChooser.getSelectedFile();
      }
      else {
        System.exit(1);
      }
    }
    else {
      filepath = new File(args[0]);
    }
    filepath = filepath.getAbsoluteFile();
    
    if(filepath.getName().contains(".huffman.txt")) {
      String words = readHuffmanTxt(filepath);
      writeTxt(words, filepath);
    }
    else {
      toHuffman(filepath);
    }
    
    System.exit(0);
  }
  /**
   * takes text file with regular words and turns into another text file with words huffman encoded 
   */
  public static File toHuffman(File input) {
    //read input file
    String text = readTxt(input);
    //create frequency table
    int[] freq = getFreq(text);
    //create all leaf nodes that will be in tree
    ArrayList<HNode> nodelist = getNodelist(freq);
    //build tree up from leafs
    HNode root = createTree(nodelist);
    String[] encodings = new String[256];
    //traverse tree to find encodings for each letter
    getEncoding(root, encodings, "");
    //output original message, huffman encoded, into a text file
    return outputHuffmanTxt(text, encodings, input);
  }
  
  public static int[] getFreq(String str) {
    int[] freq = new int[256]; //256 different types of chars
    //go through string and add the corresponding point in array for each char
    for(int i = 0; i < str.length(); i++) {
      freq[str.charAt(i)]++;
    }
    return freq;
  }
  /**
   * returns list of all the leaf nodes to be made into a tree
   */
  public static ArrayList<HNode> getNodelist(int[] freq) {
    ArrayList<HNode> nodelist = new ArrayList<>();
    //construct list of leaf nodes, one for each non-zero frequency char
    for(int i = 0; i < freq.length; i++) {
      if(freq[i] > 0) {
        nodelist.add(new HNode(String.valueOf((char)i),freq[i]));
      }
    }
    return nodelist;
  }
  /**
   * builds huffman tree based on frequency table
   * returns root node
   */
  public static HNode createTree(ArrayList<HNode> nodelist) {
    while(nodelist.size() > 1) { //changed condition from (leafLeft(nodelist)) to (nodelist.size() > 1)
      //HNodes in list with the 2 lowest frequencies.
      HNode r = nodelist.remove(indexofLowestFreq(nodelist));
      HNode l = nodelist.remove(indexofLowestFreq(nodelist));
      //add new HNode into list with no char and frequency is sum of two lowest
      nodelist.add(new HNode(null, r.freq + l.freq, r, l)); //leftchild is >= frequency of right child
    }
    return nodelist.get(nodelist.size()-1); //I cannot think of a situation where the root node would not be the last one added to the list
  }
  
  /**
   * returns index of node with lowest frequency value in list
   */
  public static int indexofLowestFreq(ArrayList<HNode> nodelist) {
    int minIndex = 0;
    for(int i = 0; i < nodelist.size(); i++) {
      if(nodelist.get(i).freq < nodelist.get(minIndex).freq) {
        minIndex = i;
      }
    }
    return minIndex;
  }
  /**
   * puts the huffman encodings of all the leafs in the tree into encodings
   */
  public static void getEncoding(HNode node, String[] encodings, String path) {
    if(!node.isLeaf()) {
      getEncoding(node.right, encodings, path + "1");
      getEncoding(node.left, encodings, path + "0");
    }
    else {
      //put the path of the leaf we have reached into the encoding array
      encodings[node.letter.charAt(0)] = path;
    }
  }
  
  /**
   * reads text file using a scanner, returns contents
   */
  public static String readTxt(File f) {
    try {
      String contents = new Scanner(f).useDelimiter("\\A").next(); //delimiter '\\A' makes scanner read entire file in one go
      return contents;
    } catch (IOException e) {
      System.out.println("Unable to read file: " + e.getMessage());
      e.printStackTrace();
      System.exit(1);
    }
    return null;
  }
  /**
   * writes a message to text file, returns output file
   */
  public static File writeTxt(String words, File input) {
    File output = new File(input.getParent() + "/" + input.getName().replace(".huffman.txt",".txt")); //creates new file in same directory, name is original_runlength.txt
    try {
      BufferedWriter writer = new BufferedWriter(new FileWriter(output));
      writer.write(words);
      writer.close();
    } catch(IOException e) {
      System.out.println("Unable to output to file: " + e.getMessage());
      e.printStackTrace();
      System.exit(1);
    }
    return output;
  }
  
  /**
   * converts text into huffman encoding, outputs to text file
   */
  public static File outputHuffmanTxt(String text, String[] encodings, File input) {
    File output = new File(input.getParent() + "/" + input.getName().replace(".txt",".huffman.txt")); //creates new file in same directory, name is original_runlength.txt
    
    try {
      BufferedWriter writer = new BufferedWriter(new FileWriter(output));
      //outputs lookup table for decoding first
      for(int i = 0; i < encodings.length-1; i++) {
        writer.write(encodings[i] + ",");
      }
      writer.write(encodings[encodings.length-1] + ";");
      
      //convert input into char array and then use encodings aray to output huffman encoding
      char[] chArray = text.toCharArray();
      for(char ch: chArray) {
        writer.write(encodings[ch]);
      }
      writer.close();
    } catch (IOException e) {
      System.out.println("Unable to output to file: " + e.getMessage());
      e.printStackTrace();
      System.exit(1);
    }
    return output;
  }
  /**
   * 
   */
  public static String readHuffmanTxt(File f) {
    String[] lookup = makeLookupTable(f);
    String message = ""; //message is what is read out of input text file, huffman encoded
    
    try {
      Scanner in = new Scanner(f);
      in.useDelimiter(";");
      message = in.next();
      message = in.next(); //only want the part after the semicolon
    } catch (IOException e) {
      System.out.println("Unable to read file: " + e.getMessage());
      e.printStackTrace();
      System.exit(1);
    }
    String words = ""; //words is the converted message, in regular words
    //use lookup table to convert message from 1's and 0's to words
    int start = 0;
    while(start < message.length() -1) {
      int end = start + 1;
      char c = 0;
      while(c == 0) { //once c != 0, we have reached the end of the encoding of one char
        String s = message.substring(start,end);
        //check if current substring of message is a full encoding yet
        c = lookup(s,lookup);
        //will go over the end of the string sometimes...
        if(end == message.length()) {
          return words;
        }
        end++;
      }
      words += c;
      start = end - 1; //start the next letter where the last one ended
    }
    return words;
  }
  
  public static char lookup(String msg, String[] table) {
    for(int i = 0; i < table.length; i++) {
      if(table[i].equals(msg)) {
        return (char)i;
      }
    }
    return 0;
  }
  /**
   * 
   */
  public static String[] makeLookupTable(File input) {
    String[] lookup = new String[256];
    try {
      Scanner in = new Scanner(input);
      in.useDelimiter(",|;");
      for(int i = 0; i < lookup.length; i++) {
        lookup[i] = in.next();
      }
    } catch (IOException e) {
      System.out.println("Unable to read file: " + e.getMessage());
      e.printStackTrace();
      System.exit(1);
    }
    return lookup;
  }
  /**
   * outputs huffman encoded message with lookup table to binary file
   * problems:
   * what to use for delimiters? 
   * how to know how to read lookup table 
   * how to know when lookup table ends and message starts
   */
  public static void outputBinary(String text, String[] encodings, File input) {
    FileOutputStream writer = null;
    
    try {
      writer = new FileOutputStream(input.getParent() + "/" + input.getName().replace(".txt",".huffman"));
      //outputs lookup table for decoding first
      for(int i = 0; i < encodings.length-1; i++) {
        writer.write(Integer.parseInt(encodings[i]));
      }
      writer.write(Integer.parseInt(encodings[encodings.length-1]));
      
      //convert input into char array and then use encodings aray to output huffman encoding
      char[] chArray = text.toCharArray();
      for(char ch: chArray) {
        writer.write(Integer.parseInt(encodings[ch]));
      }
      writer.close();
    } catch(IOException e) {
      System.out.println("Unable to output to file: " + e.getMessage());
      e.printStackTrace();
      System.exit(1);
    }
  }
}