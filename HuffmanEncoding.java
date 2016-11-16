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
    //convert from huffman
    if(filepath.getName().contains(".huffman.txt")) {
      String words = readBinary(filepath);
      writeTxt(words, filepath);
    }
    //convert to huffman
    else {
      toHuffman(filepath);
    }
    
    //System.exit(0);
  }
  /**
   * takes text file with regular words and turns into another text file with words huffman encoded 
   */
  public static void toHuffman(File input) {
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
    //output binary file
    writeBinary(text, encodings, input);
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
   * 
   */
  public static String binaryToWords(String message, String[] lookup) {
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
  
  public static char lookup(String msg, String[] lookup) {
    for(int i = 0; i < lookup.length; i++) {
      if(lookup[i].equals(msg)) {
        return (char)i;
      }
    }
    return 0;
  }
  
  /**
   * outputs huffman encoded message with lookup table to binary file
   */
  public static void writeBinary(String text, String[] encodings, File input) {
    File output = new File(input.getParent() + "/" + input.getName().replace(".txt",".huffman.txt")); //creates new file in same directory, name is original.txt
    
    try {
      BufferedWriter writer = new BufferedWriter(new FileWriter(output));
      //outputs length of each encoding in lookup table for reading lookuptable
      for(int i = 0; i < encodings.length; i++) {
        String len = "";
        //add 'padding' to the front to make sure all numbers are 4 digits long for easy reading,
        //there will be issues if a length of an encoding length is >4 binary digits, not sure if possible with only 256 ascii characters
        if(encodings[i] == null) {
          len = "0000";
        } else if(encodings[i].length() < 2) {
         len = "000";
        } else if(encodings[i].length() < 4) {
          len = "00";
        } else if(encodings[i].length() < 8) {
          len = "0";
        }
        
        if(encodings[i] != null) {
          len += Integer.toBinaryString(encodings[i].length());
        }

        if (len.length() != 4) {
          throw new IllegalStateException("Len should always be 4, not " + len.length() + " (i=" + i + ")");
        }

        System.out.println(i + ":" + encodings[i] + ":" + len);
        writer.write(len);
      }
      //outputs lookup table
      for(int i = 0; i < encodings.length; i++) {
        if(encodings[i] != null) {
          writer.write(encodings[i]);
        }
      }
      //convert input into char array and then use encodings aray to output huffman encoding
      char[] chArray = text.toCharArray();
      for(char ch: chArray) {
        writer.write(encodings[ch]);
      }
      writer.close();
    } catch(IOException e) {
      System.out.println("Unable to output to file: " + e.getMessage());
      e.printStackTrace();
      System.exit(1);
    }
  }
  public static String readBinary(File f) {
    String binary = readTxt(f); //String containing entire contents of binary file
    int[] encodingLens = new int[256]; //256 encodings each has a length
    for(int i = 0; i < 1024; i += 4) { //the first 1024 characters in the string are the lengths of the encodings
      encodingLens[i/4] = Integer.parseInt(binary.substring(i, i+4),2);
    }
    binary = binary.substring(1024); //remove the part that was just used
    //create lookup table
    String[] lookup = new String[256];
    for(int i = 0; i < encodingLens.length; i++) {
      lookup[i] = binary.substring(i,i+encodingLens[i]);
      //remove part that was used
      binary = binary.substring(encodingLens[i]);
    }
    System.out.println(binary.substring(0,100)); //testing
    for(int i = 0; i < lookup.length; i++) {
      System.out.println((char)i + ":" + lookup[i]);
    }
    return binaryToWords(binary,lookup);
  }
}
