package adsproject;

/**
 * <b>Node Structure:</b>
 * 	Attributes: Key, Data, Degree
 * 	|Pointers: Parent, Child, Left Sibling, Right Sibling
 * 	|Boolean: ChildCut 
 * @author Jagan
 *
 */
public class NodeFibHeap{
	
	public int degree = 0;
	public Integer data = null;
	public String key;
	
	public NodeFibHeap parent = null;
	public NodeFibHeap child = null;
	public NodeFibHeap leftSibling = null;
	public NodeFibHeap rightSibling = null;
	
	public boolean childCut = false;
	
	public NodeFibHeap(){}
	
	public NodeFibHeap(String key,int data){
		this.key = key;
		this.data = data;
	}
	
	public NodeFibHeap(NodeFibHeap obj){
		key = obj.key;
		data = obj.data;
		degree = obj.degree;
		parent = obj.parent;
		child = obj.child;
		leftSibling= obj.leftSibling;
		rightSibling = obj.rightSibling;
		childCut= obj.childCut;
	}
}