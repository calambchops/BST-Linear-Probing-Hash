package edu.csus.csc130.assignment3;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 
 * Ordered Symbol Table implementation using Binary Search Tree
 */
public class BST<Key extends Comparable<Key>, Value> 
			extends AbstractOrderedSymbolTable<Key, Value>{
	private class Node {
		private Key key; // key
		private Value val; // associated value
		private Node left, right; // links to subtrees
		private int n; // # nodes in subtree rooted here
		private int h; // # of levels in tree
		
		public Node(Key key, Value val, int n, int h) {
			this.key = key;    //reference to current object
			this.val = val;    //reference to current object
			this.n = n;        //reference to current object
			this.h = h;        //reference to current object
		}
	
	}
	
	
	private Node root; // root of BST
	
	// get the number of nodes from this tree
	public int size() {
		return size(root);
	}

	// get the number of nodes rooted at Node node
	private int size(Node node) {
		if (node == null)
			return 0;
		else
			return node.n;
	}
	
	public Value get(Key key) {
		return get(root, key);
	}
	
	// Return value associated with key in the subtree rooted at node;
	// return null if key not present in subtree rooted at node.
	private Value get(Node node, Key key) {
		if (node == null) return null;
		
		int cmp = key.compareTo(node.key);
		if (cmp < 0)
			return get(node.left, key);
		else if (cmp > 0)
			return get(node.right, key);
		else
			return node.val;
	}

	public void put(Key key, Value val)	{
		root = put(root, key, val);
	}
	
	// Change key’s value to val if key in subtree rooted at node.
	// Otherwise, add new node to subtree associating key with val.
	private Node put(Node node, Key key, Value val) {
		if (node == null) return new Node(key, val, 1, 0);

		int cmp = key.compareTo(node.key);
		if (cmp < 0)
			node.left = put(node.left, key, val);
		else if (cmp > 0)
			node.right = put(node.right, key, val);
		else
			node.val = val;
		
		
		if(node.left == null){
			node.h = node.right.h + 1;
		}
		else{
			node.h = node.left.h + 1;
		}
	
		node.n = size(node.left) + size(node.right) + 1;
		
		
		return node;
	}

	@Override
	public Key min() {
		return min(root).key;
	}
	
	private Node min(Node node) {
		if (node.left == null)
			return node;
		return min(node.left);
	}

	@Override
	public Key max() {
		return max(root).key;
	}
	
	private Node max(Node node) {
		if (node.right==null) {
			return node;
		} else {
			return max(node.right);
		}
	}

	@Override
	// find the greatest key less or equal to the given key
	public Key floor(Key key) {
		// unimplemented
		return null;
	}

	@Override
	public Key ceiling(Key key) {
		// unimplemented
		return null;
	}

	@Override
	public int rank(Key key) {
		return rank(root, key);
	}
	
	// Return number of keys less than node.key in the subtree rooted at node.
	private int rank(Node node, Key key) {
		if (node == null) return 0;
		
		int cmp = key.compareTo(node.key);
		if (cmp < 0)
			return rank(node.left, key);
		else if (cmp > 0)
			return 1 + size(node.left) + rank(node.right, key);
		else
			return size(node.left);
	}	

	@Override
	public Iterable<Key> keys(Key lo, Key hi) {
		Queue<Key> queue = new LinkedList<Key>();
		keys(root, queue, lo, hi);
		return queue;
	}
	
	private void keys(Node node, Queue<Key> queue, Key lo, Key hi) {
		if (node == null) return;
		
		int cmplo = lo.compareTo(node.key);
		int cmphi = hi.compareTo(node.key);
		if (cmplo < 0)
			keys(node.left, queue, lo, hi);
		if (cmplo <= 0 && cmphi >= 0)
			queue.add(node.key);
		if (cmphi > 0)
			keys(node.right, queue, lo, hi);
	}	

	@Override
	public void delete(Key key) {
		delete(root, key);
	}
	
	private Node delete(Node node, Key key) {
		if (node == null) return null;
		
		int cmp = key.compareTo(node.key);
		if (cmp < 0) {
			node.left = delete(node.left, key);
		} else if (cmp > 0) {
			node.right = delete(node.right, key);
		} else {
			if (node.right == null) {
				return node.left;
			} else if (node.left == null) {
				return node.right;
			} else {
				Node curNode = node;
				node = min(curNode.right); // curNode's successor
				node.right = deleteMin(curNode.right);
				node.left = curNode.left;
			}
		}
		node.n = size(node.left) + size(node.right) + 1;
		return node;
	}
	 
	public void deleteMin() {
		root = deleteMin(root);
	}
	
	// delete the node with minimum key 
	// and return the new root of the subtree
	private Node deleteMin(Node node) {
		if (node.left == null) return node.right;
		node.left = deleteMin(node.left);
		node.n = size(node.left) + size(node.right) + 1;
		return node;
	}
	
	public void printInorder() {
		printInorder(root);
	}
	
	private void printInorder(Node node) {
		if (node == null) return;
		printInorder(node.left);
		System.out.print(node.key + ", ");
		printInorder(node.right);
	}
	
	public void printPreorder() {
		printPreorder(root);
	}
	
	private void printPreorder(Node node) {
		if (node == null) return;
		System.out.print(node.key + ", ");
		printPreorder(node.left);
		printPreorder(node.right);		
	}
	
	public void printPostorder() {
		printPostorder(root);
	}
	
	private void printPostorder(Node node) {
		if (node == null) return;
		printPostorder(node.left);
		printPostorder(node.right);	
		System.out.print(node.key + ", ");
	}
	

	@Override
	// return the kth key
	public Key select(int k) {
		Node node = select(root, k);
		if (node==null) {
			return null;
		} else {
			return node.key;
		}
	}
	
	// Return Node containing kth key (zero based)
	private Node select(Node node, int k) { 
		if (node == null) return null;
		
		int t = size(node.left);
		if (t > k)
			return select(node.left, k);
		else if (t < k)
			return select(node.right, k - t - 1);
		else
			return node;
	}	
	
	/**
	 * Provide a non-recursive implementation of select(...)  here
	 */
	public Key selectI(int k) {
		//provide your implementation here
		Node node = select(root, k);
		if (node==null) {
			return null;
		} else{
			return node.key;			
		}
		
		
	}	
	
	
	public Node selectI(Node node, int k) {
		//provide your implementation here	
		int t = size(node.left);
		
		while(k != t){
				
			while(t > k){
				node = node.left;
				t = size(node.left);
			}
			
			while(t < k){
				node = node.right;
				k = k - t - 1;
				t = size(node.left);	 
			}
				
		}	
		return node;
	}	
	
	
	
	public  int getHeight() {
		return getHeight(root);
	}
	
	private int getHeight(Node node) {
		if (node==null) {
			return -1;
		} else {
			int leftHeight = getHeight(node.left);
			int rightHeight = getHeight(node.right);
			return Math.max(leftHeight, rightHeight) + 1;
		}
	}
	
	
	public int getHeightI() {
		
		return getHeightI(root);
	}
	
	/**
	 * Provide a non-recursive implementation of getHeight() here
	 */
	private int getHeightI(Node node) {
		// provide your implementation here
			if (node == null)
				return 0;
			else
				return node.h;
		
	}
			
		

	

}
