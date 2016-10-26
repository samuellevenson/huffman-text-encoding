/**
 * Binary tree class
 * TODO: Update these comments!
 * 
 * @author Samuel Levenson
 * @version 10/19
 */
public class HNode {
  
  public char letter;
  public int freq;
  public BinaryNode rightChild;
  public BinaryNode leftChild;
  
  /**
   * constructor for binary tree node
   */
  public HNode(char l, int f, BinaryNode right, BinaryNode left) {
    letter = l;
    freq = f;
    rightChild = right;
    leftChild = left;
  }
  /**
   * constructor for bottom layer binary tree node
   */
  public HNode(char l, int f) {
    letter = l;
    freq = f;
    rightChild = null;
    leftChild = null;
  }
  
  public boolean isLeaf() {
    return (rightChild == null && leftChild == null);
  }
}