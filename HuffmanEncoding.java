import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
/**
 * Reads text file given as command line argument, outputs text file containing huffman encoding of input and lookup table for decoding
 * 
 * @author Samuel Levenson
 * @version 11/2/16
 * most helpful resources: http://www.fas.harvard.edu/~cscie119/lectures/trees.pdf - also has useful information about nodes, https://www.youtube.com/watch?v=ZdooBTdW5bM - just huffman encoding
 */
public class HuffmanEncoding {
  public static void main(String[] args) {
    if(args.length < 1) {
      System.out.println("please enter a filepath as a commandline argument");
      System.exit(0);
    }
    File input = new File(args[0]).getAbsoluteFile();
    System.out.println(input.getAbsolutePath());
    String text = readTxt(input);
    
    int[] freq = getFreq(text);
    ArrayList<HNode> nodelist = getNodelist(freq);
    HNode root = createTree(nodelist);
    String[] encodings = new String[256];
    getEncoding(root, encodings, "");
    
    outputTxt(text, encodings, input);
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
   * converts text into huffman encoding, outputs to text file
   */
  public static void outputTxt(String text, String[] encodings, File input) {
    File output = new File(input.getParent() + "/" + input.getName().replace(".txt",".huffman.txt")); //creates new file in same directory, name is original_runlength.txt
    System.out.println(output.getAbsolutePath());
    
    try {
      BufferedWriter writer = new BufferedWriter(new FileWriter(output));
      //outputs lookup table for decoding first
      for(int i = 0; i < encodings.length; i++) {
        if(encodings[i] != null) {
          writer.write((char)(i) + encodings[i] + ", ");
        }
      }
      System.out.println();
      
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
}