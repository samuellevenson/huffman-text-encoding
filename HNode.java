/**
 * Binary tree class
 * 
 * @author Samuel Levenson
 * @version 10/19
 */
public class HNode {
  
  public String letter;
  public int freq;
  public HNode right;
  public HNode left;
  
  /**
   * constructor for binary tree node
   */
  public HNode(String l, int f, HNode rght, HNode lft) {
    letter = l;
    freq = f;
    right = rght;
    left = lft;
  }
  
  /**
   * constructor for bottom layer binary tree node (leaf)
   */
  public HNode(String l, int f) {
    letter = l;
    freq = f;
    right = null;
    left = null;
  }
  
  /**
   * returns if given HNode is at the bottom of the tree
   */
  public boolean isLeaf() {
    return (right == null && left == null);
  }


  /**
   * checks if given HNode equals HNode method is called on
   */
  public boolean equals(HNode other) {
   return (right == other.right && left == other.left && letter == other.letter && freq == other.freq);
  }
}