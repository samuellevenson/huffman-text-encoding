/**
 * Binary tree class
 * 
 * @author Samuel Levenson
 * @version 10/19
 */
public class BinaryNode<T> {
  
  private T value;
  private BinaryNode<T> rightChild;
  private BinaryNode<T> leftChild;
  
  /**
   * constructor for binary tree node
   */
  public BinaryNode(T val, BinaryNode<T> right, BinaryNode<T> left) {
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
  
  public BinaryNode<T> getRightChild() {
    return rightChild;
  }
  
  public void setRightChild(BinaryNode<T> newRight) {
    rightChild = newRight;
  }
  
  public BinaryNode<T> getLeftChild() {
    return leftChild;
  }
  
  public void setLeftChild(BinaryNode<T> newLeft) {
    leftChild = newLeft;
  }
  
  public boolean isLeaf() {
    return (rightChild == null && leftChild == null);
  }
}