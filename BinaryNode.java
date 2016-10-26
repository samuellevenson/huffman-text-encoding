/**
 * Binary tree class
 * 
 * @author Samuel Levenson
 * @version 10/19
 */
public class BinaryNode<T> {
  
  private T value;
  private BinaryNode rightChild;
  private BinaryNode leftChild;
  
  /**
   * constructor for binary tree node
   */
  public BinaryNode(T val, BinaryNode right, BinaryNode left) {
    value = val;
    rightChild = right;
    leftChild = left;
  }
  /**
   * constructor for bottom layer binary tree node
   */
  public BinaryNode(T val) {
    value = val;
    rightChild = null;
    leftChild = null;
  }

  public T getValue() {
    return value;
  }
  
  public void setValue(T newVal) {
    value = newVal;
  }
  
  public BinaryNode getRightChild() {
    return rightChild;
  }
  
  public void setRightChild(BinaryNode newRight) {
    rightChild = newRight;
  }
  
  public BinaryNode getLeftChild() {
    return leftChild;
  }
  
  public void setLeftChild(BinaryNode newLeft) {
    leftChild = newLeft;
  }
  
  public boolean isLeaf() {
    return (rightChild == null && leftChild == null);
  }
}