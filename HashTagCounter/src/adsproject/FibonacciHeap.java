package adsproject;

/**
 * Implementation of Fibonacci Heap
 * @author Jagan
 *
 */
public class FibonacciHeap {
		
	private NodeFibHeap max;
	
	private int maxDegree;
	
	private int N;
	
	public NodeFibHeap getMax(){
		return max;
	}
	
	public int getMaxDegree(){
		return maxDegree;
	}
	
	public int getNoOfNodes(){
		return N;
	}
	
	/**
	 * Inserts a new node into the fibonacci heap
	 * 
	 * @param newNode
	 */
	public void insert(NodeFibHeap newNode){
			
		if(max == null){
			// If there are no elements in the Fibonacci heap initially
			newNode.leftSibling = newNode.rightSibling = newNode;
			max = newNode;
			
		}else{
			// Create a tree of single element and add it to the top level circular list and update max if necessary
			newNode.leftSibling = max.leftSibling;
			max.leftSibling.rightSibling = newNode;
			newNode.rightSibling = max;
			max.leftSibling = newNode;
			if(newNode.data > max.data)
				max = newNode;
		}
		
		N++;
	}
	
	/**
	 * Updates the max pointer to maximum node after max removal
	 * 
	 * @param node traversal of top-list starts from this node while updating max
	 */
	private void updateMax(NodeFibHeap node){
		max = node; // An arbitary max to start with
		NodeFibHeap temp = node.rightSibling;
		while(temp != node){
			if(temp.data > max.data)
				max = temp;
			temp = temp.rightSibling;
		}
	}
	
	/**
	 * Removes maximum node from the Fibonacci heap
	 * Updates new maximum and consolidates top circular list trees until no trees of equal rank are left
	 * Uses meld operation internally
	 * 
	 * @return removed node from Fibonacci heap
	 * @throws Exception when trying to remove a node when heap is empty
	 */
	public NodeFibHeap remove() throws Exception{

		//If there are no nodes in heap
		if(max == null)
			throw new Exception("Error: Heap is empty, cannot remove node");
		
		NodeFibHeap removeNode = max;
		NodeFibHeap tempNode = max.leftSibling;
		if(removeNode.child != null){
			//Case1: Maximum is the single element in the top level list; Case2: Top level list contains >= 2 elements
			NodeFibHeap child = removeNode.child;
						
			if(max != max.rightSibling){
				// if top level list has more than one element
				max.rightSibling.leftSibling = child.leftSibling;
				child.leftSibling.rightSibling = max.rightSibling;
				child.leftSibling = tempNode;
				tempNode.rightSibling = child;
			}else{
				// top level list has a single element
				tempNode = max.child; //to update the max in updated top list
			}
		}else{
			//Single element in the Fibonacci heap
			if(max == max.leftSibling && max == max.rightSibling){
				// set max = null and return from the method
				max = null;
				N--;
				return tempNode;
			}else{
				max.leftSibling.rightSibling = max.rightSibling;
				max.rightSibling.leftSibling = max.leftSibling;
			}
		}

		N--;
		
		updateMax(tempNode);

		// Consolidate trees with equal ranks after remove
		int heapMaxDegree = (int)Math.ceil(Math.log(N)/Math.log(2));
		NodeFibHeap[] ranks = new NodeFibHeap[heapMaxDegree+1]; // heapMaxDegree+1 will create array with highest index as heapMaxDegree
		NodeFibHeap temp = max, current; // current stores copy of the current node since the node links are getting dynamically updated

		//Start consolidating from current max node
		do{
			temp.parent = null;
			current = new NodeFibHeap(temp);
			while(ranks[temp.degree] != null){
				if(ranks[temp.degree] == temp){
					System.out.print(temp.degree+" "+temp.key+" "+max.key);break;}
				//If there is already a tree with same degree then meld the two trees
				int tempDegree = temp.degree;
				temp = meld(temp, ranks[tempDegree]);
				ranks[tempDegree] = null; // Since after meld the rank increases
			}
			ranks[temp.degree] = temp;
			temp = current.rightSibling;
			
		}while(current.rightSibling != max);	//Check the condition when to terminate

		return removeNode;
	}
	
	public void arbitaryRemove(NodeFibHeap node){
		
	}
	
	/**
	 * Melds two trees
	 * Removes the to be child tree from top-level circular list and adds it to the to be parent's child circular
	 * list, updating the sibling and parent pointers.
	 * Since meld happens only during remove node, to bypass redundant traversal of top-level root nodes we
	 * remove the root of the melded tree from its position and put it at "current" position as defined by "current"
	 * variable in remove method
	 * 
	 * @param node1 current node in top-level circular list traversal where meld is called
	 * @param node2 node with equal degree as current node
	 * 
	 * @return root of the melded trees
	 */
	private NodeFibHeap meld(NodeFibHeap node1, NodeFibHeap node2){

		NodeFibHeap root = node1;

		if(node1.data < node2.data || (node1.data.equals(node2.data) && max == node2)){ // Added "=" since assuming maximum element will already be in the ranks array 
			node1 = node2;
			node2 = root;
		}
		NodeFibHeap temp = new NodeFibHeap(node2); //smallest node to be removed from top level list

		if(node1.child == null){
			node1.child = node2;
		}		
		 
		//Add node2 to the child circular list of node1
		node2.rightSibling = node1.child;
		node2.leftSibling = node1.child.leftSibling;
		node1.child.leftSibling.rightSibling = node2;
		node1.child.leftSibling = node2;

		node2.parent = node1;
		node1.degree++;

		// Remove the smaller tree from top list
		temp.rightSibling.leftSibling = temp.leftSibling;
		temp.leftSibling.rightSibling = temp.rightSibling;

		return node1;
	}
	
	/**
	 * Increases the key of a node and updates it in Fibonacci Heap and max if necessary
	 * 
	 * @param node
	 * @param increase
	 */
	public void increaseKey(NodeFibHeap node, int increase){

		NodeFibHeap temp = node;
		temp.data = temp.data + increase;
		if(temp.parent != null && temp.data > temp.parent.data){
			NodeFibHeap parentNode = temp.parent;
			if(addLowerLevelNodeToTopLevellist(temp))
				cascadingCut(parentNode);
		}
		if(temp.data > max.data)
			max = temp;

	}
	
	/**
	 * It checks whether the increased node in increaseKey is larger than its parent node, if so it will remove
	 * the increased node from its parent and add to top level circular list. While doing so to maintain the flat
	 * structure of the heap it will also remove the parent whose cascadingCut flag is marked true or in other sense
	 * lost a child in the past
	 * 
	 * @param temp
	 */
	public void cascadingCut(NodeFibHeap temp){	

		NodeFibHeap node = null;
		// If any method is calling cascadingCut it means calling method encountered a node whose second child
		// has been removed
		boolean childcut = true;
		while(childcut && temp != null && temp.childCut == true){
			node = temp.parent;
			if(node == null){
				temp.childCut = false;
			}else{
				childcut = addLowerLevelNodeToTopLevellist(temp);
			}
			temp = node;
		}
	}
	
	/**
	 * Since nodes from lower level will be removed during both increase key and cascading cuts
	 * modularized to separate function
	 * 
	 * @param temp
	 * 
	 * @return boolean result for successful addition of a child to top level list
	 */
	private boolean addLowerLevelNodeToTopLevellist(NodeFibHeap temp){
		
		//single child of parent then set parent's child as null and add to top level list
		if(temp.parent.degree == 1)
			temp.parent.child = null;
		else{
			//remove from sibling list if not single child
			temp.parent.child = temp.rightSibling;
			temp.leftSibling.rightSibling = temp.rightSibling;
			temp.rightSibling.leftSibling = temp.leftSibling;
		}

		//Add removed node to top level list
		temp.leftSibling = max.leftSibling;
		max.leftSibling = max.leftSibling.rightSibling = temp;
		temp.rightSibling = max;

		temp.parent.degree--;
		
		// If this is the first time parent loses a child set its child cut to true
		// Do not set a top level node
		boolean childcut = false;
		if(temp.parent.childCut != true ){//&& temp.parent.parent != null){
			temp.parent.childCut = true;
		}else{
			//parent lost child second time now
			childcut = true;
		}
		temp.parent = null;
		temp.childCut = false;
		
		return childcut;
	}
	
}