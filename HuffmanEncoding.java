import java.util.ArrayList;

public class HuffmanEncoding {
  public static void main(String[] args) {
    int[] freq = getFreq(args[0]);
  }
  
  public static int[] getFreq(String str) {
    int[] freq = new int[256]; //256 different types of chars
    char[] letterArray = new char[256]; 
    for(int i = 0; i < letterArray.length; i++) {
      freq[letterArray[i]]++;
    }
    return freq;
  }
  
  public static ArrayList<HNode> createTree(int[] freq) {
    ArrayList<HNode> nodeList = new ArrayList<>();
    for(int i = 0; i < freq.length; i++) {
      if(freq[i] > 0) {
        nodeList.add(new HNode((char)i,freq[i]));
      }
    }
    
    while(nodelist.size() > 0) {
      
    }
    
    
    return null;
  }
  
  public static int indexofHNodeWithLowestFreq(ArrayList<HNode> nodelist) {
    minIndex = 0;
    for(int i = 0; i < nodelist.size(); i++) {
      if(nodelist.get(i).freq < nodelist.get(minIndex).freq) {
        minIndex = i;
      }
    }
    return minIndex;
  }
}

