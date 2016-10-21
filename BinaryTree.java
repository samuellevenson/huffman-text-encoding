/**
 * Binary tree class
 * 
 * @author Samuel Levenson
 * @version 10/19
 */
public class BinaryTree {
  
  private String value;
  private BinaryTree rightChild;
  private BinaryTree leftChild;
  
  /**
   * constructor for binary tree node
   */
  public BinaryTree(String val, BinaryTree right, BinaryTree left) {
    value = val;
    rightChild = right;
    leftChild = left;
  }
  /**
   * constructor for bottom layer binary tree node
   */
  public BinaryTree(String val) {
    value = val;
    rightChild = null;
    leftChild = null;
  }

  public String getValue() {
    return value;
  }
  
  public void setValue(String newVal) {
    value = newVal;
  }
  
  public BinaryTree getRightChild() {
    return rightChild;
  }
  
  public void setRightChild(BinaryTree newRight) {
    rightChild = newRight;
  }
  
  public BinaryTree getLeftChile() {
    return leftChild;
  }
  
  public void setLeftChild(BinaryTree newLeft) {
    leftChild = newLeft;
  }
  
  
}