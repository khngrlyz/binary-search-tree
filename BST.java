package project4;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Stack;
/**
 * This class represents a Binary Search Tree of generic type.
 * It contains a BSTNode class with methods for handling the nodes, and also
 * methods and constructors specified in its javadoc page : https://cs.nyu.edu/~joannakl/cs102_f22/projects/project4/project4/BST.html#toString()
 * 
 * @author Chloe Baatar * @version 12/04/2022
 * 
 * @param <E>
 */

public class BST<E extends Comparable<E>> extends Object implements Iterable<E>{

	protected BSTNode root;   //reference to the root node of the tree 
	protected Comparator<E> comparator;   //comparator object to overwrite the 
	//natural ordering of the elements 
	private boolean found;  //helper variable used by the remove methods
	private boolean added ; //helper variable used by the add method 


	/**
	 * Constructs a new, empty tree, sorted according to the natural ordering of its elements.
	 */
	public BST () {
		root = null; 
		comparator = null; 
	}

	/**
	 * Constructs a new, empty tree, sorted according to the specified comparator.
	 */
	public BST (Comparator<E> comparator) {
		this.root = null; 
		this.comparator = comparator;
	}

	/**
	 * Constructs a new tree containing the elements in the specified collection, sorted according to the natural ordering of its elements. 
	 * All elements inserted into the tree must implement the Comparable interface.
	 * This operation should be O(N logN) where N is the number of elements in the collection. 
	 * This implies, that the tree that is constructed has to have the high that is approximately logN, not N.
	 * 
	 * @param collection is the array of elements the new tree will be consturcted from
	 * 
	 * @throws NullPointerException
	 */
	public BST (E[] collection) throws NullPointerException {
		int len = collection.length;
		if (len == 0) {
			throw new NullPointerException("Collection cannot be empty.");
		}
		Arrays.sort(collection);
		this.root = sortedArrToBST(collection, 0, collection.length-1);


	}

	/**
	 * Recursive algorithm that constructs a new binary tree from a sorted array
	 * 
	 * @param arr is the given array
	 * 
	 * @param start first index of the array
	 * 
	 * @param end last index of the array
	 * 
	 * @return the root of the new tree
	 */
	public BSTNode sortedArrToBST(E[] arr, int start, int end) {
		if(start>end)
			return null;

		int mid = (start+end)/2;
		BSTNode root = new BSTNode(arr[mid]);
		root.left = sortedArrToBST(arr, start, mid-1);
		root.right = sortedArrToBST(arr, mid+1, end);
		return root;
	}




	/**
	 * Helper method that calls the recursive size method
	 * 
	 * @return the size of the tree
	 */
	public int size() {
		return size(this.root);	  
	}

	/**
	 * recursive method to calculate the size of the tree
	 * 
	 * @param root
	 * 
	 * @return
	 */
	private int size(BSTNode root) {
		if (root == null)
			return 0;
		else
			return (size(root.left) + 1 + size(root.right));
	}

	/**
	 * Checks if the tree is empty
	 * @return true if this set contains no elements. 
	 */
	public boolean isEmpty() {
		if(this.root == null) 
			return true;
		else
			return false;

	}
	/**
	 * Helper method that calls the recursive height method
	 * 
	 * @return the height of the tree, 0 if the tree is empty
	 */
	public int height() {
		return height(this.root);
	}
	/**
	 * Returns the height of this tree. The height of a leaf is 1. 
	 * The height of the tree is the height of its root node. 
	 * @param root
	 * @return
	 */
	private int height(BSTNode root)
	{
		if (root == null)
			return 0;
		else {
			/* compute the depth of each subtree */
			int lHeight = height(root.left);
			int rHeight = height(root.right);

			/* use the larger one */
			if (lHeight > rHeight)
				return (lHeight + 1);
			else
				return (rHeight + 1);
		}
	}

	/**
	 * Adds the specified element to this tree if it is not already present. 
	 * If this tree already contains the element, the call leaves the 
	 * tree unchanged and returns false.
	 * 
	 * @param data element to be added to this tree

	 * @return true if this tree did not already contain the specified element 
	 * 
	 * @throws NullPointerException if the specified element is null  
	 * this method is a work of Joanna Klukowska
	 */
	public boolean add (E data) throws NullPointerException {
		if (data == null ) {
			throw new NullPointerException("Null values cannot be added to the tree."); 
		}
		//adding first element to the tree 
		if (root == null ) {
			root = new BSTNode(data); 
			return true; 
		}
		added = add(data, root) ;
		return added;
	}

	private boolean add(E data, BSTNode node ) {

		if ( node.data.equals(data) ) {
			return false; 
		}
		if (data.compareTo(node.data ) < 0 ) {
			if (node.left == null ) {
				node.left = new BSTNode(data); 
				return true; 
			}
			return add( data, node.left) ;
		}
		else {
			if (node.right == null ) {
				node.right = new BSTNode(data); 
				return true; 
			}
			return add(data, node.right) ; 
		}
	}

	/**
	 * Helper method that calls the recursive ceiling method
	 * @param e the value to match
	 * 
	 * @return the least element greater than or equal to e, or null if there is no such element
	 * 
	 * @throws ClassCastException
	 * @throws NullPointerException
	 */
	public E ceiling​(E e) throws ClassCastException, NullPointerException {
		if (e == null) {
			throw new NullPointerException("Cannot find the ceiling for null value.");
		}
		return ceiling(this.root, e);
	}
	/**
	 * Recursive method that returns the smallest element in the tree 
	 * that is greater than or equal to the given element.
	 * Returns null if there is no such element.
	 * 
	 * @param root the root of this tree
	 * 
	 * @param e the value to the match
	 * 
	 * @return
	 */
	private E ceiling(BSTNode root, E e) {

		// Base cases
		if (root == null)
			return null;
		if (root.data.equals(e))
			return e;

		// If root's value is greater, try in left
		// subtree
		else if (root.data.compareTo(e) > 0) {
			E k = ceiling(root.left, e);
			if (k == null)
				return root.data;
			else
				return k;
		}

		// If root's value is smaller, return value
		// from right subtree.
		else if (root.data.compareTo(e) < 0)
			return ceiling(root.right, e);
		return null;
	}

	/**
	 * Returns the greatest element in this set less than or 
	 * equal to the given element, or null if there is no such element. 
	 * 
	 * @param e value to match
	 * @return the greatest element that is less than or equal to the given element
	 * @throws ClassCastException if the specified element cannot be compared with the elements currently in the set
	 * @throws NullPointerException if the specified element is null
	 */
	public E floor(E e) throws ClassCastException, NullPointerException {
		if(e == null) {
			throw new NullPointerException("Cannot find the floor for null value.");
		}
		return floor(this.root, e);

	}
	/**
	 * Recursive method returns the greatest element in this set that is
	 * less than or equal to the given element, or null if there is no such element. 
	 * @param root
	 * 
	 * @param e
	 * 
	 * @return 
	 * Returns the greatest element in this set less than or equal to the given element, 
	 * or null if there is no such element. 
	 */
	private E floor(BSTNode root, E e)
	{
		// Base cases
		if (root == null)
			return null;
		if (root.data.equals(e))
			return e;

		// If root's value is smaller, try in right
		// subtree
		else if (root.data.compareTo(e) < 0) {
			E k = floor(root.right, e);
			if (k == null)
				return root.data;
			else
				return k;
		}

		// If root's key is greater, return value
		// from left subtree.
		else if (root.data.compareTo(e) > 0)
			return floor(root.left, e);
		return null;
	}

	/**
	 *  Wrapper function that deletes the tree and sets root node reference to null.
	 */
	public void clear() {

		clear(this.root);
		this.root = null;
	}
	/**
	 * this function clears the tree in post order traversal
	 * @param node
	 */
	private void clear(BSTNode node)
	{
		// In Java automatic garbage collection
		// happens, so we can simply make root
		// null to delete the tree
		this.root = null;
	}


	/**
	 * Returns true if this set contains the specified element. 
	 * More formally, returns true if and only if this set contains an element 
	 * e such that Objects.equals(o, e). 
	 * @param o object to be checked in the tree
	 * 
	 * @return true if the object contains the tree
	 *
	 * @throws NullPointerException if the specified element is null and this set uses natural ordering, or its comparator does not permit null elements
	 * @throws ClassCastException if the specified object cannot be compared with the elements currently in the set
	 */
	public boolean contains(Object o) throws NullPointerException, ClassCastException {
		if(o == null) {
			throw new NullPointerException("The tree does not contain null elements.");
		}
		return contains(this.root, o);

	}
	// recursive method that returns true if the tree contains Object o
	private boolean contains(BSTNode root, Object o) {

		if(root.data.equals(o)) {
			return true;
		}
		try {
			@SuppressWarnings("unchecked")
			E obj = (E) o;
			if (( root.data.compareTo(obj) > 0 && root.left == null) ||
					(root.data.compareTo(obj)< 0 && root.right == null)){
				return false;
			}
			if (root.data.compareTo(obj) > 0  && root.left != null ){
				return contains(root.left, o);
			}
			else if (root.data.compareTo(obj) < 0  && root.right != null ) {
				return contains(root.right, o);
			}
			else
				return false;
		}
		catch(ClassCastException ex) {
			System.err.println("ClassCastException thrown");
		}
		return false;
	}
	/**
	 * Compares the specified object with this tree for equality.
	 * Returns true if the given object is also a tree, the two trees have the same size,
	 * and every member of the given tree is contained in this tree. 
	 *This operation should be O(N).
	 *@param object to be compared for equality with this tree
	 *@return true if the object is equal to the tree
	 */

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if(obj == this) {
			return true;
		}
		if ((obj instanceof BST)){
			try {
				BST<E> o = (BST<E>) obj;
				return isIdentical(this.root, o.root) ;
			}catch(ClassCastException ex) {
				System.err.println("ClassCastException thrown.");
			}
		}
		return false;

	}
	//Recursive method that checks if two trees are identical
	private boolean isIdentical(BSTNode root1,  BSTNode root2){
		// Check if both the trees are empty
		if (root1 == null && root2 == null)
			return true;

		// If any one of the tree is non-empty
		// and other is empty, return false
		else if (root1 != null &&
				root2 == null)
			return false;
		else if (root1 == null &&
				root2 != null)
			return false;
		else{
			// Check if current data of both trees equal
			// and recursively check for left and right subtrees
			if (root1.data == root2.data &&
					isIdentical(root1.left, root2.left) == true &&
					isIdentical(root1.right, root2.right) == true)
				return true;
			else
				return false;
		}
	}
	/**
	 * Returns the first (lowest) element currently in this tree.
	 * @return the first element through inorder traversal
	 * @throws NoSuchElementException if the set is empty
	 */
	public E first() throws NoSuchElementException {
		if(this.root == null) {
			throw new NoSuchElementException("The tree is empty, no first element available.");
		}

		BSTNode current = this.root;

		// loop down to find the leftmost leaf 
		while (current.left != null) {
			current = current.left;
		}
		return (current.data);	
	}

	/**
	 * Returns the last (highest) element currently in this tree.
	 * @return the last element through in order traversal
	 * @throws NoSuchElementException if the tree set is empty
	 */
	public E last() throws NoSuchElementException {
		if(this.root == null) {
			throw new NoSuchElementException("The tree is emptry, no last value available.");
		}
		BSTNode current = this.root;
		//loop down to find the right most element
		while(current.right != null) {
			current = current.right;
		}
		return current.data;
	}

	/**
	 * Returns the element at the specified position in this tree. 
	 * The order of the indexed elements is the same as provided by this tree's iterator. 
	 * The indexing is zero based 
	 * (i.e., the smallest element in this tree is at index 0 and the largest one is at index size()-1). 
	 * @param index
	 * @return
	 * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index >= size())
	 */
	public E get(int index) throws IndexOutOfBoundsException {
		if(index>=this.size()) {
			throw new IndexOutOfBoundsException("The index cannot be larger than or equal to the size.");
		}
		E res = get(this.root, index+1).data;
		return res;
	}
	/**
	 * recursive method to get the k'th or index+1 th smallest element in the BST
	 */
	int count = 0;
	private BSTNode get(BSTNode root, int k)
	{
		// base case
		if (root == null)
			return null;

		// search in left subtree
		BSTNode left = get(root.left, k);

		// if k'th smallest is found in left subtree, return it
		if (left != null)
			return left;

		// if current element is k'th smallest, return it
		count++;
		if (count == k)
			return root;

		// else search in right subtree
		return get(root.right, k);
	}

	/**
	 * Helper method that calls the recursive higher method.
	 * @param e the value to match
	 * @return the least element greater than e, or null if there is no such element
	 * @throws ClassCastException
	 * @throws NullPointerException
	 */
	public E higher(E e) throws ClassCastException, NullPointerException{
		if(e == null)
			throw new NullPointerException("Cannot find the least higher element for null value.");
		return higher(this.root, e);
	}
	//
	/**
	 * Recursive method to find the closest element higher compared to e
	 * Returns the least element in this tree strictly greater than the given element,
	 * or null if there is no such element.
	 * @param root
	 * @param e e the value to match
	 * @return
	 */
	private E higher(BSTNode root, E e) {

		// Base cases
		if (root == null)
			return null;
		if (root.left == null &&root.right == null && root.data.compareTo(e) < 0)
			return null;

		else if (root.data.compareTo(e) == 0) {
			E k = higher(root.right, e);
			if (k == null)
				return null;
			else
				return k;
		}
		// If root's value is greater, try in left
		// subtree
		else if (root.data.compareTo(e) > 0) {
			E k = higher(root.left, e);
			if (k == null)
				return root.data;
			else
				return k;
		}

		// If root's value is smaller, return value
		// from right subtree.
		else if (root.data.compareTo(e) < 0)
			return higher(root.right, e);
		else
			return null;
	}




	/**
	 * Helper method that calls the recursive lower method
	 * @param e
	 * @return
	 * @throws ClassCastException
	 * @throws NullPointerException
	 */
	public E lower(E e) throws ClassCastException, NullPointerException {
		if(e == null) {
			throw new NullPointerException("Cannot find the closest lower element for null value.");
		}
		return lower(this.root, e);
	}
	/**
	 * Recursive method that returns the greatest value that is strictly lower than e
	 * returns null if there is no such element
	 * @param root
	 * @param e
	 * @return
	 */
	private E lower(BSTNode root, E e) {

		// Base cases
		if (root == null)
			return null;
		if (root.left == null &&root.right == null && root.data.compareTo(e) > 0)
			return null;

		else if (root.data.compareTo(e) == 0) {
			E k = lower(root.left, e);
			if (k == null)
				return null;
			else
				return k;
		}
		// If root's value is smaller, try in right
		// subtree
		else if (root.data.compareTo(e) < 0) {
			E k = lower(root.right, e);
			if (k == null)
				return root.data;
			else
				return k;
		}

		// If root's key is greater, return value
		// from left subtree.
		else if (root.data.compareTo(e) > 0)
			return lower(root.left, e);
		else
			return null;
	}


	/**
	 * @return an iterator over the elements in this tree in order of the inorder traversal
	 */
	public Iterator<E> iterator(){
		Iterator<E> Itr = new BSTIterator(this.root);
		return Itr;

	}
	/**
	 * 
	 * @return an iterator over the elements in this tree in order of the postorder traversal
	 */
	public Iterator<E> postorderIterator(){
		Iterator<E> Itr = new PostOrderIterator(this.root);
		return Itr;
	}
	/**
	 * @return an iterator over the elements in this tree in order of the preorder traversal
	 */
	public Iterator<E> preorderIterator(){
		Iterator<E> Itr = new PreOrderIterator(this.root);
		return Itr;
	}

	/**
	 * Finds and removes Object o from the tree. 
	 * @param o
	 * @return true if there is an element equal to the object, false otherwise
	 * @throws NullPointerException if the object is null
	 * @throws ClassCastException
	 */
	@SuppressWarnings("unchecked")
	public boolean remove(Object o) throws NullPointerException, ClassCastException {
		if(o == null) {
			throw new NullPointerException("Cannot remove a null element from the tree.");
		}

		try {
			E obj = (E) o;
			Comparable<E> co = (Comparable<E>) o;
			remove(root, obj);
			if(Objects.equals(co, tmp))
				found = true;
			else
				found = false;
		}
		catch (ClassCastException ex) {
			System.err.println("ClassCastException has been thrown");
		}
		return found; 


	}

	E tmp; // datafield that contains the data of the removed root by the recursive remove method
	/**
	 * Recursive remove method that removes a node and replaces it by its inOrderSuccessor
	 * the node does not get replaced if it has no children
	 * @param root
	 * @param value to be removed 
	 * @return the root
	 */ 
	BSTNode remove(BSTNode root, E value) {

		if (root == null)
			return root;

		if (value.compareTo(root.data) < 0) {      //if value is less than root, look in the left subtree
			root.left = remove(root.left, value);
		} else if (value.compareTo(root.data) > 0) { //if value is more than root, look in the right subtree
			root.right = remove(root.right, value);
		} else { //if the value is equal to the root
			tmp = root.data; //storing the data that is equal to the root
			if (root.left == null) 
				return root.right;
			else if (root.right == null)
				return root.left;



			root.data = inOrderSuccessor(root.right);
			root.right = remove(root.right, root.data);
		}

		return root;

	}
	/**
	 * Method that returns the minimum value of the subtree with a given root
	 * called inside the recursive remove method to find the inOrderSuccessor or a node
	 * @param root
	 * @return
	 */
	private E inOrderSuccessor(BSTNode root) {
		E minimum =  root.data;
		while (root.left != null) {
			minimum = root.left.data;
			root = root.left;
		}
		return minimum;
	}

	@Override
	/**
	 * Helper method that calls the recursive toString method and 
	 * returns the elements in the tree in inorder traversal enclosed in  square brackets []
	 */

	public String toString() {

		return "[" + toString( this.root).substring(0, toString( this.root).length() - 2) + "]";
	}
	/**
	 * Recursive method that returns a string containing the tree elements in inorder traversal
	 * enclosed in  square brackets []
	 * @param root
	 * @return String
	 */
	private String toString(BSTNode root) {
		if (root == null)
			return "";
		StringBuffer sb = new StringBuffer();
		sb.append(toString(root.left));
		sb.append(root.data);
		sb.append(", ");
		sb.append(toString(root.right));
		return String.valueOf(sb);

	}





	/**
	 * Helper method that calls the recursive toStringTree
	 * @return
	 */
	public String toStringTreeFormat() {
		StringBuffer sb = new StringBuffer(); 
		toStringTree(sb, root, 0);
		return sb.toString();
	}
	/**
	 * Produces tree like string representation of this tree. 
	 * Returns a string representation of this tree in a tree-like format. 
	 * @param sb
	 * @param node
	 * @param level
	 *This method is a work of Joanna Klukowska
	 **/
	//uses preorder traversal to display the tree 
	//WARNING: will not work if the data.toString returns more than one line 
	private void toStringTree( StringBuffer sb, BSTNode node, int level ) {
		//display the node 
		if (level > 0 ) {
			for (int i = 0; i < level-1; i++) {
				sb.append("   ");
			}
			sb.append("|--");
		}
		if (node == null) {
			sb.append( "null\n"); 
			return;
		}
		else {
			sb.append( node.data + "\n"); 
		}

		//display the left subtree 
		toStringTree(sb, node.left, level+1); 
		//display the right subtree 
		toStringTree(sb, node.right, level+1); 
	}







	/**
	 * This class represents a node of the mountain. It contains a constructor
	 * for the node and methods for comparing the node, setting or updating its
	 * height and level, checking if it has been visited by the hiker, and
	 * calculating its balance factor.
	 *
	 * @author Chloe Baatar * @version 12/04/2022
	 */
	protected class BSTNode implements Comparable < BSTNode > {

		// Create private variables for the node
		protected E data;
		protected BSTNode  left;
		protected BSTNode  right;
		protected int height;
		protected int level;
		protected boolean visited;

		/**
		 * Constructor for a node of the mountain.
		 *
		 * @param data RestStop object for the node to contain.
		 */
		public BSTNode ( E data ) {
			this.data = data;
		}

		/**
		 * Compares two nodes of the mountain.
		 *
		 * @param other node to be compared.
		 * 
		 * @throws NullPointerException if the parameter is null.
		 *
		 * @return a positive integer if the node's is greater than other's,
		 * negative if it is less than, and zero if they're equal.
		 */
		public int compareTo ( BSTNode other ) throws NullPointerException {
			if (other==null) {
				throw new NullPointerException("Cannot compare to null node.");
			}
			return this.data.compareTo(other.data);
		}

		/**
		 * Sets the height of the node.
		 *
		 * @param height integer height to be set.
		 */
		public void setHeight(int height) {
			this.height = height;
		}

		/**
		 * Updates the height of a node.
		 */
		public void updateHeight() {
			// If the node is a leaf
			if (this.left==null && this.right==null) {
				this.height = 0; // set the height to 0
			}
			// If the node only has a right child
			else if (this.left == null) {
				// Set the height to one more than its child's
				this.height = this.right.height + 1;
			}
			// If the node only has a left child
			else if (this.right == null) {
				// Set the height to one more than its child's
				this.height = this.left.height + 1;
			}
			// If the node has two children
			else {
				// Set the height one more than the max of it's children's
				this.height = 1 + Math.max( this.left.height, 
						this.right.height );
			}
		}

		/**
		 * Sets the level of a node.
		 *
		 * @param level integer level to be set.
		 */
		public void setLevel(int level) {
			this.level = level;
		}

		/**
		 * Sets the node's visited boolean to true.
		 */
		public void setVisited() {
			this.visited = true;
		}

		/**
		 * Calculates the balance factor of a node.
		 *
		 * @return balance factor (integer value between -2 and 2).
		 */
		public int balanceFactor () {
			// If the node only has one child
			if ( this.right == null ) {
				return -this.height; // balance factor equals the nodes height
			}
			if ( this.left == null ) {
				return this.height; // balance factor equals the nodes height
			}
			// If the node has two children, balance factor equals
			// the difference between the height of the right and left child
			return this.right.height - this.left.height;
		}
	}



	/**
	 * This class represents an inOrderIterator for BST class
	 * @author chloe
	 *
	 */
	private class BSTIterator implements Iterator<E> {

		private Stack<BSTNode> stack;

		public BSTIterator(BSTNode root) {
			stack = new Stack<>();
			if (root != null)
				init(root);
		}

		private void init(BSTNode root) {

			while (root != null) {
				stack.push(root);
				root = root.left;
			}

		}

		/**
		 * @return the next smallest element
		 */
		public E next() {
			if (hasNext()) {

				BSTNode current = stack.pop();

				process(current.right);

				return current.data;
			}

			return null;
		}

		private void process(BSTNode root) {
			init(root);
		}

		/**
		 * @return whether we have a next smallest element
		 */
		public boolean hasNext() {

			return !stack.isEmpty();
		}
	}
	/**
	 * This class represents a PreOrderIterator for a BST
	 * @author chloe
	 *
	 */
	private class PreOrderIterator implements Iterator <E> {
		private Stack<BSTNode> stack;
		public PreOrderIterator(BSTNode root) {
			stack = new Stack<>();
			if (root != null)
				init(root);
		}
		private void init(BSTNode root) {
			stack.push(root);
		}
		/**
		 * @return the next smallest element
		 */
		public E next() {
			BSTNode node = stack.pop();

			if (node.right != null)
				init(node.right);

			if (node.left != null)
				init(node.left);

			return node.data;
		}
		/**
		 * @return true if we have smaller element
		 */
		public boolean hasNext() {

			return !stack.isEmpty();
		}
	}
	/**
	 * This class represents a Post Order Iterator for a BST
	 * @author chloe
	 *
	 */
	private class PostOrderIterator implements Iterator<E>{

		private Stack<BSTNode> stack;
		public PostOrderIterator(BSTNode root) {
			stack = new Stack<>();
			init(root);
		}

		/**
		 * find the first leaf in a tree rooted at cur and store intermediate nodes
		 */
		private void init(BSTNode root) {
			while (root != null) {
				stack.push(root);
				if (root.left != null)
					root = root.left;
				else
					root = root.right;
			}
		}

		/**
		 * @return the next smallest element
		 */
		public E next() {
			BSTNode node = stack.pop();
			if (!stack.isEmpty()) {
				if (node == stack.peek().left) {
					init(stack.peek().right);  // find next leaf in right sub-tree
				}

			}
			return node.data;
		}

		/**
		 * @return true if we have smaller element
		 */
		public boolean hasNext() {

			return !stack.isEmpty();
		}
	}


}