package tree;

import java.util.Collection;

/**
 * This class represents a non-empty search tree. An instance of this class
 * should contain:
 * <ul>
 * <li>A key
 * <li>A value (that the key maps to)
 * <li>A reference to a left Tree that contains key:value pairs such that the
 * keys in the left Tree are less than the key stored in this tree node.
 * <li>A reference to a right Tree that contains key:value pairs such that the
 * keys in the right Tree are greater than the key stored in this tree node.
 * </ul>
 *  
 */
 public class NonEmptyTree<K extends Comparable<K>, V> implements Tree<K, V> {

	/* Provide whatever instance variables you need */
	
	private Tree<K,V> left,right;
	private K key;
	private V value;
	
	/**
	 * Only constructor we need.
	 * @param key
	 * @param value
	 * @param left
	 * @param right
	 */
	public NonEmptyTree(K key, V value, Tree<K,V> left, Tree<K,V> right) {
		//left and right nodes
		this.left = left;
		this.right = right;
		
		//key/value pair
		this.key = key;
		this.value = value;
	}

	/**
	 * Find the value that this key is bound to in this tree
	 * @returns V - value associated with the key by this Tree (null if the key does not have an association in this tree)
	 */
	public V search(K key) {
	
		//go right
		if(key.compareTo(this.key) > 0)
		{
			return this.right.search(key);
		}
		
		//go left
		if(key.compareTo(this.key) < 0)
		{
			return this.left.search(key);
		}
		
		//nowhere left to go
		return this.value;
	}
	
	/**
	 * Inserts/Updates the tree with a new key/value pair.
	 * Updates the value of a key if it already exists in the tree
	 * If the key does not exist within the tree, it will be inserted
	 * (at the appropriate position) with its corresponding value
	 * 
	 * @return NonEmptyTree<K,V> that has been updated with new key/value
	 */
	public NonEmptyTree<K, V> insert(K key, V value) {
		
		if(key.compareTo(this.key) > 0)
		{
			this.right = this.right.insert(key, value);
			return this;
		}
		
		if(key.compareTo(this.key) < 0)
		{
			this.left = this.left.insert(key, value);
			return this;
		}
		
		this.value = value;
		return this;
	}
	
	
	public Tree<K, V> delete(K key) {
		
		if(key.compareTo(this.key) > 0)
		{
			this.right = this.right.delete(key);
		}
		
		else if(key.compareTo(this.key) < 0)
		{
			this.left = this.left.delete(key);
		}
		else{
		/*
		 * Deleting for a node with two possible children
		 */
		try{
			//case: left has max (left.max becomes new parent)
			this.value = this.search(this.right.min());
			this.key = this.right.min();
			this.right = this.right.delete(this.key);	
		}
		catch(TreeIsEmptyException e)
		{
			return this.left;
		}
		}
		return this;
	}
	
	/**
	 * Returns maximum key in the subtree
	 * @return K - maximum Key in this subtree
	 */
	public K max() {
		K key = this.key;
		try{
			return this.right.max();
		}
		catch(TreeIsEmptyException r)
		{
			return key;
		}
	}
	
	/**
	 * Returns minimum key within the subtree
	 * @return K - minimum key in this subtree
	 */
	public K min() {
		K key = this.key;
		
		try{
			return this.left.min();
		}
		catch(TreeIsEmptyException l)
		{
			return key;
		}
	}

	/**
	 * Returns the number of keys bound to this tree
	 * @return int - number of keys bound to this tree
	 */
	public int size() {
		return 1 + this.left.size() + this.right.size();
	}

	/**
	 * Adds all of the keys bound to this tree to a collection
	 */
	public void addKeysToCollection(Collection<K> c) {
		c.add(this.key);
		this.left.addKeysToCollection(c);
		this.right.addKeysToCollection(c);
	}
	
	
	/**
	 * Returns a subtree of this tree that contains all existing keys between fromKey and toKey
	 * @return Tree<K,V> - a subtree that contain all existing keys between fromKey and toKey (inclusive)
	 */
	public Tree<K,V> subTree(K fromKey, K toKey) {
		
		//this.key > toKey
		if(this.key.compareTo(toKey) > 0)
		{
			return this.left.subTree(fromKey, toKey);
		}
		
		//this.key < fromKey
		if(this.key.compareTo(fromKey) < 0)
		{
			return this.right.subTree(fromKey, toKey);
		}
		
		//fromKey < this.key < toKey
		return new NonEmptyTree<K,V>(this.key,this.value,
				this.left.subTree(fromKey, toKey),this.right.subTree(fromKey, toKey));
	}
	
	
	/**
	 * Returns height of the current tree
	 * @return int - defines the height of this tree
	 */
	public int height() {
		int countRight = 1 + this.right.height();
		int countLeft = 1 + this.left.height();
		
		return (countLeft > countRight) ? countLeft : countRight;
	}
	
	/**
	 * Performs an inorder traversal of each key-value pair bound to this tree
	 */
	public void inorderTraversal(TraversalTask<K,V> p) {
		this.left.inorderTraversal(p);
		p.performTask(this.key, this.value);
		this.right.inorderTraversal(p);
	}
	
	/**
	 * Performs a right-root-left traversal of each key-value pair bound to this tree
	 */
	public void rightRootLeftTraversal(TraversalTask<K,V> p) {
		this.right.rightRootLeftTraversal(p);
		p.performTask(this.key, this.value);
		this.left.rightRootLeftTraversal(p);
	}	
	
	
	
	/*
	public String toString()
	{
		String str = "";
		
		str = "Key: " + this.key + "  Value: " + this.value
				+ "\nRight(" + this.key + "): " + this.right + 
				"  \nLeft(" + this.key + "): " + this.left;
		return str;
	}*/
	
}